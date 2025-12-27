package com.example.AITools.services;

import com.example.AITools.model.Admin;
import com.example.AITools.model.CustomUserDetails;
import com.example.AITools.repository.AdminRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepo adminRepo;

    public CustomUserDetailsService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepo.findByName(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Admin not found: " + username)
                );

        return new CustomUserDetails(admin);
    }
}
