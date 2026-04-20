package com.example.demo.exception;

public class InvalidPasswordException extends BusinessException {
	
    public InvalidPasswordException(String message) {
        super("IVALID_PASS", message);
    }
}
