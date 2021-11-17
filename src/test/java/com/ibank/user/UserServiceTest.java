package com.ibank.user;

import com.ibank.user.exception.EmailTakenException;
import com.ibank.user.exception.UsernameTakenException;
import com.ibank.user.http.RegistrationResponse;
import com.ibank.user.http.UserUpdateRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void register() {
        // Registramos un usuario
        assertDoesNotThrow(() -> userService.register(
            new User(null, "demo", "demo@gmail.com", "1234")
        ));

        // Registrar el mismo usuario por lo que debe arrojar UsernameTakenExcepcion
        assertThrows(UsernameTakenException.class, () -> userService.register(
            new User(null, "demo", "demo1@gmail.com", "1234")
        ));

        // Registramos otro usuario con el mismo email por loq ue debe arrojar EmailTakenExcepcion
        assertThrows(EmailTakenException.class, () -> userService.register(
            new User(null, "demo2", "demo@gmail.com", "1234")
        ));

        // Registramos un usuario que no existe previamente
        RegistrationResponse rs = assertDoesNotThrow(() -> userService.register(
            new User(null, "demo1", "demo1@gmail.com", "1234")
        ));

        // ...
        //System.out.println("El id de: " + rs.username + " es: " +  rs.id.toString());
        //Logger.getLogger("UserServiceTest").log(Level.FINE, "El id de: " + rs.username + " es: " +  rs.id.toString());

        // Debe existir
        assertEquals(true, userRepository.findByUsername("demo").isPresent());

        // Debe existir
        assertEquals(true, userRepository.findByUsername("demo1").isPresent());

        // NO Debe existir
        assertEquals(false, userRepository.findByUsername("demo2").isPresent());
    }

    @Test
    @Disabled
    void update() {
        // Primero tenemos que logearnos

        // Actualizamos un usuario
        userService.update(
            new UserUpdateRequest("demo5", "demo@gmail.com", "1234", "123456")
        );
        assertDoesNotThrow(() -> userService.update(
            new UserUpdateRequest("demo", "demo@gmail.com", "1234", "123456")
        ));

        // Actualizamos un usuario incorrecto

        // Actualizamos un usuario con valores incorrectos


        // Debe existir
        assertEquals(true, userRepository.findByUsername("demo5").isPresent());
    }
}