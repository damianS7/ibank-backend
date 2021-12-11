package com.ibank.auth;

import com.ibank.auth.exception.AuthenticationFailedException;
import com.ibank.auth.http.AuthenticationRequest;
import com.ibank.auth.http.AuthenticationResponse;
import com.ibank.auth.http.TokenValidationRequest;
import com.ibank.auth.http.TokenValidationResponse;
import com.ibank.user.User;
import com.ibank.user.UserRepository;
import com.ibank.utils.JwtUtil;
import com.ibank.utils.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;

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
    void shouldNotLoginWithInvalidCredentials() {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo77");

        // when
        // then
        assertThrows(AuthenticationFailedException.class, () -> {
            underTest.login(authRequest);
        });
    }

    @Test
    void shouldLoginWithValidCredentials() {
        // given
        AuthenticationRequest authRequest = new AuthenticationRequest("demo", "demo");

        // when
        // then
        assertDoesNotThrow(() -> {
            AuthenticationResponse authResponse = underTest.login(authRequest);
            log.info(authResponse.toString());
        });
    }

    @Test
    void shouldUpdateToken() {
        // given
        User givenUser = new User(
            1L, "demo", "demo@gmail.com", "1234"
        );
        String token = JwtUtil.createToken(givenUser);
        log.info(token);

        TokenValidationRequest tokenValidationRequest = new TokenValidationRequest(
            givenUser.getUsername(), token
        );

        // when
        // then
        assertDoesNotThrow(() -> {
            TokenValidationResponse tokenValidationResponse = underTest.validateToken(tokenValidationRequest);
            log.info(tokenValidationResponse.token);
        });
    }

}