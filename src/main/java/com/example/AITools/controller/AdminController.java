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

    @PostMapping("/admin/register")
    public ResponseEntity<Map<String, String>> registerAdmin(@RequestBody Admin admin){
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


}
