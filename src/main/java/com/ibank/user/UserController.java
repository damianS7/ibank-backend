package com.ibank.user;

import com.ibank.user.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para la actualizacion de usuarios existentes.
     *
     * @param updateRequest La peticion con los datos del usuario a actualizar
     * @return Creado el usuario devuelve null
     */
    @PutMapping(path = "/api/v1/users", consumes = "application/json")
    public UserUpdateResponse updateUser(@Valid @RequestBody UserUpdateRequest updateRequest) {
        return new UserUpdateResponse(userService.updateUser(updateRequest));
    }

    /**
     * Endpoint para la creacion de nuevos usuarios.
     *
     * @param signupRequest Los datos del usuario que se va a registrar
     * @return Creado el usuario devuelve null
     */
    @PostMapping(path = "/api/v1/users/signup", consumes = "application/json")
    public UserSignupResponse createUser(@Valid @RequestBody UserSignupRequest signupRequest) {
        return new UserSignupResponse(userService.registerUser(signupRequest));
    }
}
