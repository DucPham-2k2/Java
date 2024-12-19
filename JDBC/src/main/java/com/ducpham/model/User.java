package com.ducpham.model;

public class User {
    private Long id;
    private String username;
    private String password;
    private String description;

    public User() {}

    public User(String username, String password, String description) {
        this.username = username;
        this.password = password;
        this.description = description;
    }

    public User(Long id, String username, String password, String description) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", designation='" + description + '\'' +
                '}';
    }
}
