package com.ibank.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

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
        userGiven = underTest.findByUsername("demo").get();

        // Actualizacion de email y password del usuario usuario
        int rowsAffected = underTest.updateUser(
            userGiven.getId(),
            "demo@gmail.com",
            "123456"
        );

        assertEquals(1, rowsAffected);
    }
}