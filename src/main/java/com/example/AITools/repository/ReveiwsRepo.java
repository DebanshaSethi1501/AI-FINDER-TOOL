package com.example.AITools.repository;

import com.example.AITools.model.Reveiws;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReveiwsRepo extends JpaRepository<Reveiws, Long> {

    // Find all reviews by status with eager fetch of AiTools
    @Query("SELECT r FROM Reveiws r JOIN FETCH r.aiTool WHERE r.status = :status")
    List<Reveiws> findByStatusWithAiTool(Reveiws.ReviewStatus status);

    // Find reviews by AI tool ID with eager fetch
    @Query("SELECT r FROM Reveiws r JOIN FETCH r.aiTool WHERE r.aiTool.id = :aiToolId")
    List<Reveiws> findByAiToolIdWithFetch(Long aiToolId);

    // Find reviews by AI tool ID and status with eager fetch
    @Query("SELECT r FROM Reveiws r JOIN FETCH r.aiTool WHERE r.aiTool.id = :aiToolId AND r.status = :status")
    List<Reveiws> findByAiToolIdAndStatusWithFetch(Long aiToolId, Reveiws.ReviewStatus status);

    // Find all pending reviews with eager fetch
    default List<Reveiws> findAllPending() {
        return findByStatusWithAiTool(Reveiws.ReviewStatus.PENDING);
    }

    // Find all approved reviews with eager fetch
    default List<Reveiws> findAllApproved() {
        return findByStatusWithAiTool(Reveiws.ReviewStatus.APPROVED);
    }
}

