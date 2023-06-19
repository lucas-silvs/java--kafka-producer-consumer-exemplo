package com.lucassilvs.libkafkaclients.lib.authentication;

import jakarta.annotation.Nonnull;
import lombok.Data;

import java.util.Map;

@Data
public class KafkaAuthProperties {

    @Nonnull
    private String saslMechanism;

    @Nonnull
    private String saslJaasConfig;

//     ---------- OAUTHBEARER ----------

    private String saslTokenEndpointUrl;
    private String authClientId;
    private String authClientSecret;
    private String scope;
    private Map<String, String> extensions;
    private String saslLoginCallbackHandlerClass;

//    ------------------------------------

//     ---------- PLAIN ----------

    private String username;
    private String password;

//    ------------------------------------

}
