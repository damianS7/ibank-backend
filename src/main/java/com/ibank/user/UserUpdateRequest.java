package com.ibank.user;


public class UserUpdateRequest {
    private final String username;
    private final String email;
    private final String oldPassword;
    private final String newPassword;

    public UserUpdateRequest(String username, String email, String oldPassword, String newPassword) {
        this.username = username;
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
