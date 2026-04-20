package com.example.demo.exception;

public class AccessDeniedException extends BusinessException {
	
    public AccessDeniedException(String message) {
        super("ACCESS_DENIED", message);
    }
}
