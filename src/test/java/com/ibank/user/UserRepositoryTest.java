package com.ibank.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@Transactional
@Slf4j
public class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    public void emailFieldShouldBeUnique() {
        // given
        User userGiven = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // then
        underTest.save(userGiven);

        // Debe fallar al intentar guardar un iban duplicado
        assertThrows(DataIntegrityViolationException.class, () -> {
            // when
            underTest.save(new User(
                null,
                "demo",
                "demo@gmail.com",
                "1234"
            ));
        });
    }

    @Test
    public void usernameFieldShouldBeUnique() {
        // given
        User userGiven = new User(
            null,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // then
        underTest.save(userGiven);

        // Debe fallar al intentar guardar un iban duplicado
        assertThrows(DataIntegrityViolationException.class, () -> {
            // when
            underTest.save(new User(
                null,
                "demo",
                "demo2@gmail.com",
                "1234"
            ));
        });
    }

    @Test
    @Order(1)
    public void itShouldSelectUserByEmail() {
        // given
        User userGiven = new User(
            null,
            "demo",
            "demo1@gmail.com",
            "1234"
        );

        // when
        underTest.save(userGiven);

        // then
        Optional<User> optUser = underTest.findByEmail(userGiven.getEmail());
        assertThat(optUser).isPresent().hasValueSatisfying(user -> {
            assertThat(user.getEmail()).isEqualTo(userGiven.getEmail());
        });
    }

    @Test
    @Order(2)
    public void itShouldSelectUserByUsername() {
        // given
        User userGiven = new User(
            null,
            "demo",
            "demo1@gmail.com",
            "1234"
        );

        // when
        underTest.save(userGiven);

        // then
        Optional<User> optUser = underTest.findByUsername("demo");
        assertThat(optUser).isPresent().hasValueSatisfying(user -> {
            assertThat(user.getUsername()).isEqualTo(userGiven.getUsername());
        });
    }

    @Test
    @Order(3)
    public void itShouldUpdateExistingUser() {
        // given
        User userGiven = new User(
            null,
            "demo",
            "demo1@gmail.com",
            "1234"
        );

        // when
        underTest.save(userGiven);

        // then
        // Actualizacion de email y password del usuario usuario
        int rowsAffected = underTest.updateUser(
            userGiven.getId(),
            "demo@gmail.com",
            "123456"
        );

        assertEquals(1, rowsAffected);
    }
}