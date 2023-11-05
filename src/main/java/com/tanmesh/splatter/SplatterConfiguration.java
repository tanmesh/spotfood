package com.tanmesh.splatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanmesh.splatter.configuration.AwsConfig;
import com.tanmesh.splatter.configuration.KafkaConsumerConfig;
import com.tanmesh.splatter.configuration.KafkaProducerConfig;
import com.tanmesh.splatter.configuration.MongoDBConfig;
import io.dropwizard.Configuration;

public class SplatterConfiguration extends Configuration {
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
