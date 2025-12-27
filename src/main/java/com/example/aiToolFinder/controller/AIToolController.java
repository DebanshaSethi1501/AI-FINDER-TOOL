package com.example.aiToolFinder.controller;

import java.util.List; 

import org.springframework.web.bind.annotation.*;

import com.example.aiToolFinder.model.AiTool;
import com.example.aiToolFinder.service.AIToolService;

@RestController
@RequestMapping("/tools")
public class AIToolController {

    private final AIToolService service;

    public AIToolController(AIToolService service) {
        this.service = service;
    }

    // User API - get all tools // added filters
    @GetMapping
    public List<AiTool> getTools(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, name = "price") String price,
            @RequestParam(required = false) Double rating
    ) {
        return service.getFilteredTools(category, rating, price);
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
