package com.bezkoder.springjwt.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor


@Table(name = "advertisment")
public class Advertisment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String post;

    @NotBlank
    private String username;

    private Integer likecounter;

    @Column(name = "created_at", nullable = true, updatable = false)
    private LocalDateTime createdAt;


    @ElementCollection
    @CollectionTable(name = "advertisement_likes", joinColumns = @JoinColumn(name = "advertisement_id"))
    @Column(name = "user_like")
    private List<String> likes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Advertisment(String content,String username) {
        this.post = content;
        this.username = username;
        this.likecounter = 0;
        this.createdAt = LocalDateTime.now();  // Automatically set the creation time

    }

    public String getContent() {
        return this.post;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public void addLike(String username){
        if (this.likes.add(username)) { // only increment if the like is new
            this.likecounter++;
        }
    }

    public void removeLike(String username){
        this.likecounter--;
        this.likes.remove(username);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
