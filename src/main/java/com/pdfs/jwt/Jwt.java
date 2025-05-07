package com.pdfs.jwt;

import com.pdfs.myuser.MyUser;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String token;
    private boolean isActive;
    @ManyToOne
    private MyUser user;

    public Jwt() {}

    public Jwt(String token) {
        this.token = token;
        this.isActive = true;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
