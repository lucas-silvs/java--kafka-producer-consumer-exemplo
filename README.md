# java--kafka-producer-consumer-exemplo

Projeto desenvolvido utilizando Java 17 e Spring boot 3 utilizando Kafka com um producer e um consumer seguindo o modelo de Arquitetura Orientada a Evento

## Requisitos

- Java 17
- Spring 3
- Gradle 7.6
- Kafka 2.13

## Criar tópico Kafka

Antes de executar o projeto Spring, é necessário criar o tópico Kafka. Para criar o tópico kafka, é necessário executar o comando abaixo na pasta do Kafka:

```
./bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic topico-teste --create --partitions 3 --replication-factor 1
```
