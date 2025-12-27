package com.example.AITools.controller;


import com.example.AITools.model.Admin;
import com.example.AITools.repository.AdminRepo;
import com.example.AITools.services.AdminService;
import com.example.AITools.services.CustomUserDetailsService;
import com.example.AITools.services.JWTService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AdminController {

    private final AdminRepo adminRepo;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AdminController(AdminRepo adminRepo,
                          AdminService adminService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JWTService jwtService,
                          CustomUserDetailsService userDetailsService){
        this.adminRepo = adminRepo;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Register a new admin
     * POST /auth/admin/register
     */
    @PostMapping("/admin/register")
    public ResponseEntity<Map<String, String>> registerAdmin(@RequestBody Admin admin){
        // Check if admin already exists
        if (adminRepo.findByName(admin.getName()).isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Admin savedAdmin = adminRepo.save(admin);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin registered successfully");
        response.put("username", savedAdmin.getName());
        response.put("id", savedAdmin.getId().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Login endpoint
     * POST /auth/admin/login
     */
    @PostMapping("/admin/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Admin admin) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            admin.getName(),
                            admin.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getName());
            String token = jwtService.generateToken(userDetails);

            // Get the admin ID
            Admin authenticatedAdmin = adminService.getAdminByName(admin.getName());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");
            response.put("username", admin.getName());
            response.put("adminId", authenticatedAdmin.getId().toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
    }

    /**
     * Get current admin profile (authenticated)
     * GET /auth/admin/profile
     */
    @GetMapping("/admin/profile")
    public ResponseEntity<Map<String, Object>> getProfile(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Admin admin = adminService.getAdminByName(userDetails.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("id", admin.getId());
        response.put("username", admin.getName());
        response.put("totalTools", admin.getAitools() != null ? admin.getAitools().size() : 0);

        return ResponseEntity.ok(response);
    }

}
