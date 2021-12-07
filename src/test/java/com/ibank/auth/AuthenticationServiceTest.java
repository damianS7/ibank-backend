package com.ibank.auth;

import com.ibank.auth.exception.AuthenticationFailedException;
import com.ibank.auth.http.AuthenticationRequest;
import com.ibank.auth.http.AuthenticationResponse;
import com.ibank.user.User;
import com.ibank.user.UserRepository;
import com.ibank.utils.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AuthenticationServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    private AuthenticationService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AuthenticationService(authenticationManager);
        // insertar un usuario en la db
        userRepository.save(
            new User(null, "demo", "demo@gmail.com", PasswordEncoder.encode("demo"))
        );
    }

    @Test
    @Transactional
    void shouldFailWhenIncorrectPassword() {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo77");

        // when
        // then
        assertThrows(AuthenticationFailedException.class, () -> {
            underTest.login(authRequest);
        });
    }

    @Test
    @Transactional
    void shouldLoginSuccessfully() {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo");

        // when
        // then
        assertDoesNotThrow(() -> {
            AuthenticationResponse authResponse = underTest.login(authRequest);
            log.info(authResponse.toString());
        });
    }

}