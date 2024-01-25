# java--kafka-producer-consumer-exemplo

Projeto desenvolvido utilizando Java 17 e Spring boot 3 utilizando Kafka com um producer e um consumer seguindo o modelo de Arquitetura Orientada a Evento

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
- [Referencias](#referencias)

## Requisitos

- Java 17
- Spring 3.2.2
- Gradle 7.6
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


## Mirror Maker 2

É possivel usar o Mirror Maker para replicar a mensagem de tópicos Kafka entre clusters diferentes, para isso será necessário executar o  seguinte comando abaixo:

``` sh
./kafka-mirror-maker.sh --consumer.config ../../cluster-source.properties --producer.config ../../cluster-target.properties --whitelist "<nome-do-tópico"
```


## Kafka Confluent Replicator
Também é possivel utilizar o Kafka Confluent Replicator para replicar a mensagem de tópicos Kafka entre clusters diferentes, para isso será necessário executar o  seguinte comando abaixo:

``` sh
curl --location 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data '{
  "name": "replicator-teste",
  "config": {
    "connector.class": "io.confluent.connect.replicator.ReplicatorSourceConnector",
    "key.converter": "org.apache.kafka.connect.storage.StringConverter",
    "value.converter": "org.apache.kafka.connect.storage.StringConverter",
    "src.kafka.bootstrap.servers": "kafka2:9092",
    "dest.kafka.bootstrap.servers": "kafka3:9092",
    "topic.whitelist": "<nome-do-topico>",
    "confluent.topic.replication.factor": 1,
    "provenance.header.enable": true
  }
}
'
```

Com isso será criado o conector no servidor do kafka connect que está no Docker.


## Uso de multiplos consumers e producers

Para utilizar diversos consumers e producer, será necessário instancias os beans
para cada um e os factories para cada um, conforme o exemplo abaixo:

### Producer
ProducerConfig:
``` java
    private Map<String, Object> producerConfigs(KafkaProperties kafkaProperties, String username, String password, String clientId) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties();

        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(PLAIN_JAAS_CONFIG, username, password));

        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        return props;
    }

    private ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties, String username, String password, String clientId) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(kafkaProperties, username, password, clientId));
    }


    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(final KafkaProperties kafkaProperties) {
        ProducerFactory<String, String> producerFactory = producerFactory(kafkaProperties, "producer", "producer-secret", "producer1");

        producerFactory.createProducer();
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateTopico2(final KafkaProperties kafkaProperties) {
        ProducerFactory<String, String> producerFactory = producerFactory(kafkaProperties, "producer2", "teste", "producer2");

        producerFactory.createProducer();
        return new KafkaTemplate<>(producerFactory);
    }
    
```
Injetando cada producer:

```java
// omitindo nome da classe
    private final KafkaTemplate kafkaTemplate;

    private final KafkaTemplate kafkaTemplateTopico2;

    public KafkaPostUtils(KafkaTemplate kafkaTemplate, KafkaTemplate kafkaTemplateTopico2) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateTopico2 = kafkaTemplateTopico2;
    }

    public void postarMensagem(Object mensagem, String nomeTopico){
        logger.info("Postando mensagem: " + mensagem + " no tópico " + nomeTopico);
        kafkaTemplate.send(nomeTopico, mensagem);

        kafkaTemplateTopico2.send("topico-teste-2", mensagem);

        logger.info("mensagem enviada com sucesso");
    }
```

Para o producer, o nome dos campos de classe KafkaTemplate deve ser o mesmo que cada
bean criado na classe ProducerConfig.

### Consumer

ConsumerConfig:

```java
    private ConsumerFactory<String, String> consumerFactory(KafkaProperties kafkaProperties, String username, String password, String groupId, String clientId){
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties();
        properties.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(PLAIN_JAAS_CONFIG, username, password));

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(CommonClientConfigs.CLIENT_ID_CONFIG, clientId);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> consumer1(KafkaProperties kafkaProperties){
        ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(kafkaProperties, "consumer", "consumer-secret", "grupo-teste", "consumer1"));

        //Adicionando retry para caso de erro de autenticação com o broker (GroupAuthorizationException)
        factory.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofSeconds(7));

        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String,String>> consumer2(KafkaProperties kafkaProperties){
        ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(kafkaProperties, "consumer2", "teste", "grupo-teste-2", "consumer2"));

        //Adicionando retry para caso de erro de autenticação com o broker (GroupAuthorizationException)
        factory.getContainerProperties().setAuthExceptionRetryInterval(Duration.ofSeconds(7));

        return factory;
    }
```

Injetando cada consumer:

```java
    @KafkaListener(topics = "topico-teste", containerFactory = "consumer1")
    public void consumindoMensagemSimples(String mensagem) {
        logger.info("Recebendo mensagem topico 1:  {0}", mensagem);
    }

    @KafkaListener(topics = "topico-teste-2", containerFactory = "consumer2")
    public void consumindoMensagemTopico2(String mensagem) {
        logger.info("Recebendo mensagem topico 2: {0}", mensagem);
    }
```
Para injetar cada consumer, é informado o nome do bean de cada consumer no campo "containerFactory" do @KafkaListener.


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

Primeiro, é necessário instalar o KEDA no cluster Kubernetes, para isso, execute o comando abaixo:

```sh
# Via Microk8s
microk8s enable keda

# Via Helm
helm repo add kedacore https://kedacore.github.io/charts
helm repo update
helm install keda kedacore/keda --namespace keda --create-namespace
```

após aplicar, ao realizar o deploy da aplicação, será aplicado junto o Trigger do KEDA, que irá monitorar o lag do consumer e irá escalar
baseado no lag.
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
