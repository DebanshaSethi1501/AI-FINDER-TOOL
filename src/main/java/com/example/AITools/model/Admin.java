package com.example.AITools.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ✅ wrapper

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    private List<AiTools> aitools;

    public Admin() {
    }

    public Admin(Long id, String name, String password, List<AiTools> aitools) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.aitools = aitools;
    }

    public Long getId() {          // ✅ wrapper
        return id;
    }

    public void setId(Long id) {   // ✅ wrapper
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AiTools> getAitools() {
        return aitools;
    }

    public void setAitools(List<AiTools> aitools) {
        this.aitools = aitools;
    }
}
