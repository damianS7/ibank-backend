package com.ibank.registration;

public class RegistrationResponse {
    public Long id;
    public String username;
    public String email;

    public RegistrationResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
