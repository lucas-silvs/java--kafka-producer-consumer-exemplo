#!/bin/bash

# Define o arquivo com as ACLs e o endereço do broker de destino
ACL_FILE="list-acls-examples.txt"
DEST_BROKER="dest-broker:9092"
BATCH_SIZE=10

# Regex para extrair informações da ACL
regex="principal=User:([^,]+), host=([^,]+), operation=([^,]+), permissionType=([^,]+)"

# Contador para o lote atual
current_batch=0

# Lê cada linha do arquivo ACL
while IFS= read -r line
do
  if [[ $line =~ Current\ ACLs\ for\ resource ]]; then
    # Extrai o nome do recurso
    current_resource=$(echo "$line" | grep -oP 'name=\K[^,]+')
  elif [[ $line =~ $regex ]]; then
    # Extrai informações da ACL usando regex
    principal="User:${BASH_REMATCH[1]}"
    host="${BASH_REMATCH[2]}"
    operation="${BASH_REMATCH[3]}"
    permission_type="${BASH_REMATCH[4]}"

    # Executa o comando Kafka de forma assíncrona
    kafka-acls.sh --bootstrap-server $DEST_BROKER --command-config /path/to/client.properties --add --allow-principal "$principal" --operation $operation --permission-type $permission_type --topic "$current_resource" --host $host &

    # Incrementa o contador do lote
    ((current_batch++))

    # Se o lote atingir o tamanho definido, espera a conclusão
    if [[ $current_batch -eq $BATCH_SIZE ]]; then
      wait
      current_batch=0
    fi
  fi
done < "$ACL_FILE"

# Espera por qualquer comando ainda em execução
wait

echo "Todas as ACLs foram aplicadas."
