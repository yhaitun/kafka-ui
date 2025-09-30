package com.hy.demo.kafkaui.service;

import com.hy.demo.kafkaui.model.KafkaCluster;
import com.hy.demo.kafkaui.repository.KafkaClusterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ClusterService {
    
    @Autowired
    private KafkaClusterRepository clusterRepository;
    
    public List<KafkaCluster> getAllClusters() {
        return clusterRepository.findAll();
    }
    
    public List<KafkaCluster> getActiveClusters() {
        return clusterRepository.findByActiveTrue();
    }
    
    public Optional<KafkaCluster> getClusterById(Long id) {
        return clusterRepository.findById(id);
    }
    
    public KafkaCluster createCluster(KafkaCluster cluster) {
        // 检查是否已存在同名集群
        if (clusterRepository.existsByName(cluster.getName())) {
            throw new RuntimeException("集群名称已存在: " + cluster.getName());
        }
        
        // 设置创建和更新时间
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        cluster.setCreatedAt(now);
        cluster.setUpdatedAt(now);
        
        return clusterRepository.save(cluster);
    }
    
    public KafkaCluster updateCluster(Long id, KafkaCluster updatedCluster) {
        Optional<KafkaCluster> existingClusterOpt = clusterRepository.findById(id);
        if (existingClusterOpt.isPresent()) {
            KafkaCluster existingCluster = existingClusterOpt.get();
            
            // 检查名称是否被其他集群使用
            if (!existingCluster.getName().equals(updatedCluster.getName()) && 
                clusterRepository.existsByName(updatedCluster.getName())) {
                throw new RuntimeException("集群名称已存在: " + updatedCluster.getName());
            }
            
            // 更新集群信息
            existingCluster.setName(updatedCluster.getName());
            existingCluster.setBootstrapServers(updatedCluster.getBootstrapServers());
            existingCluster.setDescription(updatedCluster.getDescription());
            existingCluster.setActive(updatedCluster.isActive());
            
            // 更新时间
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            existingCluster.setUpdatedAt(now);
            
            return clusterRepository.save(existingCluster);
        } else {
            throw new RuntimeException("未找到ID为 " + id + " 的集群");
        }
    }
    
    public void deleteCluster(Long id) {
        if (clusterRepository.existsById(id)) {
            clusterRepository.deleteById(id);
        } else {
            throw new RuntimeException("未找到ID为 " + id + " 的集群");
        }
    }
    
    public boolean activateCluster(Long id) {
        Optional<KafkaCluster> clusterOpt = clusterRepository.findById(id);
        if (clusterOpt.isPresent()) {
            KafkaCluster cluster = clusterOpt.get();
            cluster.setActive(true);
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            cluster.setUpdatedAt(now);
            clusterRepository.save(cluster);
            return true;
        }
        return false;
    }
    
    public boolean deactivateCluster(Long id) {
        Optional<KafkaCluster> clusterOpt = clusterRepository.findById(id);
        if (clusterOpt.isPresent()) {
            KafkaCluster cluster = clusterOpt.get();
            cluster.setActive(false);
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            cluster.setUpdatedAt(now);
            clusterRepository.save(cluster);
            return true;
        }
        return false;
    }
}