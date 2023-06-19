package com.lucassilvs.libkafkaclients.properties.consumer;

import com.lucassilvs.libkafkaclients.properties.KafkaCommonProperties;
import lombok.Data;
@Data
public class ConsumerCommonProperties extends KafkaCommonProperties {

    private String groupId; //OK
    private String groupInstanceId; //OK
    private String maxPollRecords; //OK
    private String maxPollIntervalMs; //OK
    private String sessionTimeoutMs; //OK
    private String heartbeatIntervalMs; //OK
    private String ClientDnsLookup; //OK
    private boolean enableAutoCommit; //OK
    private String autoCommitIntervalMs; //OK
    private String partitionAssignmentStrategy; //OK
    private String autoOffsetReset; //OK
    private String fetchMinBytes; //OK
    private String fetchMaxBytes; //OK
    private String fetchMaxWaitMs; //OK
    private String metadataMaxAgeMs; //OK
    private String maxPartitionFetchBytes; //OK
    private String sendBufferBytes; //OK
    private String receiveBufferBytes; //OK
    private String clientRack; //OK
    private String reconnectBackoffMs; //OK
    private String reconnectBackoffMaxMs; //OK
    private String retryBackoffMs; //OK
    private String metricsSampleWindowMs; //OK
    private String metricsNumSamples; //OK
    private String metricsRecordingLevel; //OK
    private String metricReporters; //OK
    private String checkCrcs; //OK
    private String keyDeserializer; //OK
    private String valueDeserializer; //OK
    private String socketConnectionSetupTimeoutMs; //OK
    private String socketConnectionSetupTimeoutMaxMs; //OK
    private String connectionsMaxIdleMs; //OK
    private String requestTimeoutMs; //OK
    private String defaultApiTimeoutMs; //OK
    private String interceptorClasses; //OK
    private String excludeInternalTopics; //OK
    private String internalLeaveGroupOnClose; //OK
    private String internalThrowOnFetchStableOffsetUnsupported; //OK
    private String isolationLevel; //OK
    private String allowAutoCreateTopics; //OK
    private String securityProviders; //OK
}
