package com.example.demo.exception;

public class InvalidBackUpPathException extends BusinessException {
	
    public InvalidBackUpPathException(String message) {
        super("INVALID_BACK_PATH", message);
    }
}
