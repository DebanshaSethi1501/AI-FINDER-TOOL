package com.example.AITools.services;

import com.example.AITools.model.AiTools;
import com.example.AITools.model.Reveiws;
import com.example.AITools.repository.AiToolsRepo;
import com.example.AITools.repository.ReveiwsRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReveiwsService {

    private final ReveiwsRepo reveiwsRepo;
    private final AiToolsRepo aiToolsRepo;

    public ReveiwsService(ReveiwsRepo reveiwsRepo, AiToolsRepo aiToolsRepo) {
        this.reveiwsRepo = reveiwsRepo;
        this.aiToolsRepo = aiToolsRepo;
    }

    @Transactional
    public Reveiws submitReview(Long aiToolId, String content, Double rating, String username) {
        AiTools aiTool = aiToolsRepo.findById(aiToolId)
                .orElseThrow(() -> new RuntimeException("AI Tool not found with id: " + aiToolId));

        Reveiws review = new Reveiws();
        review.setContent(content);
        review.setRating(rating);
        review.setUsername(username);
        review.setStatus(Reveiws.ReviewStatus.PENDING);
        review.setAiTool(aiTool);

        return reveiwsRepo.save(review);
    }


    @Transactional(readOnly = true)
    public List<Reveiws> getAllPendingReviews() {
        return reveiwsRepo.findAllPending();
    }

    @Transactional(readOnly = true)
    public List<Reveiws> getAllApprovedReviews() {
        return reveiwsRepo.findAllApproved();
    }

    @Transactional(readOnly = true)
    public List<Reveiws> getReviewsByAiToolId(Long aiToolId) {
        return reveiwsRepo.findByAiToolIdWithFetch(aiToolId);
    }

    @Transactional(readOnly = true)
    public List<Reveiws> getApprovedReviewsByAiToolId(Long aiToolId) {
        return reveiwsRepo.findByAiToolIdAndStatusWithFetch(aiToolId, Reveiws.ReviewStatus.APPROVED);
    }

    @Transactional(readOnly = true)
    public Reveiws getReviewById(Long reviewId) {
        return reveiwsRepo.findByIdWithAiTool(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
    }

    @Transactional
    public Reveiws approveReview(Long reviewId) {
        Reveiws review = getReviewById(reviewId);
        review.setStatus(Reveiws.ReviewStatus.APPROVED);
        return reveiwsRepo.save(review);
    }


    @Transactional
    public Reveiws rejectReview(Long reviewId) {
        Reveiws review = getReviewById(reviewId);
        review.setStatus(Reveiws.ReviewStatus.REJECTED);
        return reveiwsRepo.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Reveiws review = getReviewById(reviewId);
        reveiwsRepo.delete(review);
    }

    @Transactional(readOnly = true)
    public List<Reveiws> getReviewsByStatus(Reveiws.ReviewStatus status) {
        return reveiwsRepo.findByStatusWithAiTool(status);
    }
}

