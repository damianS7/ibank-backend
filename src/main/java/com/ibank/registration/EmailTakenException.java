package com.ibank.registration;

public class EmailTakenException extends Exception {
    public EmailTakenException(String s) {
        super(s);
    }
}
