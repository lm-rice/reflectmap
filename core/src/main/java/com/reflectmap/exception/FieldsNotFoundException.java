package com.reflectmap.exception;

public class FieldsNotFoundException extends ReflectMapException {

    public FieldsNotFoundException(Class<?> srcType, Class<?> dstType) {
        super(String.format("No valid fields were found for a ReflectMap invocation [%s -> %s].", srcType.getName(), dstType.getName()));
    }
}
