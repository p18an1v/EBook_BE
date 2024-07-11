package com.LoginRegisterAuth.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
@Entity
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private LocalDateTime loginTimestamp;
    private LocalDateTime logoutTimestamp;

    // Constructors
    public UserActivity() {}

    public UserActivity(String email, LocalDateTime loginTimestamp) {
        this.email = email;
        this.loginTimestamp = loginTimestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLoginTimestamp() {
        return loginTimestamp;
    }

    public void setLoginTimestamp(LocalDateTime loginTimestamp) {
        this.loginTimestamp = loginTimestamp;
    }

    public LocalDateTime getLogoutTimestamp() {
        return logoutTimestamp;
    }

    public void setLogoutTimestamp(LocalDateTime logoutTimestamp) {
        this.logoutTimestamp = logoutTimestamp;
    }
}

