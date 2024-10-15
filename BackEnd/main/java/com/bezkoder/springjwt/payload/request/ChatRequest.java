package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.NotBlank;

public class ChatRequest {

    @NotBlank
    private String toUser;

    @NotBlank
    private String message;

    // Getters and setters
    public String getToUser() { return toUser; }
    public void setToUser(String toUser) { this.toUser = toUser; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
