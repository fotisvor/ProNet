package com.bezkoder.springjwt.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor


@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String username;

    public Comment(String content, String username){
        this.content=content;
        this.username=username;
    }

    public String getContent(){
        return this.content;
    }

    public String getUsername() {
        return username;
    }
}
