package com.ibank.user.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UserUpdateResponse {

    @Getter
    private final String username;

    @Getter
    private final String email;
}
