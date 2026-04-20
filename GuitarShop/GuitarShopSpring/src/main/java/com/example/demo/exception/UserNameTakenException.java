package com.example.demo.exception;

public class UserNameTakenException extends BusinessException {
	
    public UserNameTakenException(String name) {
        super("USER_NAME_TAKEN", "username: " + name + " is taken");
    }
}
