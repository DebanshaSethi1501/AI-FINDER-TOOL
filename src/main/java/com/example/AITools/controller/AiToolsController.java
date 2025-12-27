package com.example.AITools.controller;

import com.example.AITools.model.Admin;
import com.example.AITools.model.AiTools;
import com.example.AITools.services.AdminService;
import com.example.AITools.services.AiToolsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aitools")
public class AiToolsController {

    private final AiToolsService aiToolsService;
    private final AdminService adminService;

    public AiToolsController(AiToolsService aiToolsService, AdminService adminService) {
        this.aiToolsService = aiToolsService;
        this.adminService = adminService;
    }

    /**
     * Get the authenticated admin's ID from the JWT token
     */
    private Long getAuthenticatedAdminId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Admin admin = adminService.getAdminByName(username);
        return admin.getId();
    }

    /**
     * Get all AI tools for the authenticated admin
     * GET /api/aitools/my-tools
     */
    @GetMapping("/my-tools")
    public ResponseEntity<List<AiTools>> getMyAiTools(Authentication authentication) {
        Long adminId = getAuthenticatedAdminId(authentication);
        List<AiTools> tools = aiToolsService.getAiToolsByAdminId(adminId);
        return ResponseEntity.ok(tools);
    }

    /**
     * Get all AI tools (public endpoint for browsing)
     * GET /api/aitools
     */
    @GetMapping
    public ResponseEntity<List<AiTools>> getAllAiTools() {
        List<AiTools> tools = aiToolsService.getAllAiTools();
        return ResponseEntity.ok(tools);
    }

    /**
     * Get a specific AI tool by ID
     * GET /api/aitools/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AiTools> getAiToolById(@PathVariable Long id) {
        AiTools tool = aiToolsService.getAiToolById(id);
        return ResponseEntity.ok(tool);
    }

    /**
     * Create a new AI tool (authenticated admin only)
     * POST /api/aitools
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAiTool(
            @RequestBody AiTools aiTool,
            Authentication authentication) {

        Long adminId = getAuthenticatedAdminId(authentication);
        AiTools createdTool = aiToolsService.createAiTool(adminId, aiTool);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "AI Tool created successfully");
        response.put("tool", createdTool);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an AI tool (authenticated admin only - must own the tool)
     * PUT /api/aitools/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAiTool(
            @PathVariable Long id,
            @RequestBody AiTools aiTool,
            Authentication authentication) {

        Long adminId = getAuthenticatedAdminId(authentication);
        AiTools updatedTool = aiToolsService.updateAiTool(id, adminId, aiTool);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "AI Tool updated successfully");
        response.put("tool", updatedTool);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete an AI tool (authenticated admin only - must own the tool)
     * DELETE /api/aitools/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAiTool(
            @PathVariable Long id,
            Authentication authentication) {

        Long adminId = getAuthenticatedAdminId(authentication);
        aiToolsService.deleteAiTool(id, adminId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "AI Tool deleted successfully");
        response.put("deletedId", id.toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Check if the authenticated admin owns a specific tool
     * GET /api/aitools/{id}/ownership
     */
    @GetMapping("/{id}/ownership")
    public ResponseEntity<Map<String, Boolean>> checkOwnership(
            @PathVariable Long id,
            Authentication authentication) {

        Long adminId = getAuthenticatedAdminId(authentication);
        boolean isOwner = aiToolsService.isToolOwnedByAdmin(id, adminId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isOwner", isOwner);

        return ResponseEntity.ok(response);
    }
}

