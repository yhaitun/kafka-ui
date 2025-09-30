package com.hy.demo.kafkaui.model;

public class TopicInfo {
    private String name;
    private int partitionCount;
    private int replicationFactor;
    private long totalMessages;
    private long totalSize;
    
    public TopicInfo(String name, int partitionCount, int replicationFactor) {
        this.name = name;
        this.partitionCount = partitionCount;
        this.replicationFactor = replicationFactor;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getPartitionCount() { return partitionCount; }
    public void setPartitionCount(int partitionCount) { this.partitionCount = partitionCount; }
    
    public int getReplicationFactor() { return replicationFactor; }
    public void setReplicationFactor(int replicationFactor) { this.replicationFactor = replicationFactor; }
    
    public long getTotalMessages() { return totalMessages; }
    public void setTotalMessages(long totalMessages) { this.totalMessages = totalMessages; }
    
    public long getTotalSize() { return totalSize; }
    public void setTotalSize(long totalSize) { this.totalSize = totalSize; }
}