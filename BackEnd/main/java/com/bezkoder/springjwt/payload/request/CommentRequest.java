package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.NotBlank;

public class CommentRequest {
    @NotBlank
    private Long id;

    @NotBlank
    private String content;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
