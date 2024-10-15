package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String message;
    @NotNull
    private LocalDateTime timestamp;

    @NotBlank
    private String sender;
    public Chat(String user,String message) {
        this.message = message;
        this.timestamp= LocalDateTime.now();
        this.sender=user;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    @JsonBackReference

    private Conversation conversation;

    public Chat() {}

    //getters
    public Long getId() {return id;}
    public String getMessage() {return message;}
    public LocalDateTime getTimestamp() {return timestamp;}
    public String getSender(){return  sender;}
    //setters
    public void setId(Long id) {this.id = id;}
    public void setMessage(String message) {this.message = message;}
    public void setTimestamp(LocalDateTime timestamp) {this.timestamp = timestamp;}
    public void setSender(String i){this.sender=i;}
    public Conversation getConversation() { return conversation; }
    public void setConversation(Conversation conversation) { this.conversation = conversation; }
}
