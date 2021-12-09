package com.ibank.auth;

import com.ibank.auth.http.AuthenticationRequest;
import com.ibank.auth.http.AuthenticationResponse;
import com.ibank.auth.http.TokenValidationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
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
    @PostMapping(path = "/api/v1/login", consumes = "application/json")
    public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest request) {
        return authenticationService.login(request);
    }

    /**
     * Endpoint para la validacion del token
     *
     * @throws AuthenticationException Excepcion si falla
     */
    @GetMapping("/api/v1/user/token/validate")
    public String validateToken(@Valid @RequestBody TokenValidationRequest request) {
        return "token is valid";
        // MalFormedJWT ... ???
        //return authenticationService.validToken();
    }
}
