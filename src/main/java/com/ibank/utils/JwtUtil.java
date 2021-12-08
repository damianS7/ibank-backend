package com.ibank.utils;

import com.ibank.config.SecurityConstants;
import com.ibank.user.User;
import io.jsonwebtoken.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JwtUtil {

    /**
     * Crea un token unico para el usuario indicado
     *
     * @param user Usuario sobre el que se genera el token
     * @return El token generado con los datos de usuario
     */
    public static String createToken(User user) {
        return JwtUtil.createTokenFromUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getEmail());
    }

    /**
     * Crea un token unico para el usuario indicado
     *
     * @return El token generado con los datos de usuario
     */
    public static String createTokenFromUserDetails(Long id, String username, String password, String email) {
        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("username", username);
        claims.put("password", password);
        claims.put("email", email);

        return Jwts.builder()
            .setIssuer(SecurityConstants.ISSUER_INFO)
            .setIssuedAt(new Date())
            .setClaims(claims)
            .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(SecurityConstants.TOKEN_EXPIRATION_MINUTES)))
            //.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1)))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY.getBytes(StandardCharsets.UTF_8))
            .compact();
    }

    /**
     * Convierte un token valido en una instancia de User
     *
     * @param token - El token
     * @return User - Una instancia de User que contiene los datos del usuario contenidos en el token
     * @throws ExpiredJwtException   El token de seguridad ha expirado
     * @throws MalformedJwtException El formato del token no es correcto
     */
    public static User parseToken(String token)
        throws MalformedJwtException, ExpiredJwtException, SignatureException {
        Claims claims = Jwts.parser()
            .setSigningKey(SecurityConstants.SECRET_KEY.getBytes(StandardCharsets.UTF_8))
            .parseClaimsJws(token.replace(SecurityConstants.TOKEN_BEARER_PREFIX, ""))
            .getBody();

        return new User(
            Long.parseLong(claims.get("id").toString()),
            claims.get("username").toString(),
            claims.get("email").toString(),
            claims.get("password").toString()
        );
    }
}

