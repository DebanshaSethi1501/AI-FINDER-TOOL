package com.example.AITools.controller;

import com.example.AITools.model.AiTools;
import com.example.AITools.services.AdminService;
import com.example.AITools.services.AiToolsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("auth/users")
public class UserController {
    private final AiToolsService aiToolsService;
    private final AdminService adminService;

    public UserController(AiToolsService aiToolsService, AdminService adminService) {
        this.aiToolsService = aiToolsService;
        this.adminService = adminService;
    }

    @GetMapping
    public List<AiTools> getAllAiTools() {
        return aiToolsService.getAllAiTools();
    }


}
