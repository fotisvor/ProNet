package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.NotBlank;

public class SkillRequest {
    @NotBlank
    private String name;

    // Getter and Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}