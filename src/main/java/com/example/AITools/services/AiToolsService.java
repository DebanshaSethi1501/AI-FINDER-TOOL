package com.example.AITools.services;

import com.example.AITools.model.Admin;
import com.example.AITools.model.AiTools;
import com.example.AITools.repository.AdminRepo;
import com.example.AITools.repository.AiToolsRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AiToolsService {

    private final AiToolsRepo aiToolsRepo;
    private final AdminRepo adminRepo;

    public AiToolsService(AiToolsRepo aiToolsRepo, AdminRepo adminRepo) {
        this.aiToolsRepo = aiToolsRepo;
        this.adminRepo = adminRepo;
    }

    public List<AiTools> getAiToolsByAdminId(Long adminId) {
        return aiToolsRepo.findByAdminId(adminId);
    }

    public List<AiTools> getAllAiTools() {
        return aiToolsRepo.findAll();
    }
    public AiTools getAiToolByIdAndAdminId(Long toolId, Long adminId) {
        return aiToolsRepo.findByIdAndAdminId(toolId, adminId)
                .orElseThrow(() -> new RuntimeException("AI Tool not found or does not belong to this admin"));
    }

    public AiTools getAiToolById(Long toolId) {
        return aiToolsRepo.findById(toolId)
                .orElseThrow(() -> new RuntimeException("AI Tool not found with id: " + toolId));
    }

    @Transactional
    public AiTools createAiTool(Long adminId, AiTools aiTool) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        aiTool.setAdmin(admin);
        return aiToolsRepo.save(aiTool);
    }

    @Transactional
    public AiTools updateAiTool(Long toolId, Long adminId, AiTools updatedTool) {
        AiTools existingTool = aiToolsRepo.findByIdAndAdminId(toolId, adminId)
                .orElseThrow(() -> new RuntimeException("AI Tool not found or does not belong to this admin"));

        // Update fields
        existingTool.setName(updatedTool.getName());
        existingTool.setDecription(updatedTool.getDecription());
        existingTool.setUsecases(updatedTool.getUsecases());
        existingTool.setCategory(updatedTool.getCategory());
        existingTool.setPricingtype(updatedTool.getPricingtype());
        existingTool.setRating(updatedTool.getRating());

        return aiToolsRepo.save(existingTool);
    }

    @Transactional
    public void deleteAiTool(Long toolId, Long adminId) {
        AiTools aiTool = aiToolsRepo.findByIdAndAdminId(toolId, adminId)
                .orElseThrow(() -> new RuntimeException("AI Tool not found or does not belong to this admin"));

        aiToolsRepo.delete(aiTool);
    }

    public boolean isToolOwnedByAdmin(Long toolId, Long adminId) {
        return aiToolsRepo.findByIdAndAdminId(toolId, adminId).isPresent();
    }

    public List<AiTools> getFilteredTools(String category,Double rating, String price){
        return aiToolsRepo.filterTools(category,price,rating);
    }

}

