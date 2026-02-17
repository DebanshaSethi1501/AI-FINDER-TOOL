package com.example.AITools.controller;

import com.example.AITools.model.Reveiws;
import com.example.AITools.services.ReveiwsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReveiwsController {

    private final ReveiwsService reveiwsService;

    public ReveiwsController(ReveiwsService reveiwsService) {
        this.reveiwsService = reveiwsService;
    }

    /**
     * Submit a review (Public - No Auth)
     * POST /api/reviews
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> submitReview(@RequestBody ReviewSubmissionDTO reviewDTO) {
        Reveiws review = reveiwsService.submitReview(
                reviewDTO.getAiToolId(),
                reviewDTO.getContent(),
                reviewDTO.getRating(),
                reviewDTO.getUsername()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Review submitted successfully. It will be visible after admin approval.");
        response.put("reviewId", review.getId());
        response.put("status", review.getStatus().toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get approved reviews for a specific AI tool (Public - No Auth)
     * GET /api/reviews/aitool/{aiToolId}
     */
    @GetMapping("/aitool/{aiToolId}")
    public ResponseEntity<List<ReviewResponseDTO>> getApprovedReviewsForAiTool(@PathVariable Long aiToolId) {
        List<Reveiws> reviews = reveiwsService.getApprovedReviewsByAiToolId(aiToolId);
        List<ReviewResponseDTO> response = reviews.stream()
                .map(this::convertToResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Get all pending reviews (Admin Only - Requires Auth)
     * GET /api/reviews/pending
     */
    @GetMapping("/pending")
    public ResponseEntity<List<ReviewResponseDTO>> getPendingReviews() {
        List<Reveiws> reviews = reveiwsService.getAllPendingReviews();
        List<ReviewResponseDTO> response = reviews.stream()
                .map(this::convertToResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Approve a review (Admin Only - Requires Auth)
     * PUT /api/reviews/{reviewId}/approve
     */
    @PutMapping("/{reviewId}/approve")
    public ResponseEntity<Map<String, Object>> approveReview(@PathVariable Long reviewId) {
        Reveiws review = reveiwsService.approveReview(reviewId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Review approved successfully");
        response.put("reviewId", review.getId());
        response.put("status", review.getStatus().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Reject a review (Admin Only - Requires Auth)
     * PUT /api/reviews/{reviewId}/reject
     */
    @PutMapping("/{reviewId}/reject")
    public ResponseEntity<Map<String, Object>> rejectReview(@PathVariable Long reviewId) {
        Reveiws review = reveiwsService.rejectReview(reviewId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Review rejected successfully");
        response.put("reviewId", review.getId());
        response.put("status", review.getStatus().toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Delete a review (Admin Only - Requires Auth)
     * DELETE /api/reviews/{reviewId}
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable Long reviewId) {
        reveiwsService.deleteReview(reviewId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Review deleted successfully");
        response.put("deletedId", reviewId.toString());

        return ResponseEntity.ok(response);
    }

    /**
     * Get all approved reviews (Public - No Auth)
     * GET /api/reviews/approved
     */
    @GetMapping("/approved")
    public ResponseEntity<List<ReviewResponseDTO>> getAllApprovedReviews() {
        List<Reveiws> reviews = reveiwsService.getAllApprovedReviews();
        List<ReviewResponseDTO> response = reviews.stream()
                .map(this::convertToResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific review by ID (Admin Only)
     * GET /api/reviews/{reviewId}
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable Long reviewId) {
        Reveiws review = reveiwsService.getReviewById(reviewId);
        return ResponseEntity.ok(convertToResponseDTO(review));
    }

    // Helper method to convert entity to DTO
    private ReviewResponseDTO convertToResponseDTO(Reveiws review) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setRating(review.getRating());
        dto.setUsername(review.getUsername());
        dto.setStatus(review.getStatus().toString());
        dto.setAiToolId(review.getAiTool().getId());
        dto.setAiToolName(review.getAiTool().getName());
        return dto;
    }

    // DTOs
    public static class ReviewSubmissionDTO {
        private Long aiToolId;
        private String content;
        private Double rating;
        private String username;

        public Long getAiToolId() {
            return aiToolId;
        }

        public void setAiToolId(Long aiToolId) {
            this.aiToolId = aiToolId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class ReviewResponseDTO {
        private Long id;
        private String content;
        private Double rating;
        private String username;
        private String status;
        private Long aiToolId;
        private String aiToolName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Double getRating() {
            return rating;
        }

        public void setRating(Double rating) {
            this.rating = rating;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Long getAiToolId() {
            return aiToolId;
        }

        public void setAiToolId(Long aiToolId) {
            this.aiToolId = aiToolId;
        }

        public String getAiToolName() {
            return aiToolName;
        }

        public void setAiToolName(String aiToolName) {
            this.aiToolName = aiToolName;
        }
    }
}

