package com.tanmesh.splatter.utils;

import com.google.gson.Gson;
import com.tanmesh.splatter.configuration.KafkaProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.ws.rs.core.Response;
import java.util.Properties;


public class CustomKafkaProducer {
    private KafkaProducer<String, String> _producer;
    private String topicName;

    private KafkaProducerConfig kafkaProducerConfig;

//    public CustomKafkaProducer(String topicName, KafkaProducerConfig kafkaProducerConfig) {
//        this.topicName = topicName;
//        this.kafkaProducerConfig = kafkaProducerConfig;
//
//        Properties properties = new Properties();
//        properties.put("bootstrap.servers", kafkaProducerConfig.getBootstrapServers());
//        properties.put("key.deserializer", kafkaProducerConfig.getKeySerializer());
//        properties.put("value.deserializer", kafkaProducerConfig.getValueSerializer());
//
//        this._producer = new KafkaProducer<>(properties);
//    }

    public CustomKafkaProducer(String topicName) {
        this.topicName = topicName;

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this._producer = new KafkaProducer<>(properties);
    }
  
    public void send(Response response) {
        Gson gson = new Gson();
        ProducerRecord<String, String> _record = new ProducerRecord<>(topicName, gson.toJson(response.getEntity()));
        _producer.send(_record);
    }

    public void close() {
        try {
            _producer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
