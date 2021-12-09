package com.ibank.auth.http;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 *
 */
@AllArgsConstructor
public class TokenValidationRequest {

    @NotBlank
    public String username;

    @NotBlank
    public String token;
}
