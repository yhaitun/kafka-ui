package com.hy.demo.kafkaui.model;

import java.time.LocalDateTime;

public class KafkaMessage {
    private long offset;
    private LocalDateTime timestamp;
    private String key;
    private String value;
    private int partition;
    
    public KafkaMessage(long offset, LocalDateTime timestamp, String key, String value, int partition) {
        this.offset = offset;
        this.timestamp = timestamp;
        this.key = key;
        this.value = value;
        this.partition = partition;
    }
    
    // Getters and Setters
    public long getOffset() { return offset; }
    public void setOffset(long offset) { this.offset = offset; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public int getPartition() { return partition; }
    public void setPartition(int partition) { this.partition = partition; }
    
    @Override
    public String toString() {
        return String.format("Partition: %d, Offset: %d, Key: %s, Value: %s", 
                           partition, offset, key, value);
    }
}