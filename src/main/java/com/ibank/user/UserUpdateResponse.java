package com.ibank.user;

public class UserUpdateResponse {
    private String username;
    private String email;

    public UserUpdateResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
