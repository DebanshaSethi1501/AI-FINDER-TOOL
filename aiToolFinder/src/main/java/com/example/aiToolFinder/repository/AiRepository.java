package com.example.aiToolFinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.aiToolFinder.model.AiTool;

public interface AiRepository extends JpaRepository<AiTool, Long> {
}
