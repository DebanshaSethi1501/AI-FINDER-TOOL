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

    /**
     * Get all AI tools for a specific admin
     */
    public List<AiTools> getAiToolsByAdminId(Long adminId) {
        return aiToolsRepo.findByAdminId(adminId);
    }

    /**
     * Get all AI tools (for public viewing or admin purposes)
     */
    public List<AiTools> getAllAiTools() {
        return aiToolsRepo.findAll();
    }

    /**
     * Get a specific AI tool by ID and verify it belongs to the admin
     */
    public AiTools getAiToolByIdAndAdminId(Long toolId, Long adminId) {
        return aiToolsRepo.findByIdAndAdminId(toolId, adminId)
                .orElseThrow(() -> new RuntimeException("AI Tool not found or does not belong to this admin"));
    }

    /**
     * Get a specific AI tool by ID (without admin verification)
     */
    public AiTools getAiToolById(Long toolId) {
        return aiToolsRepo.findById(toolId)
                .orElseThrow(() -> new RuntimeException("AI Tool not found with id: " + toolId));
    }

    /**
     * Create a new AI tool for an admin
     */
    @Transactional
    public AiTools createAiTool(Long adminId, AiTools aiTool) {
        Admin admin = adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        aiTool.setAdmin(admin);
        return aiToolsRepo.save(aiTool);
    }

    /**
     * Update an existing AI tool (only if it belongs to the admin)
     */
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

    /**
     * Delete an AI tool (only if it belongs to the admin)
     */
    @Transactional
    public void deleteAiTool(Long toolId, Long adminId) {
        AiTools aiTool = aiToolsRepo.findByIdAndAdminId(toolId, adminId)
                .orElseThrow(() -> new RuntimeException("AI Tool not found or does not belong to this admin"));

        aiToolsRepo.delete(aiTool);
    }

    /**
     * Check if an AI tool belongs to a specific admin
     */
    public boolean isToolOwnedByAdmin(Long toolId, Long adminId) {
        return aiToolsRepo.findByIdAndAdminId(toolId, adminId).isPresent();
    }
}

