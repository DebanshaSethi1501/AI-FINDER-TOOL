package com.example.AITools.repository;

import com.example.AITools.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdminRepo extends JpaRepository<Admin,Long> {
    Optional<Admin> findByName(String name);
}
