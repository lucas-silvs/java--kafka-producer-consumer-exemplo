# Confluent Plaftorm & Kubernetes - Estudo de Integrações com o Kafka

Projeto desenvolvido utilizando Java 21 e Spring boot 3 utilizando Kafka com um producer e um consumer seguindo o modelo de Arquitetura Orientada a Evento

## Sumário

- [Requisitos](#requisitos)
- [Configurações Kafka](#configurações-kafka)
  - [Kafka Local:](#kafka-local)
    - [Criar tópico Kafka](#criar-tópico-kafka)
    - [Acompanhar tópico](#acompanhar-tópico)
  - [Utilizando Docker](#utilizando-docker)
  - [Utilizando Kubernetes](#utilizando-kubernetes)
- [Comandos Kafka CLI](#comandos-kafka-cli)
  - [Reasing topic para outro broker kafka](#reasing-topic-para-outro-broker-kafka)
- [Mirror Maker 2](#mirror-maker-2)
- [Kafka Confluent Replicator](#kafka-confluent-replicator)
- [Uso de multiplos consumers e producers](#uso-de-multiplos-consumers-e-producers)
  - [Producer](#producer)
  - [Consumer](#consumer)
- [Monitoramento](#monitoramento)
  - [Grafana e Prometheus](#grafana-e-prometheus)
  - [Painel de controle do Kafka - Confluent Control Center](#painel-de-controle-do-kafka---confluent-control-center)
- [Escalando aplicações Kubernetes com o KEDA](#escalando-aplicações-kubernetes-com-o-keda)
  - [Configurando ambiente do KEDA](#configurando-ambiente-do-keda)
- [Referencias](#referencias)

## Requisitos

- Java 21
- Spring 3.3.1
- Gradle 8.8
- Kafka 3.5.0
- Docker
- Kubernetes (opcional)

## Configurações Kafka

### Kafka Local:

#### Criar tópico Kafka

Antes de executar o projeto Spring, é necessário criar o tópico Kafka. Para isso, devemos antes iniciar o Kafka, onde iniciamos o Zookeper e o kafka com os comandos abaixo:

- Iniciar Zookeper:

```
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
```

- Iniciar Kafka:

```
./bin/kafka-server-start.sh ./config/server.properties
```

Para criar o tópico kafka, é necessário executar o comando abaixo na pasta do Kafka:

```
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic topico-teste --create --partitions 3 --replication-factor 1
```

#### Acompanhar tópico

Para acompanhar e validar se as mensagens estão sendo enviadas para o tópico, execute o comando abaixo:

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topico-teste --from-beginning
```

### Utilizando Docker

Também é possivel executar o Kafka com o arquivo Docker disponibilizado no repositório na pasta `docker/confluent-all-in-one/` . Para subir o Kafka utilizando o Docker, deve executar o comando abaixo:

```
docker-compose up
```

### Utilizando Kubernetes

Também foi disponibilizado a configuração de subir um ambiente Kafka utilizando o Kubernetes, baseado no ambiente do ambiente Docker. Para subir o ambiente Kafka no Kubernetes, basta acessar a pasta `docker/confluent-all-in-one/` e executar o comando abaixo, que irá criar todos os artefatos Kubernetes:

```sh
kubectl apply -f kubernetes
```

## Comandos Kafka CLI

### Reasing topic para outro broker kafka

Abaixo:

```sh
kafka-reassign-partitions.sh --zookeeper <YOUR_ZOOKEEPER> --verify --reassignment-json-file <YOUR_JSON_FILE> 2>&1 | grep "Leader: -1" | awk '{print $2}' | sort | uniq | awk 'BEGIN{printf "{\"topics\": [\n"} {printf "    {\n        \"topic\":  \"%s\"\n    },\n", $1} END{print "]}\n"}' | sed '$s/,$//'
```

```sh
kafka-reassign-partitions.sh --zookeeper <YOUR_ZOOKEEPER> --verify --reassignment-json-file <YOUR_JSON_FILE> 2>&1 | grep "Leader: -1" | awk '{print $2}' | sort | uniq | awk 'BEGIN{printf "{\"topics\": [\n"} {printf "    {\n        \"topic\":  \"%s\"\n    },\n", $1} END{print "]}\n"}' | sed '$s/,$//' > topics_without_leader.json
```

Com isso será criado o Broker Kafka, Zookeper, Schema Registry e o Kafka UI da Confluent, onde o gerenciamento será realizado pela interface e pode ser acessado acessando a url [Control Center](http://localhost:9021/).

## Monitoramento

### Grafana e Prometheus

É possivel monitorar os recursos do cluster Kubernetes utilizando o Prometheus e o Grafana, para isso.
Como foi utilizando o Microk8s para os testes local, execute os comandos abaixo para instalar o Prometheus e o Grafana:

```sh
# Via Microk8s
microk8s enable observability
```

Com isso, será habilitado o prometheus e o Dash do Grafana, que pode ser acessado pela url http://localhost:37349,
onde o usuário e senha padrão é admin/prom-operator.

### Painel de controle do Kafka - Confluent Control Center

Também é possivel monitorar o Kafka utilizando o Confluent Control Center,
que pode ser acessado pela url http://localhost:9021, que monitora os recursos internos do Kafka, como o KSQLDB,
Schema Registry, Kafka Connect, Kafka Broker, Kafka Rest Proxy.

## Escalando aplicações Kubernetes com o KEDA

Para a escala de aplicações Kubernetes, foi utilizado o KEDA, que é um componente que permite escalar aplicações baseado
em métricas mais complexas, no caso foi utilizado o lag do consumer Kafka. Para isso, foi substituido o artefato HPA (Horizontal Pod Autoscaler)
pelo ScaledObject do KEDA, que pode ser visto no arquivo `hpa.yaml` na pasta `kubernetes/` nos projetos producer e consumer.
No arquivo contem o exemplo de implementação com HPA e com o ScaledObject do KEDA, onde está escalando com memoria e cpu, e para o consumer inclui
a metrica do lag do consumer.

### Configurando ambiente do KEDA

com o KEDA instado, agora será configurado a configuração de autenticaçao do KEDA com o Kafka,
para isso será necessário disponibilizar as credenciais do cluster Kafka.

Neste estudo foi utilizado o AWS Secret Manager para armazenar as credenciais, e o External secret Operator para disponibilizar
as credenciais para o Keda

Para isso, siga o procedimento abaixo:

1. Instale o KEDA + External Secret Operator com o comando abaixo:

```sh

# instalar o KEDA

## Via Microk8s
microk8s enable keda

## Via Helm
helm repo add kedacore https://kedacore.github.io/charts
helm repo update
helm install keda kedacore/keda --namespace keda --create-namespace

# instalar o External Secret Operator

## Via Healm
helm repo add external-secrets https://charts.external-secrets.io
helm install external-secrets \
   external-secrets/external-secrets \
    -n external-secrets \
    --create-namespace
```

2. Com os plugins instalados, devemos configurar as credenciais da AWS para o External Secret Operator. Para isso, ajuste o arquivo `aws-secret.yaml` na pasta `kubernetes/keda/aws/` do projeto producer e consumer, com as credenciais de um usuario IAM (AWS acess key e AWS secret key id).

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: keda-aws-credential-secret
  namespace: external-secrets

type: Opaque
data:
  AWS_ACCESS_KEY_ID: <base64-encoded> # echo -n "AWS_ACCES_KEY_ID" | base64
  AWS_SECRET_ACCESS_KEY: <base64-encoded> # echo -n "AWS_SECRET_ACCESS_KEY" | base64
```

3. Com as credenciais configuradas, agora iremos ajustar o artefato que irá obter criar o cliente AWS para o External Operator, utilizando a secret criada na etapa 2.

```yaml
apiVersion: external-secrets.io/v1beta1
kind: ClusterSecretStore
metadata:
  name: aws-secretsmanager
spec:
  provider:
    aws:
      service: SecretsManager
      region: us-east-1
      auth:
        secretRef:
          accessKeyIDSecretRef:
            name: keda-aws-credential-secret #nome dos kubernetes secrets com as credenciais
            namespace: external-secrets
            key: AWS_ACCESS_KEY_ID # nome do campo da credencial access-key no kubernetes secret
          secretAccessKeySecretRef:
            name: keda-aws-credential-secret #nome dos kubernetes secrets com as credenciais
            namespace: external-secrets
            key: AWS_SECRET_ACCESS_KEY # nome do campo da credencial access-key no kubernetes secret
```

4. Agora devemos criar o artefato obter o valor da secret na aws e criar o Kubernetes secret com o valor da secret da AWS.

```yaml
apiVersion: external-secrets.io/v1beta1
kind: ClusterExternalSecret
metadata:
  name: cluster-keda-credentials-external-secret
spec:
  namespaceSelector:
    # Validação para quais namespace será criada o Kubernetes Secrets
    matchLabels:
      name: "keda"
  externalSecretName: "keda-secret" # nome do Kubernetes Secrets que será criado
  externalSecretSpec:
    refreshInterval: "1h" # Intervalo que a secret será consultada novamente na AWS
    secretStoreRef:
      name: aws-secretsmanager
      kind: ClusterSecretStore
    target:
      name: keda-credentials
      creationPolicy: Owner
    data:
      - secretKey: username # nome do campo no Kubernetes Secrets
        remoteRef:
          key: "k8s/kafka/keda-user" # nome da secret na AWS
          property: username # nome do campo na Secret na AWS
      - secretKey: password # nome do campo no Kubernetes Secrets
        remoteRef:
          key: "k8s/kafka/keda-user" # nome da secret na AWS
          property: password # nome do campo na Secret na AWS
```

5. Pronto, agora será necessário configurar o KEDA para usar esse Kubernetes Secret para se conectar com o Kafka, primeiro devemos configurar o artefato do KEDA que irá mapear a secret para o uso no Trigger do Keda:

```yaml
apiVersion: keda.sh/v1alpha1
kind: ClusterTriggerAuthentication
metadata:
  name: kafka-trigger-auth-aws-secret-manager
spec:
  secretTargetRef:
    - parameter: username # nome do campo de autenticação que o KEDA irá usar
      name: keda-credentials # nome do Kubernetes secrets
      key: username # nome do campo no Kubernetes Secrets
    - parameter: password  nome do campo de autenticação que o KEDA irá usar
      name: keda-credentials # nome do Kubernetes secrets
      key: password # nome do campo no Kubernetes Secrets
```

6. Agora basta somente configurar o Trigger do Keda, utilizando o Cluster Trigger Authentication criado na etapa 5:

```yaml
apiVersion: keda.sh/v1alpha1
kind: ScaledObject
metadata:
  name: kafka-keda-consumer-trigger
  namespace: consumer
spec:
  minReplicaCount: 2 # Defina aqui o número mínimo de réplicas desejado
  maxReplicaCount: 6 # Defina aqui o número máximo de réplicas desejado

  scaleTargetRef:
    name: consumer-deployment
  pollingInterval: 3
  triggers:
    #  Configuração para configuração de trigger utilizando lag de consumer Kafka
    - type: kafka
      metadata:
        bootstrapServers: broker.kafka.svc.cluster.local:9093
        consumerGroup: consumer-teste # Consumer group que será monitorado
        topic: topico-teste-avro # Tópico que será monitorado
        lagThreshold: "3" # Limite de lag para iniciar a escala
        offsetResetPolicy: latest # Política de reset de offset
        sasl: plaintext # Configuração de autenticação

      # Configuração para utilizar objeto de autenticação do tipo ClusterTriggerAuthentication (que contem valor do AWS Secret Manager)
      # Esse artefato disponibiliza os campos "username" e "password" para autenticação
      authenticationRef:
        name: kafka-trigger-auth-aws-secret-manager
        kind: ClusterTriggerAuthentication

    #  Configuração para configuração de trigger utilizando porcentagem de utilização do CPU
    - type: cpu
      metricType: Utilization
      metadata:
        value: "60" #valor é calculado em porcentagem
```

Após aplicar o artefato de Trigger junto com o Deploy da aplicação, a aplicação passará a escalar a partir do lag do tópico Kafka e do consumo de CPU

Para simular um lag no consumer, altere a variavel `ENABLE_MOCK_LAG` para `true` no arquivo `configMap.yaml` na pasta `kubernetes/` do projeto consumer.
Com isso ao produzir as mensagens com o consumer, o consumer irá aguardar 30 segundos antes de consumir a mensagem, simulando um lag.

## Referencias

- [Docker](https://www.docker.com/)
- [Kafka](https://kafka.apache.org/)
- [Spring](https://spring.io/projects/spring-boot)
- [Confluent](https://docs.confluent.io/5.5.1/quickstart/ce-docker-quickstart.html)
- [Kubernetes](https://kubernetes.io/)
- [Microk8s](https://microk8s.io/)
- [KEDA](https://keda.sh/)
- [External Secret Operator](https://external-secrets.io/latest/)
