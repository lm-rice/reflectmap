package com.reflectmap.exception;

public class FieldNotFoundException extends ReflectMapException {

    public FieldNotFoundException(String className, String fieldName) {
        super(String.format("Failed to find field: %s.%s", className, fieldName));
    }

    public FieldNotFoundException(String className) {
        super("Failed to find any suitable fields in class: " + className);
    }
}
