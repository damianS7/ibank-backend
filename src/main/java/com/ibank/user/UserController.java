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
    @PutMapping("/api/v1/users")
    public UserUpdateResponse update(@RequestBody UserUpdateRequest request) {
        return userService.update(request);
    }

    /**
     * Metodo para crear nuevos usuarios.
     * Al enviar una peticion POST a la URL api/users/
     * con un objeto JSON con los campos de usuario requeridos
     * la aplicacion creara un nuevo usuario y nos devolvera el mismo
     * objeto si ha tenido exito.
     * @param user
     * @return Creado el usuario devuelve null
     */
    @PostMapping(path = "/api/v1/users/registration", consumes = "application/json")
    public RegistrationResponse create(@RequestBody User user) throws EmailTakenException, UsernameTakenException {
        return userService.register(user);
    }

}
