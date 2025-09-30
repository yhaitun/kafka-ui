package com.hy.demo.kafkaui.model;

import javax.persistence.*;

@Entity
@Table(name = "kafka_clusters")
public class KafkaCluster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "cluster_name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "bootstrap_servers", nullable = false)
    private String bootstrapServers;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    @Column(name = "created_at")
    private String createdAt;
    
    @Column(name = "updated_at")
    private String updatedAt;
    
    public KafkaCluster() {}
    
    public KafkaCluster(String name, String bootstrapServers, String description) {
        this.name = name;
        this.bootstrapServers = bootstrapServers;
        this.description = description;
        this.active = true;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getBootstrapServers() { return bootstrapServers; }
    public void setBootstrapServers(String bootstrapServers) { this.bootstrapServers = bootstrapServers; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}