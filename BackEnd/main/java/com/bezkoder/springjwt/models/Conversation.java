package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference

    private List<Chat> chats = new ArrayList<>();

    public Conversation() {
    }

    public Conversation(String title) {
        this.title = title;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Chat> getChats() { return chats; }
    public void setChats(List<Chat> chats) { this.chats = chats; }

    public void addChat(Chat chat) {
        chats.add(chat);
        chat.setConversation(this);
    }

    public void removeChat(Chat chat) {
        chats.remove(chat);
        chat.setConversation(null);
    }
}

