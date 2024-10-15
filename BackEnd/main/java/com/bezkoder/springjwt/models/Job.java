package com.bezkoder.springjwt.models;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @NotNull
    private LocalDateTime timestamp;

    @NotBlank
    private String employer;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "job_id")
    @JsonManagedReference
    private Set<User> applicants = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();

    public Job(String employer,String title,String description, Set<Skill> skills){
        this.employer=employer;
        this.title=title;
        this.description=description;
        this.timestamp=LocalDateTime.now();
        this.applicants=null;
        this.skills=skills;
    }

    public Job(){}

    public Long getId() {
        return id;
    }

    public String getEmployer() {
        return employer;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Set<User> getApplicants() { return applicants; }
    public void setApplicants(Set<User> applicants) { this.applicants = applicants; }

    public void addApplicant(User user) {
        applicants.add(user);
    }

    public void removeApplicant(User user) {
        applicants.remove(user);
    }

    public Set<Skill> getSkills() { return skills; }

    public void setSkills(Set<Skill> skills) {this.skills = skills;}
}
