package com.lucassilvs.libteste.lib.properties.schema_registry;

import lombok.Data;

@Data
public class SchemaRegistryConfiguration {

    private String url;
    private String username;
    private String password;

}
