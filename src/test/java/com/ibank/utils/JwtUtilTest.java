package com.ibank.utils;

import com.ibank.user.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class JwtUtilTest {

    @Test
    void shouldGenerateTokenWithSuccess() {
        // given
        User user = new User(
            6L,
            "demo",
            "demo@gmail.com",
            "1234"
        );

        // when
        String token = JwtUtil.createToken(user);

        // then
        log.info(token);
        assertNotNull(token);
    }

    @Test
    void shouldConvertTokenToUser() {
        // given
        User user = new User(
            6L,
            "demo",
            "demo@gmail.com",
            "1234"
        );
        String token = JwtUtil.createToken(user);

        // when
        User userFromToken = JwtUtil.parseToken(token);

        // then
        assertNotNull(userFromToken);
        log.info(userFromToken.toString());
    }

    @Test
    void shouldFailWhenTokenInvalid() {
        // given
        String token = "invalid token";

        // when
        // then
        assertThrows(MalformedJwtException.class, () -> {
            JwtUtil.parseToken(token);
        });
    }

    @Test
    void shouldFailWhenTokenNotProperlySigned() {
        // given
        String tokenHeader = "eyJhbGciOiJIUzUxMiJ9";
        String tokenPayload = "eyJpZCI6NiwidXNlcm5hbWUiOiJkZW1vIiwicGFzc3dvcmQiOiIxMjM0IiwiZW1haWwiOiJkZW1vQGdtYWlsLmNvbSJ9";

        // Firma valida
        String validTokenSignature = "EpJ53_AJL-Wjx3RkBAkK4bbWaRAEgraeiQx88J4veO41M56F_0hUakyiM0GDByVtywa0k4zTjJOuxLe8SDq_GQ";

        // Firma invalida
        String invalidTokenSignature = "dummy signature should fail!";

        // then
        assertThrows(SignatureException.class, () -> {
            JwtUtil.parseToken(tokenHeader + "." + tokenPayload + "." + invalidTokenSignature);
        });

        assertDoesNotThrow(() -> {
            JwtUtil.parseToken(tokenHeader + "." + tokenPayload + "." + validTokenSignature);
        });
    }

    @Test
    void shouldFailWhenTokenExpired() {
        // given
        // when
        // then
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJpZCI6NiwidXNlcm5hbWUiOiJkZW1vIiwicGFzc3dvcmQiOiIxMjM0IiwiZW1haWwiOiJkZW1vQGdtYWlsLmNvbSIsImV4cCI6MTYzODg5ODEwNn0.DUeTT30OBduUNVHu27SWVI00bo4-g56dk5Cx90OgLy0AVJfWmtDcgtRzUZaF7mOtMWpoz60OBBKMJ_nCb8LE7A";
        assertThrows(ExpiredJwtException.class, () -> {
            JwtUtil.parseToken(token);
        });
    }

}