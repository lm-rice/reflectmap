package com.reflectmap.exception;

public class FieldNotFoundException extends ReflectMapException {

    public FieldNotFoundException(String className, String fieldName) {
        super(String.format("Failed to find field: %s.%s", className, fieldName));
    }
}
