package com.tanmesh.splatter.configuration;

public class KafkaConsumerConfig {
    private String bootstrapServers;
    private String keyDeserializer;
    private String valueDeserializer;
    private String groupId;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public String getGroupId() {
        return groupId;
    }
}
