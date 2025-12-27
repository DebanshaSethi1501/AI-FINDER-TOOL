package com.example.AITools.repository;

import com.example.AITools.model.AiTools;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiToolsRepo extends JpaRepository<AiTools, Long> {
    List<AiTools> findByAdminId(Long adminId);
    Optional<AiTools> findByIdAndAdminId(Long id, Long adminId);
}

