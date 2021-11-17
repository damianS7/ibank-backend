package com.ibank.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void updateUser() {
        // Creacion de un usuario nuevo
        userRepository.save(new User(null, "demo", "demo1@gmail.com", "1234"));

        // Busqueda del usuario que debe fallar
        Optional<User> user = userRepository.findByUsername("dimo4");
        assertFalse(user.isPresent());

        // Busqueda del usuario por nombre con exito
        user = userRepository.findByUsername("demo");
        assertTrue(user.isPresent());

        // Actualizacion de email y password del usuario usuario
        int rowsAffected = userRepository.updateUser(user.get().getId(), "demo@gmail.com", "123456");
        assertEquals(1, rowsAffected);

        // Busqueda del usuario por email debe fallar
        user = userRepository.findByEmail("dimo4@gmail.com");
        assertFalse(user.isPresent());

        // Busqueda del usuario por email del usuario actualizado
        user = userRepository.findByEmail("demo@gmail.com");
        assertTrue(user.isPresent());


        // Actualizacion de email y password de un usuario que no existe
        rowsAffected = userRepository.updateUser(7L, "demo@gmail.com", "123456");
        assertEquals(0, rowsAffected);

    }
}