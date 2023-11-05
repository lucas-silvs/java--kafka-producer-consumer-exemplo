#!/bin/bash

# Defina as variáveis de ambiente ou substitua pelos seus valores de servidor.
SOURCE_BROKER="<source-bootstrap-servers>"
DEST_BROKER="<destination-bootstrap-servers>"
ACL_OUTPUT_FILE="acls-to-apply.sh"

# Certifique-se de que o kafka-acls.sh está no seu PATH ou forneça o caminho completo para ele.
KAFKA_ACLS_PATH="kafka-acls.sh"

# Obtém as ACLs do cluster de origem e salva em um arquivo.
$KAFKA_ACLS_PATH --bootstrap-server $SOURCE_BROKER --list > source_acls.txt

# Inicia o arquivo de saída com o shebang do bash.
echo "#!/bin/bash" > $ACL_OUTPUT_FILE

# Processa cada linha da saída do comando list.
while IFS= read -r line
do
    # Filtra linhas que contêm informações de ACL e as transforma em comandos de adição de ACL.
    if echo "$line" | grep -q "ALLOWED"; then
        # Transforma a linha de ACL numa linha de comando kafka-acls para adicionar a ACL.
        # Este é um exemplo e pode precisar de ajustes conforme a saída do seu comando kafka-acls.
        acl_command=$(echo "$line" | awk '{print "--add --allow-principal User:\"" $2 "\" --operation " $4 " --topic " $6}')
        
        # Adiciona o comando de adição de ACL ao arquivo de saída.
        echo "$KAFKA_ACLS_PATH --bootstrap-server $DEST_BROKER $acl_command" >> $ACL_OUTPUT_FILE
    fi
done < "source_acls.txt"

# Torna o script de saída executável.
chmod +x $ACL_OUTPUT_FILE

echo "Comandos de ACL escritos em $ACL_OUTPUT_FILE. Revise antes de executar no destino."
