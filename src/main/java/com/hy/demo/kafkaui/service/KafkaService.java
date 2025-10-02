package com.hy.demo.kafkaui.service;

import com.hy.demo.kafkaui.model.KafkaCluster;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class KafkaService {
    
    private Properties getKafkaProperties(String bootstrapServers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("group.id", "kafka-ui-group");
        props.put("auto.offset.reset", "earliest");
        return props;
    }
    
    private Properties getKafkaProperties() {
        return getKafkaProperties("localhost:9092");
    }
    
    public List<String> getTopics() {
        return getTopics("localhost:9092");
    }
    
    public List<String> getTopics(String bootstrapServers) {
        try (AdminClient adminClient = AdminClient.create(getKafkaProperties(bootstrapServers))) {
            ListTopicsResult topicsResult = adminClient.listTopics();
            Set<String> topics = topicsResult.names().get();
            return new ArrayList<>(topics);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public List<String> searchTopics(String bootstrapServers, String keyword) {
        List<String> allTopics = getTopics(bootstrapServers);
        if (keyword == null || keyword.trim().isEmpty()) {
            return allTopics;
        }
        
        String searchTerm = keyword.toLowerCase();
        return allTopics.stream()
                .filter(topic -> topic.toLowerCase().contains(searchTerm))
                .sorted()
                .collect(Collectors.toList());
    }
    
    public boolean createTopic(String topicName, int partitions, short replicationFactor) {
        return createTopic("localhost:9092", topicName, partitions, replicationFactor);
    }
    
    public boolean createTopic(String bootstrapServers, String topicName, int partitions, short replicationFactor) {
        try (AdminClient adminClient = AdminClient.create(getKafkaProperties(bootstrapServers))) {
            NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean sendMessage(String topic, String key, String message) {
        return sendMessage("localhost:9092", topic, key, message);
    }
    
    public boolean sendMessage(String bootstrapServers, String topic, String key, String message) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(getKafkaProperties(bootstrapServers))) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
            producer.send(record).get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> consumeMessages(String topic, int maxMessages) {
        return consumeMessages("localhost:9092", topic, maxMessages);
    }
    
    public List<String> consumeMessages(String bootstrapServers, String topic, int maxMessages) {
        List<String> messages = new ArrayList<>();
        Properties props = getKafkaProperties(bootstrapServers);
        props.put("enable.auto.commit", "false");
        
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(topic));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            
            records.forEach(record -> {
                if (messages.size() < maxMessages) {
                    messages.add(String.format("Key: %s, Value: %s, Partition: %d, Offset: %d", 
                            record.key(), record.value(), record.partition(), record.offset()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}