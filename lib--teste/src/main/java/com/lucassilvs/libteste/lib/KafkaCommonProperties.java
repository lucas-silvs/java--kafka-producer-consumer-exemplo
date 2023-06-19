package com.lucassilvs.libteste.lib;


import com.lucassilvs.libteste.lib.authentication.KafkaAuthProperties;
import com.lucassilvs.libteste.lib.schema_registry.SchemaRegistryConfiguration;
import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


@Data
public class KafkaCommonProperties {

    @Nonnull
    @Value("${nomeTopico}")
    private String nomeTopico; //OK

    @Nonnull
    @Value("${bootstrapServers}")
    private String bootstrapServers; //OK

    @Value("${auth}")
    private KafkaAuthProperties auth; //OK

    @Value("${schemaRegistry}")
    private SchemaRegistryConfiguration schemaRegistry; //OK

    private String clientId; //OK

    private String securityProtocol; // OK

}
