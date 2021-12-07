package com.ibank.auth.http;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Template que contiene los datos a devolver cuando el usuario se logea
 * con exito en la aplicacion.
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthenticationResponse {
    public Long id;
    public String username;
    public String email;
    public String token;
}

