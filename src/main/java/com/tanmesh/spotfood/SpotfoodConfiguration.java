package com.tanmesh.spotfood;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanmesh.spotfood.configuration.AwsConfig;
import com.tanmesh.spotfood.configuration.KafkaConsumerConfig;
import com.tanmesh.spotfood.configuration.KafkaProducerConfig;
import com.tanmesh.spotfood.configuration.MongoDBConfig;
import io.dropwizard.Configuration;

public class SpotfoodConfiguration extends Configuration {
    @JsonProperty
    private MongoDBConfig dbConfig;

    @JsonProperty
    private AwsConfig awsConfig;

    @JsonProperty
    private KafkaProducerConfig kafkaProducerConfig;

    @JsonProperty
    private KafkaConsumerConfig kafkaConsumerConfig;

    public MongoDBConfig getDbConfig() {
        return dbConfig;
    }

    public AwsConfig getAwsConfig() { return awsConfig; }

    public KafkaConsumerConfig getKafkaConsumerConfig() { return kafkaConsumerConfig; }

    public KafkaProducerConfig getKafkaProducerConfig() { return kafkaProducerConfig; }
}
