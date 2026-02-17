package com.example.AITools.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "AiTools")
public class AiTools {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String decription;

    private String usecases;

    private String category;

    private String pricingtype;

    private double rating;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "aiTool", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reveiws> reviews;

    public AiTools() {
    }

    public AiTools(Long id, String name, String decription, String usecases, String category, String pricingtype, double rating, Admin admin) {
        this.id = id;
        this.name = name;
        this.decription = decription;
        this.usecases = usecases;
        this.category = category;
        this.pricingtype = pricingtype;
        this.rating = rating;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public String getUsecases() {
        return usecases;
    }

    public void setUsecases(String usecases) {
        this.usecases = usecases;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPricingtype() {
        return pricingtype;
    }

    public void setPricingtype(String pricingtype) {
        this.pricingtype = pricingtype;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Reveiws> getReviews() {
        return reviews;
    }

    public void setReviews(List<Reveiws> reviews) {
        this.reviews = reviews;
    }
}
