package com.ibank.user.http;

import com.ibank.user.User;

public class RegistrationResponse {
    public Long id;
    public String username;
    public String email;

    public RegistrationResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public RegistrationResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
