package com.reflectmap.exception;

public class IncompatibleFieldTypesException extends ReflectMapException {

    public IncompatibleFieldTypesException(Class<?> srcFieldType, String srcFieldName, Class<?> dstFieldType, String dstFieldName) {
        super(String.format("Incompatible field types: Cannot map %s.%s to %s.%s", srcFieldType, srcFieldName, dstFieldType, dstFieldName));
    }
}
