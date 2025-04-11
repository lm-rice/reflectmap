package com.reflectmap.internal.compiler;

import com.reflectmap.internal.util.TypeUtils;
import com.reflectmap.exception.IncompatibleFieldTypesException;

import java.lang.invoke.MethodHandle;

public record LambdaCompilerInstruction(MethodHandle getter, MethodHandle setter, Class<?> srcType) {

    public LambdaCompilerInstruction(MethodHandle getter, MethodHandle setter, Class<?> srcType, Class<?> dstType) {
        this(getter, setter, srcType);

        Class<?> getterFieldType = getter.type().returnType();
        Class<?> setterFieldType = setter.type().lastParameterType();

        if (!TypeUtils.isTypeCompatible(getterFieldType, setterFieldType)) {
            throw new IncompatibleFieldTypesException(srcType, srcType.getName(), dstType, dstType.getName());
        }
    }
}
