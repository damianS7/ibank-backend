package com.ibank.user.http;


import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank
    @NotEmpty
    public String username;

    @NotBlank
    @NotEmpty
    public String email;

    @NotBlank
    @NotEmpty
    public String oldPassword;

    @NotBlank
    @NotEmpty
    public String newPassword;

    @NotBlank
    @NotEmpty
    public String token;

}
