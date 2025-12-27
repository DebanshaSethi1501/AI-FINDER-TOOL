package com.example.AITools.controller;


import com.example.AITools.model.Admin;
import com.example.AITools.repository.AdminRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AdminController {

    private AdminRepo adminRepo;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public AdminController(AdminRepo adminRepo, PasswordEncoder passwordEncoder , AuthenticationManager authenticationManager){
        this.adminRepo=adminRepo;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager=authenticationManager;
    }
    @GetMapping("/admin")
    public List<Admin> getAllAdmin(){
        return adminRepo.findAll();
    }

    @PostMapping("/admin")
    public Admin saveAdmin(@RequestBody Admin admin){
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepo.save(admin);
    }

    @PostMapping("/admin/login")
    public String login(@RequestBody Admin admin) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        admin.getName(),
                        admin.getPassword()
                )
        );

        return "LOGIN SUCCESSFUL";
    }

}
