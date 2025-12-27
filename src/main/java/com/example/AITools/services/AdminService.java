package com.example.AITools.services;

import com.example.AITools.model.Admin;
import com.example.AITools.repository.AdminRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepo adminRepo;

    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    /**
     * Get admin by ID
     */
    public Admin getAdminById(Long adminId) {
        return adminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
    }

    /**
     * Get admin by username
     */
    public Admin getAdminByName(String name) {
        return adminRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Admin not found with name: " + name));
    }

    /**
     * Get all admins
     */
    public List<Admin> getAllAdmins() {
        return adminRepo.findAll();
    }

    /**
     * Update admin profile
     */
    public Admin updateAdmin(Long adminId, Admin updatedAdmin) {
        Admin admin = getAdminById(adminId);
        admin.setName(updatedAdmin.getName());
        // Don't update password here - use separate method with encoding
        return adminRepo.save(admin);
    }

    /**
     * Delete admin
     */
    public void deleteAdmin(Long adminId) {
        Admin admin = getAdminById(adminId);
        adminRepo.delete(admin);
    }
}
