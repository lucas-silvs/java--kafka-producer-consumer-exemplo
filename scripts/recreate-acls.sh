#!/bin/bash

# Define o arquivo com as ACLs e o endereço do broker de destino
ACL_FILE="list-acls-examples.txt"
DEST_BROKER="dest-broker:9092"

# Lê cada linha do arquivo ACL
while IFS= read -r line
do
  # Checa se a linha é o início de um novo recurso
  if echo "$line" | grep -q "Current ACLs for resource"; then
    current_resource=$(echo "$line" | grep -oE 'name=[^,]+' | sed 's/name=//')
  elif echo "$line" | grep -q "principal=User:"; then
    # Processa a ACL para o recurso atual
    principal=$(echo "$line" | grep -oE 'principal=User:[^,]+')
    host=$(echo "$line" | grep -oE 'host=[^,]+')
    operation=$(echo "$line" | grep -oE 'operation=[^,]+')
    permission_type=$(echo "$line" | grep -oE 'permissionType=[^,]+')

    echo "ACL para o recurso $current_resource encontrada: $principal $host $operation $permission_type"

    # Constrói e executa o comando para adicionar a ACL no cluster de destino
    # kafka-acls.sh --bootstrap-server $DEST_BROKER --command-config /path/to/client.properties --add --allow-principal "$principal" --operation ${operation#operation=} --permission-type ${permission_type#permissionType=} --topic "$current_resource" --host ${host#host=}
  fi
done < "$ACL_FILE"