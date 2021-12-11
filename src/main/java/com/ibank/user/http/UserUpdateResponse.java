package com.ibank.user.http;

import com.ibank.user.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateResponse {
    public Long id;
    public String username;
    public String email;

    public UserUpdateResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
