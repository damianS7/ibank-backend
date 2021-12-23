package com.ibank.user.http;

import com.ibank.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
public class UserSignupRequest {

    @NotBlank
    public String username;

    @NotBlank
    public String email;

    @NotBlank
    public String password;

    public UserSignupRequest(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
