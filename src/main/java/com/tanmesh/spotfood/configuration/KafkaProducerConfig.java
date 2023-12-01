package com.tanmesh.spotfood.configuration;

public class KafkaProducerConfig {
    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;

    private String topicName;

    public KafkaProducerConfig() {
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }
}
