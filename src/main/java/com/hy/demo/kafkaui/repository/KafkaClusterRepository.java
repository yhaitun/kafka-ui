package com.hy.demo.kafkaui.repository;

import com.hy.demo.kafkaui.model.KafkaCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KafkaClusterRepository extends JpaRepository<KafkaCluster, Long> {
    List<KafkaCluster> findByActiveTrue();
    Optional<KafkaCluster> findByName(String name);
    boolean existsByName(String name);
}