package com.ibank.user.http;

import com.ibank.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserSignupResponse {
    public Long id;
    public String username;
    public String email;
    public String password;

    public UserSignupResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
