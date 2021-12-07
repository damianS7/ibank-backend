package com.ibank.auth.http;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Peticion para logearse en la aplicacion
 */
@AllArgsConstructor
public class AuthenticationRequest {

    @NotBlank
    public String username;

    @NotBlank
    public String password;
}
