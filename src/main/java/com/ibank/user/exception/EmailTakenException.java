package com.ibank.user.exception;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String s) {
        super(s);
    }
}
