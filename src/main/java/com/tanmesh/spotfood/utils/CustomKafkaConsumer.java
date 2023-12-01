package com.tanmesh.spotfood.utils;

import com.tanmesh.spotfood.configuration.KafkaConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;


public class CustomKafkaConsumer {
  private KafkaConsumer<String, String> _consumer;
  private String topicName;

  public CustomKafkaConsumer(String topicName, KafkaConsumerConfig kafkaConsumerConfig) {
    this.topicName = topicName;

//    Properties properties = new Properties();
//    properties.put("bootstrap.servers", kafkaConsumerConfig.getBootstrapServers());
//    properties.put("key.deserializer", kafkaConsumerConfig.getKeyDeserializer());
//    properties.put("value.deserializer", kafkaConsumerConfig.getValueDeserializer());
//    properties.put("group.id", kafkaConsumerConfig.getGroupId());
//
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "localhost:9092");
    properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    properties.put("group.id", "consumer-group");

    this._consumer = new KafkaConsumer<>(properties);
    this._consumer.subscribe(Collections.singleton(topicName));
  }
  
  public ConsumerRecords<String, String> poll() {
    while (_consumer.assignment().isEmpty()) {
      _consumer.poll(Duration.ofMillis(100));
    }
    _consumer.seekToBeginning(_consumer.assignment());
    return _consumer.poll(Duration.ofMillis(1000));
  }

  public void close() {
    try {
      _consumer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
