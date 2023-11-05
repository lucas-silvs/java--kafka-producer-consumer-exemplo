#!/bin/bash

# Defina o caminho para o arquivo com a saída do describe
input_file="topicos.txt"

# Defina o endereço do seu broker Kafka
bootstrap_servers="localhost:9092"

# Lê o arquivo linha por linha
while IFS= read -r line
do
    # Procura por linhas que começam com "Topic:" e que contêm "PartitionCount:"
    if [[ $line == Topic:* && $line == *PartitionCount:* ]]; then
        # Extrai o nome do tópico
        topic_name=$(echo $line | grep -oP 'Topic: \K\S+')
        # Extrai a contagem de partições
        partition_count=$(echo $line | grep -oP 'PartitionCount: \K\d+')
        # Extrai o fator de replicação
        replication_factor=$(echo $line | grep -oP 'ReplicationFactor: \K\d+')
        # Extrai as configurações, removendo espaços e quebrando-as em um array
        config_string=$(echo $line | grep -oP 'Configs: \K.*' | sed 's/, /,/g')
        IFS=',' read -r -a config_array <<< "$config_string"
        
        # Cria o comando básico de criação do tópico
        COMMAND="./kafka-topics.sh --create --bootstrap-server $bootstrap_servers --replication-factor $replication_factor --partitions $partition_count --topic $topic_name"

        # Adiciona as configurações ao comando se elas existirem
        for config in "${config_array[@]}"
        do
            if [ -n "$config" ]; then
                COMMAND+=" --config $config"
            fi
        done

        # Executa o comando
        echo "Executando comando: $COMMAND"
        eval $COMMAND

        if [ $? -eq 0 ]; then
            echo "Tópico $topic_name criado com sucesso."
        else
            echo "Erro ao criar tópico $topic_name."
        fi
    fi
done < "$input_file"
