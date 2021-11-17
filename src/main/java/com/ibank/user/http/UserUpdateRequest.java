package com.ibank.user.http;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UserUpdateRequest {

    @Getter
    private final String username;

    @Getter
    private final String email;

    @Getter
    private final String oldPassword;

    @Getter
    private final String newPassword;

}
