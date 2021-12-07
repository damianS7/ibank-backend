package com.ibank.auth.http;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Template que contiene los datos a devolver cuando el usuario se logea
 * con exito en la aplicacion.
 */
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    public Long id;
    public String username;
    public String email;
    public String token;
}

