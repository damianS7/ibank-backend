package com.ibank.user;

import com.ibank.user.exception.EmailTakenException;
import com.ibank.user.exception.UsernameTakenException;
import com.ibank.user.http.RegistrationResponse;
import com.ibank.user.http.UserUpdateRequest;
import com.ibank.user.http.UserUpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint para actualizar los datos de usuario

    /**
     * Endpoint para la actualizacion de usuarios existentes.
     *
     * @param request
     * @return Creado el usuario devuelve null
     */
    @PutMapping(path = "/api/v1/users", consumes = "application/json")
    public User update(@RequestBody UserUpdateRequest request) {
        return userService.update(request);
    }

    // Este metodo no deberia tirar exceptions ??? ...

    /**
     * Endpoint para la creacion de nuevos usuarios.
     *
     * @param user
     * @return Creado el usuario devuelve null
     */
    @PostMapping(path = "/api/v1/users/register", consumes = "application/json")
    public User create(@RequestBody User user) {
        return userService.register(user);
    }

}
