package com.bezkoder.springjwt.payload.request;

import com.bezkoder.springjwt.models.Skill;

import javax.validation.constraints.NotBlank;
import java.util.Set;

public class JobRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Set<String> skills;

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<String> getSkills() {
        return skills;
    }
}
