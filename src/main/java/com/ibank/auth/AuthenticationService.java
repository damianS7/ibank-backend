package com.ibank.auth;

import com.ibank.auth.exception.AuthenticationFailedException;
import com.ibank.auth.http.AuthenticationRequest;
import com.ibank.auth.http.AuthenticationResponse;
import com.ibank.user.User;
import com.ibank.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Servicio que permite autenticarse en la aplicacion
 */
@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Este metodo permite iniciar sesion en la aplicacion. Dado un usuario y password
     * se coteja la informacion con la DB y se crea un token con la entidad User
     *
     * @param request Peticion que contiene los datos de usuario que intenta autenticarse (usuario y password)
     * @return La respuesta que contiene los datos del usuario autentificado.
     * @throws AuthenticationException Excepcion con el mensaje del fallo arrojado durante la autenticacion
     */
    public AuthenticationResponse login(AuthenticationRequest request) throws AuthenticationException {
        String username = request.username;
        String password = request.password;

        Authentication auth;

        try {
            auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>())
            );
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Bad credentials"); // 403 Forbidden
        }

        // Usuario autenticado
        User authUser = (User) auth.getPrincipal();

        // Creamos el token utilizado para validar al usuario
        String token = JwtUtil.createToken(authUser);

        // Enviamos al usuario de vuelta los datos necesarios para el cliente
        return new AuthenticationResponse(
            authUser.getId(), authUser.getUsername(), authUser.getEmail(), token
        );
    }

}
