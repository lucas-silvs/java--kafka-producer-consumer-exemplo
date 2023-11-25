# java--kafka-producer-consumer-exemplo

Projeto desenvolvido utilizando Java 17 e Spring boot 3 utilizando Kafka com um producer e um consumer seguindo o modelo de Arquitetura Orientada a Evento

## Requisitos

- Java 17
- Spring 3
- Gradle 7.6
- Kafka 2.13
- Docker

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

Também é possivel executar o Kafka com o arquivo Docker disponibilizado no repositório na pasta "docker". Para subir o Kafka utilizando o Docker, deve executar o comando abaixo:

```
docker-compose up
```


## Comandos Kafka CLI

### Reasing topic para outro broker kafka

Abaixo:
```
kafka-reassign-partitions.sh --zookeeper <YOUR_ZOOKEEPER> --verify --reassignment-json-file <YOUR_JSON_FILE> 2>&1 | grep "Leader: -1" | awk '{print $2}' | sort | uniq | awk 'BEGIN{printf "{\"topics\": [\n"} {printf "    {\n        \"topic\":  \"%s\"\n    },\n", $1} END{print "]}\n"}' | sed '$s/,$//'
```

```
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

## Referencias

- [Docker](https://www.docker.com/)
- [Kafka](https://kafka.apache.org/)
- [Spring](https://spring.io/projects/spring-boot)
- [Confluent](https://docs.confluent.io/5.5.1/quickstart/ce-docker-quickstart.html)
