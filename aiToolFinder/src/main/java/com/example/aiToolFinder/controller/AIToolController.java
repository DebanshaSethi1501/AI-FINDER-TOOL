package com.example.aiToolFinder.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.aiToolFinder.model.AiTool;
import com.example.aiToolFinder.service.AIToolService;

@RestController
@RequestMapping
public class AIToolController {

    private final AIToolService service;

    public AIToolController(AIToolService service) {
        this.service = service;
    }

    // User API - get all tools
    @GetMapping("/tools")
    public List<AiTool> getAllTools() {
        return service.getAllTools();
    }

    // Admin API - create tool
    @PostMapping("/admin/tools")
    public AiTool createTool(@RequestBody AiTool tool) {
        return service.createTool(tool);
    }

    // Admin API - update tool
    @PutMapping("/admin/tools/{id}")
    public AiTool updateTool(@PathVariable Long id, @RequestBody AiTool tool) {
        return service.updateTool(id, tool);
    }

    // Admin API - delete tool
    @DeleteMapping("/admin/tools/{id}")
    public String deleteTool(@PathVariable Long id) {
        service.deleteTool(id);
        return "Tool deleted successfully";
    }
}
