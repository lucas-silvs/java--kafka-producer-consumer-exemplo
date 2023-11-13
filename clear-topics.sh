#!/bin/bash

# Lista dos tópicos para limpar
TOPICS=("topico1" "topico2" "topico3")

# Broker Kafka
BROKER="localhost:9092"

# Tempo de retenção temporário em milissegundos (1000 = 1 segundo)
TEMP_RETENTION_TIME=1000

# Altera o tempo de retenção dos tópicos para forçar a limpeza
for TOPIC in "${TOPICS[@]}"
do
    echo "Definindo retenção curta para o tópico $TOPIC..."
    kafka-configs.sh --bootstrap-server $BROKER --entity-type topics --entity-name $TOPIC --alter --add-config retention.ms=$TEMP_RETENTION_TIME
done

# Aguarde um pouco para a retenção ser aplicada e as mensagens serem excluídas
echo "Aguardando a limpeza dos tópicos..."
sleep 60

# Restaura o tempo de retenção original
for TOPIC in "${TOPICS[@]}"
do
    echo "Restaurando o tempo de retenção original para o tópico $TOPIC..."
    kafka-configs.sh --bootstrap-server $BROKER --entity-type topics --entity-name $TOPIC --alter --delete-config retention.ms
done

echo "Limpeza concluída."
