package com.hy.demo.kafkaui.controller;

import com.hy.demo.kafkaui.model.KafkaCluster;
import com.hy.demo.kafkaui.service.ClusterService;
import com.hy.demo.kafkaui.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class KafkaController {
    
    @Autowired
    private KafkaService kafkaService;
    
    @Autowired
    private ClusterService clusterService;
    
    @GetMapping("/")
    public String index(@RequestParam(required = false) String keyword, Model model) {
        // 获取激活的集群列表
        List<KafkaCluster> clusters = clusterService.getActiveClusters();
        model.addAttribute("clusters", clusters);
        
        // 如果有激活的集群，获取第一个集群的主题列表
        if (!clusters.isEmpty()) {
            KafkaCluster activeCluster = clusters.get(0);
            
            // 如果有关键字，则搜索主题，否则获取所有主题
            List<String> topics;
            if (keyword != null && !keyword.trim().isEmpty()) {
                topics = kafkaService.searchTopics(activeCluster.getBootstrapServers(), keyword);
                model.addAttribute("keyword", keyword);
            } else {
                topics = kafkaService.getTopics(activeCluster.getBootstrapServers());
            }
            
            model.addAttribute("topics", topics);
            model.addAttribute("activeCluster", activeCluster);
        }
        
        return "index";
    }
    
    @GetMapping("/cluster/{id}")
    public String clusterTopics(@PathVariable Long id, 
                              @RequestParam(required = false) String keyword,
                              Model model) {
        // 获取所有激活的集群
        List<KafkaCluster> clusters = clusterService.getActiveClusters();
        model.addAttribute("clusters", clusters);
        
        // 获取指定集群的主题列表
        KafkaCluster selectedCluster = clusterService.getClusterById(id)
            .orElseThrow(() -> new RuntimeException("未找到集群"));
        
        // 如果有关键字，则搜索主题，否则获取所有主题
        List<String> topics;
        if (keyword != null && !keyword.trim().isEmpty()) {
            topics = kafkaService.searchTopics(selectedCluster.getBootstrapServers(), keyword);
            model.addAttribute("keyword", keyword);
        } else {
            topics = kafkaService.getTopics(selectedCluster.getBootstrapServers());
        }
        
        model.addAttribute("topics", topics);
        model.addAttribute("activeCluster", selectedCluster);
        return "index";
    }
    
    @PostMapping("/topic/create")
    public String createTopic(@RequestParam String topicName, 
                            @RequestParam(defaultValue = "1") int partitions,
                            @RequestParam(defaultValue = "1") short replicationFactor,
                            @RequestParam(required = false) Long clusterId,
                            Model model) {
        boolean success;
        if (clusterId != null) {
            // 获取指定集群
            KafkaCluster cluster = clusterService.getClusterById(clusterId)
                .orElseThrow(() -> new RuntimeException("未找到集群"));
            success = kafkaService.createTopic(cluster.getBootstrapServers(), topicName, partitions, replicationFactor);
        } else {
            success = kafkaService.createTopic(topicName, partitions, replicationFactor);
        }
        model.addAttribute("message", success ? "主题创建成功" : "主题创建失败");
        return "redirect:/";
    }
    
    @PostMapping("/message/send")
    public String sendMessage(@RequestParam String topic,
                            @RequestParam String key,
                            @RequestParam String message,
                            @RequestParam(required = false) Long clusterId,
                            Model model) {
        boolean success;
        if (clusterId != null) {
            // 获取指定集群
            KafkaCluster cluster = clusterService.getClusterById(clusterId)
                .orElseThrow(() -> new RuntimeException("未找到集群"));
            success = kafkaService.sendMessage(cluster.getBootstrapServers(), topic, key, message);
        } else {
            success = kafkaService.sendMessage(topic, key, message);
        }
        model.addAttribute("message", success ? "消息发送成功" : "消息发送失败");
        return "redirect:/";
    }
    
    @GetMapping("/message/consume")
    public String consumeMessages(@RequestParam String topic,
                                @RequestParam(defaultValue = "10") int maxMessages,
                                @RequestParam(required = false) Long clusterId,
                                Model model) {
        List<String> messages;
        List<String> topics;
        KafkaCluster activeCluster = null;
        
        if (clusterId != null) {
            // 获取指定集群
            activeCluster = clusterService.getClusterById(clusterId)
                .orElseThrow(() -> new RuntimeException("未找到集群"));
            messages = kafkaService.consumeMessages(activeCluster.getBootstrapServers(), topic, maxMessages);
            topics = kafkaService.getTopics(activeCluster.getBootstrapServers());
        } else {
            messages = kafkaService.consumeMessages(topic, maxMessages);
            topics = kafkaService.getTopics();
        }
        
        model.addAttribute("messages", messages);
        model.addAttribute("selectedTopic", topic);
        model.addAttribute("topics", topics);
        model.addAttribute("activeCluster", activeCluster);
        
        // 获取所有激活的集群
        List<KafkaCluster> clusters = clusterService.getActiveClusters();
        model.addAttribute("clusters", clusters);
        
        return "index";
    }
}