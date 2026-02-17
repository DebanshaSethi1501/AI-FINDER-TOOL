package com.example.AITools.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class Reveiws {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private String username; // Name of the user who submitted the review

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status = ReviewStatus.PENDING; // Default to PENDING

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aitool_id", nullable = false)
    @JsonIgnore
    private AiTools aiTool;

    public Reveiws() {
    }

    public Reveiws(Long id, String content, Double rating, String username, ReviewStatus status, AiTools aiTool) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.username = username;
        this.status = status;
        this.aiTool = aiTool;
    }

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

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public AiTools getAiTool() {
        return aiTool;
    }

    public void setAiTool(AiTools aiTool) {
        this.aiTool = aiTool;
    }

    // Enum for Review Status
    public enum ReviewStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
