package com.hy.demo.kafkaui.controller;

import com.hy.demo.kafkaui.model.KafkaCluster;
import com.hy.demo.kafkaui.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/clusters")
public class ClusterController {
    
    @Autowired
    private ClusterService clusterService;
    
    @GetMapping
    public String listClusters(Model model) {
        List<KafkaCluster> clusters = clusterService.getAllClusters();
        model.addAttribute("clusters", clusters);
        return "clusters";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("cluster", new KafkaCluster());
        return "cluster-form";
    }
    
    @PostMapping("/create")
    public String createCluster(@ModelAttribute KafkaCluster cluster, Model model) {
        try {
            // 设置创建时间和更新时间
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            cluster.setCreatedAt(now);
            cluster.setUpdatedAt(now);
            
            clusterService.createCluster(cluster);
            return "redirect:/clusters";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cluster", cluster);
            return "cluster-form";
        }
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            KafkaCluster cluster = clusterService.getClusterById(id)
                .orElseThrow(() -> new RuntimeException("未找到集群"));
            model.addAttribute("cluster", cluster);
            return "cluster-form";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clusters", clusterService.getAllClusters());
            return "clusters";
        }
    }
    
    @PostMapping("/update/{id}")
    public String updateCluster(@PathVariable Long id, @ModelAttribute KafkaCluster cluster, Model model) {
        try {
            // 设置更新时间
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            cluster.setUpdatedAt(now);
            
            clusterService.updateCluster(id, cluster);
            return "redirect:/clusters";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cluster", cluster);
            return "cluster-form";
        }
    }
    
    @GetMapping("/delete/{id}")
    public String deleteCluster(@PathVariable Long id, Model model) {
        try {
            clusterService.deleteCluster(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/clusters";
    }
    
    @GetMapping("/activate/{id}")
    public String activateCluster(@PathVariable Long id) {
        clusterService.activateCluster(id);
        return "redirect:/clusters";
    }
    
    @GetMapping("/deactivate/{id}")
    public String deactivateCluster(@PathVariable Long id) {
        clusterService.deactivateCluster(id);
        return "redirect:/clusters";
    }
}