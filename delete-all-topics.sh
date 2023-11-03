#!/bin/bash

# Defina seu broker de bootstrap Kafka aqui
BOOTSTRAP_SERVER='localhost:9092'

# Lista de tópicos internos que não devem ser excluídos
INTERNAL_TOPICS="__consumer_offsets __transaction_state _schemas"

# Obtendo a lista de tópicos
topics=$(kafka-topics.sh --bootstrap-server $BOOTSTRAP_SERVER --list 2>/dev/null)

# Loop para excluir tópicos que não são internos
for topic in $topics
do
    if [[ ! $INTERNAL_TOPICS =~ $topic ]]; then
        echo "Excluindo tópico: $topic"
        kafka-topics.sh --bootstrap-server $BOOTSTRAP_SERVER --delete --topic "$topic" 2>/dev/null
        if [ $? -eq 0 ]; then
            echo "Tópico excluído: $topic"
        else
            echo "Falha ao excluir tópico: $topic"
        fi
    else
        echo "Ignorando tópico interno: $topic"
    fi
done

echo "Operação de exclusão concluída."
