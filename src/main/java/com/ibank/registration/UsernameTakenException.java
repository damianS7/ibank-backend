package com.ibank.registration;

public class UsernameTakenException extends Exception {
    public UsernameTakenException(String s) {
        super(s);
    }
}
