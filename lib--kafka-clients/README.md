# Lib Kafka Clients
Foi desenvolvida uma lib para a criação dinamica de producers e consumers Kafka com Spring Boot,
onde obtendo as propriedades para a criação dos producers e consumers via properties, e instanciados
dinamicamente.

## Tecnologias
- Java 17
- Spring Boot 3
- Gradle 7.6


## Utilização da lib

Para utilizar a lib em um projeto, deve adicionar a dependência no projeto, e configurar as propriedades, e instanciar o bean `KafkaClientsManager` no projeto.
Abaixo está detalhado cada etapa.

### Configurações Kafka Application.yaml
para utilizar a lib, é necessário definir as propriedades dos consumers e producers no arquivo application.yaml,
onde é informado uma lista de producers e consumers, que serão criados dinamicamente. Apenas alguns campos são obrigatórios,
sendo eles o nome do producer/consumer, o nome do tópico, bootstrap servers e a classe de serialização/deserialização.
Abaixo está um exemplo dessas propriedades:

```yaml
kafka:
#  Exemplo de configuração de producer
  producers:
    producer1:
      nomeTopico: topico--teste
      keySerializer: org.apache.kafka.common.serialization.StringSerializer
      valueSerializer: org.apache.kafka.common.serialization.StringSerializer
      bootstrapServers: localhost:9092
      requestTimeoutMs: 10000
      clientId: teste
    producer2:
      nomeTopico: topico-com-schema
      keySerializer: org.apache.kafka.common.serialization.StringSerializer
      valueSerializer: io.confluent.kafka.serializers.KafkaAvroSerializer
      bootstrapServers: localhost:9092
      clientId: teste2
      schemaRegistry:
        url: http://localhost:8081

#  Exemplo de configuração de consumer
  consumers:
    consumer1:
      nomeTopico: topico--teste
      keyDeserializer: org.apache.kafka.common.serialization.StringDeserializer
      valueDeserializer: org.apache.kafka.common.serialization.StringDeserializer
      bootstrapServers: localhost:9092
      groupId: consumer1-teste
      clientId: teste
    consumer2:
      nomeTopico: topico--com-schema
      keyDeserializer: org.apache.kafka.common.serialization.StringDeserializer
      valueDeserializer: io.confluent.kafka.serializers.KafkaAvroDeserializer
      bootstrapServers: localhost:9092
      groupId: consumer2-teste
      clientId: teste
      schemaRegistry:
        url: http://localhost:8081
```

### Configuração do Bean KafkaClientsManager
Ao configurar as propriedades e adicionar a lib nas dependências do projeto, deve utilizar o bean `KafkaClientsManager` para criar os producers e consumers.
Abaixo está um exemplo de criação de producers e consumers com o Bean:


Producer:
```java
public class MessagemServiceLibAvroImpl implements MensagemService {


    private KafkaTemplate<String, UsuarioTesteAvro> kafkaTemplateProducerTeste;

    @Autowired
    public MessagemServiceLibAvroImpl(KafkaClientsManager kafkaClientsManager) {

        this.kafkaTemplateProducerTeste = kafkaClientsManager.buscaProducer("producer2");
    }
}
```

Consumer:
```java
public class ConsumersBeansComponent {

    private final KafkaClientsManager kafkaClientsManager;

    @Autowired
    public ConsumersBeansComponent(KafkaClientsManager kafkaClientsManager) {
        this.kafkaClientsManager = kafkaClientsManager;
    }

    @Bean("containerFactoryConsumer1")
    public ConcurrentKafkaListenerContainerFactory criaBeanConsumer1() {
        return kafkaClientsManager.buscaConsumer("consumer1");
    }

    @Bean("containerFactoryConsumer2")
    public ConcurrentKafkaListenerContainerFactory criaBeanConsumer2() {
        return kafkaClientsManager.buscaConsumer("consumer2");
    }


    //    ------------------- utilizando os Beans definidos na classe ConsumerBeansComponent
    public class ConsumerListenerAvro {

        @KafkaListener(
                topics = "${kafka.consumers.consumer2.nomeTopico}",
                containerFactory = "containerFactoryConsumer2",
                groupId = "${kafka.consumers.consumer2.groupId}"
        )
        public void consumirMensagem1(ConsumerRecord<String, Object> mensagem) {
            Object mensagemConsumida = mensagem.value();
            System.out.println("CONSUMER AVRO -------- Mensagem consumida: " + mensagemConsumida);
            
        }
    }
}



```

## Configuração para teste local
Para testar localmente, deve realizar o build do projeto, e adicionar o jar da seguinte forma no projeto que irá utilizar a lib:

Gradle:
```groovy
    implementation files('<caminho-do-proketo>/lib--kafka-clients/build/libs/lib--kafka-clients-0.0.1-SNAPSHOT.jar')  // Caminho para o arquivo JAR da lib
```

Maven:
```xml
    <dependency>
        <groupId>com.github.lucassilvs</groupId>
        <artifactId>lib--kafka-clients</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <scope>system</scope>
        <systemPath>${project.basedir}/lib--kafka-clients/build/libs/lib--kafka-clients-0.0.1-SNAPSHOT.jar</systemPath>
    </dependency>
```

## Referencias
- [Spring Kafka](https://docs.spring.io/spring-kafka/docs/current/reference/html/#reference)
- [Confluent Schema Registry](https://docs.confluent.io/platform/current/schema-registry/index.html)
- [Confluent Schema Registry authentication](https://docs.confluent.io/platform/current/schema-registry/security/index.html)
- [Confluent Avro Serializer](https://docs.confluent.io/platform/current/schema-registry/serdes-develop/index.html#serdes-develop)
- [Confluent Oauthbearer Authentication](https://docs.confluent.io/platform/current/kafka/authentication_sasl/authentication_sasl_oauth.html)
- [Confluent PLAIN Authentication](https://docs.confluent.io/platform/current/kafka/authentication_sasl/authentication_sasl_plain.html)
- [Confluent SCRAM Authentication](https://docs.confluent.io/platform/current/kafka/authentication_sasl/authentication_sasl_scram.html)