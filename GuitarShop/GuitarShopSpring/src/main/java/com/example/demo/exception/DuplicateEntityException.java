package com.example.demo.exception;

public class DuplicateEntityException extends BusinessException {
	
    public DuplicateEntityException(String message) {
        super("DUPLICATE", message);
    }
}
