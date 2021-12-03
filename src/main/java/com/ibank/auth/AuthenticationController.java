package com.ibank.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Punto de acceso (POST) para la autenticacion
     *
     * @param request Peticion que contiene el usuario y password del usuario
     * @return Devuelve AuthenticationResponse con los datos de usuario (id, username, email y token)
     * @throws AuthenticationException Excepcion en caso de fallo
     */
    @PostMapping("/api/v1/users/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) throws AuthenticationException {
        return authenticationService.auth(request);
    }

    // Validacion de token
    @GetMapping("/api/v1/users/tokenvalidation")
    //@GetMapping("/api/v1/token/validate")
    public void tokenValidation() throws AuthenticationException {
    }
}
