package com.pdfs.jwt.entity;

import com.pdfs.myuser.entity.MyUser;
import jakarta.persistence.*;

@Entity
public class Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String token;
    private boolean active;
    @ManyToOne
    private MyUser user;

    public Jwt() {}

    public Jwt(String token) {
        this.token = token;
        this.active = true;
    }

    public Jwt(String token, MyUser user) {
        this.token = token;
        this.user = user;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean active() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
