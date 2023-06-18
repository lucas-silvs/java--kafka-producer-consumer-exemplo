package com.lucassilvs.libkafkaclients.clients.schema_registry;


import lombok.Data;

@Data
public class SchemaRegistryConfiguration {

    private String url;
    private boolean basicAuth;
    private String username;
    private String password;

}
