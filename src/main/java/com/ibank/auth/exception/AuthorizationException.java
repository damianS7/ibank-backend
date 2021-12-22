package com.ibank.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Excepcion para errores de permisos
 */
public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String msg) {
        super(msg);
    }
}
