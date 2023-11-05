#!/bin/bash

OUTPUT=$(kafka-topics.sh --describe --unavailable-partitions | grep 'Topic:' | awk '{print $2}')

# Começar com a estrutura inicial do JSON
echo "{" > output.json
echo '    "topics": [' >> output.json

# Loop através dos tópicos e adicionar ao JSON
FIRST=true
for TOPIC in $OUTPUT; do
    if [ "$FIRST" = true ]; then
        FIRST=false
    else
        # Se não for o primeiro tópico, adicione uma vírgula para separar
        echo ',' >> output.json
    fi
    echo '        {' >> output.json
    echo "            \"topic\":  \"$TOPIC\"" >> output.json
    echo '        }' >> output.json
done

# Fechar a estrutura JSON
echo '    ]' >> output.json
echo "}" >> output.json
