package com.example.aiToolFinder.model;

import jakarta.persistence.*;

@Entity
public class AiTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String useCase;
    private String category;
    private String pricingType;
    private double averageRating;

    public AiTool() {
    }

    public AiTool(String name, String useCase, String category, String pricingType) {
        this.name = name;
        this.useCase = useCase;
        this.category = category;
        this.pricingType = pricingType;
        this.averageRating = 0.0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUseCase() {
        return useCase;
    }

    public String getCategory() {
        return category;
    }

    public String getPricingType() {
        return pricingType;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUseCase(String useCase) {
        this.useCase = useCase;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
