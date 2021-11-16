package com.ibank.user;

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

}
