package com.example.aiToolFinder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.aiToolFinder.model.AiTool;
import com.example.aiToolFinder.repository.AiRepository;

@Service
public class AIToolService {

    private final AiRepository repo;

    public AIToolService(AiRepository repo) {
        this.repo = repo;
    }

    // Create tool
    public AiTool createTool(AiTool tool) {
        tool.setAverageRating(0.0);
        return repo.save(tool);
    }

    // Get all tools
    public List<AiTool> getAllTools() {
        return repo.findAll();
    }

    // Update tool
    public AiTool updateTool(Long id, AiTool updated) {
        AiTool tool = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found with id: " + id));

        tool.setName(updated.getName());
        tool.setUseCase(updated.getUseCase());
        tool.setCategory(updated.getCategory());
        tool.setPricingType(updated.getPricingType());

        return repo.save(tool);
    }

    // Delete tool
    public void deleteTool(Long id) {
        repo.deleteById(id);
    }
    
    public List<AiTool> getFilteredTools(String category,Double rating, String price){
		return repo.filterTools(category,price,rating);
	}
	
	public AiTool saveTool(AiTool tool) {
		return repo.save(tool);
	}

}
