package com.example.demo.exception;

public class EntityNotFoundException extends BusinessException {
	
    public EntityNotFoundException(String entity, Integer id) {
        super("NOT_FOUND", entity + " with id " + id + " not found");
    }
    
    public EntityNotFoundException(String entity, String name) {
        super("NOT_FOUND", entity + " with name " + name + " not found");
    }
    
    public EntityNotFoundException(String entity, Object id) {
        super("NOT_FOUND", entity + " with id " + id.toString() + " not found");
    }
}
