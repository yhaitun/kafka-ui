CREATE DATABASE IF NOT EXISTS kafka-ui DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE kafka-ui;

CREATE TABLE kafka_clusters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cluster_name VARCHAR(255) NOT NULL UNIQUE,
    bootstrap_servers VARCHAR(255) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入示例数据
INSERT INTO kafka_clusters (cluster_name, bootstrap_servers, description) 
VALUES ('Local Kafka', 'localhost:9092', '本地Kafka集群');

INSERT INTO kafka_clusters (cluster_name, bootstrap_servers, description) 
VALUES ('Development Kafka', 'dev-kafka:9092', '开发环境Kafka集群');

-- 查询所有激活的集群
SELECT * FROM kafka_clusters WHERE is_active = TRUE;