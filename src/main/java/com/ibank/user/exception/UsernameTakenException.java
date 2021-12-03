package com.ibank.user.exception;

public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String s) {
        super(s);
    }
}
