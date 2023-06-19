package com.lucassilvs.libteste.lib.producer;

import com.lucassilvs.libteste.lib.KafkaCommonProperties;
import lombok.Data;
@Data
public class ProducerCommonProperties extends KafkaCommonProperties {

    private String ClientDnsLookup; //OK
    private String metadataMaxAgeMs; //OK
    private String metadataMaxIdleMs; //OK
    private String batchSize; // OK
    private String partitionerAdaptivePartitioningEnable; //OK
    private String partitionerAvailabilityTimeoutMs; //OK
    private String partitionerIgnoreKeys; //OK
    private String acks; //OK
    private String lingerMs; //OK
    private String requestTimeoutMs; //ok
    private String deliveryTimeoutMs; //OK
    private String sendBufferBytes; //OK
    private String receiveBufferBytes; //OK
    private String maxRequestSize; //OK
    private String reconnectBackoffMs; //OK
    private String reconnectBackoffMaxMs; //OK
    private String maxBlockMs; //OK
    private String bufferMemory; //OK
    private String retryBackoffMs; //OK
    private String compressionType; //OK
    private String metricsSampleWindowMs; //OK
    private String metricsNumSamples; //OK
    private String metricsRecordingLevel; //OK
    private String metricReporters; //OK
    private String autoIncludeJmxReporter; //OK
    private String maxInFlightRequestsPerConnection; //OK
    private String retries; //OK
    private String keySerializer; //OK
    private String valueSerializer; //OK
    private String socketConnectionSetupTimeoutMs; //OK
    private String socketConnectionSetupTimeoutMaxMs; //OK
    private String connectionsMaxIdleMs; //OK
    private String partitionerClass; //OK
    private String interceptorClasses; //OK
    private String enableIdempotence; //OK
    private String transactionTimeoutMs; //OK
    private String transactionalId; //OK
    private String securityProviders; //OK
}
