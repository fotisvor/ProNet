package com.bezkoder.springjwt.models;

import javax.persistence.*;
import java.beans.ConstructorProperties;
import java.util.List;

@Entity
@Table(name = "request")
public class Request {
    private String fromUser;
    private String toUser;
    private TYPE notification;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public Request(){}
    public Request(String fromUser, String toUser){
        this.fromUser=fromUser;
        this.toUser=toUser;
        this.notification = null;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }



}