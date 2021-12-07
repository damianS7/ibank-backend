package com.ibank.auth;

import com.ibank.auth.exception.InvalidTokenException;
import com.ibank.auth.exception.TokenExpiredException;
import com.ibank.auth.http.AuthenticationRequest;
import com.ibank.auth.http.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    /**
     * Endpoint para la autenticacion
     *
     * @param request Peticion que contiene el usuario y password del usuario
     * @return Devuelve AuthenticationResponse con los datos de usuario (id, username, email y token)
     * @throws AuthenticationException Excepcion en caso de fallo
     */
    @PostMapping("/login")
    public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest request) throws AuthenticationException {
        return authenticationService.login(request);
    }

    /**
     * Endpoint para la validacion del token
     *
     * @throws AuthenticationException Excepcion si falla
     */
    @GetMapping("/api/v1/user/token/validate")
    public void validateToken() throws InvalidTokenException, TokenExpiredException {
    }
}
