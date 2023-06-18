package com.lucassilvs.libkafkaclients.clients;

import com.lucassilvs.libkafkaclients.clients.authentication.KafkaAuthProperties;
import com.lucassilvs.libkafkaclients.clients.schema_registry.SchemaRegistryConfiguration;
import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class KafkaCommonProperties {

    @Nonnull
    private String nomeTopico; //OK

    @Nonnull
    private String bootstrapServers; //OK

    @Nonnull
    private KafkaAuthProperties auth; //OK

    @Nonnull
    private SchemaRegistryConfiguration schemaRegistry; //OK

    private String clientId; //OK

    private String securityProtocol; // OK
}
