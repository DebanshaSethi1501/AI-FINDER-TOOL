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

    // ✅ MODIFIED: Now recalculates average rating when review is approved
    @Transactional
    public Reveiws approveReview(Long reviewId) {
        Reveiws review = getReviewById(reviewId);
        review.setStatus(Reveiws.ReviewStatus.APPROVED);
        Reveiws savedReview = reveiwsRepo.save(review);

        // ✅ NEW: Recalculate and update average rating for the tool
        calculateAndUpdateAverageRating(review.getAiTool().getId());

        return savedReview;
    }

    // ✅ MODIFIED: Now recalculates average rating when review is rejected
    @Transactional
    public Reveiws rejectReview(Long reviewId) {
        Reveiws review = getReviewById(reviewId);
        review.setStatus(Reveiws.ReviewStatus.REJECTED);
        Reveiws savedReview = reveiwsRepo.save(review);

        // ✅ NEW: Recalculate average rating (in case it was previously approved)
        calculateAndUpdateAverageRating(review.getAiTool().getId());

        return savedReview;
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Reveiws review = getReviewById(reviewId);
        Long aiToolId = review.getAiTool().getId();
        reveiwsRepo.delete(review);

        // ✅ NEW: Recalculate average rating after deletion
        calculateAndUpdateAverageRating(aiToolId);
    }

    @Transactional(readOnly = true)
    public List<Reveiws> getReviewsByStatus(Reveiws.ReviewStatus status) {
        return reveiwsRepo.findByStatusWithAiTool(status);
    }

    // ✅ NEW METHOD: Calculates average rating from approved reviews and updates the tool
    @Transactional
    public void calculateAndUpdateAverageRating(Long aiToolId) {
        // Get all APPROVED reviews for this tool
        List<Reveiws> approvedReviews = reveiwsRepo.findByAiToolIdAndStatusWithFetch(
                aiToolId,
                Reveiws.ReviewStatus.APPROVED
        );

        // Get the AI tool
        AiTools aiTool = aiToolsRepo.findById(aiToolId)
                .orElseThrow(() -> new RuntimeException("AI Tool not found with id: " + aiToolId));

        // Calculate average rating
        double averageRating = 0.0;
        if (!approvedReviews.isEmpty()) {
            double sum = approvedReviews.stream()
                    .mapToDouble(Reveiws::getRating)
                    .sum();
            averageRating = sum / approvedReviews.size();
            // Round to 2 decimal places
            averageRating = Math.round(averageRating * 100.0) / 100.0;
        }

        // Update the tool's rating
        aiTool.setRating(averageRating);
        aiToolsRepo.save(aiTool);

        System.out.println("✅ Updated average rating for tool " + aiToolId + ": " + averageRating +
                " (based on " + approvedReviews.size() + " approved reviews)");
    }
}