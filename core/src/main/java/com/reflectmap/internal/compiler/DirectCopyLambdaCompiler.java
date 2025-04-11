package com.reflectmap.internal.compiler;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

final class DirectCopyLambdaCompiler extends AbstractLambdaCompiler {

    @Override
    protected LambdaCompilerInstruction createInstruction(Class<?> srcType, Class<?> dstType, Field dstField) throws IllegalAccessException {
        MethodHandle getter = createGetterHandle(srcType, dstField.getName());
        MethodHandle setter = createSetterHandle(dstType, dstField);

        if (getter == null || setter == null) {
            return null;
        }

        return new LambdaCompilerInstruction(getter, setter, srcType, dstType);
    }


    @Override
    protected MethodHandle createGetterHandle(Class<?> srcType, String srcFieldName) throws IllegalAccessException {
        try {
            return super.createGetterHandle(srcType, srcFieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Override
    protected MethodHandle createSetterHandle(Class<?> dstType, Field dstField) throws IllegalAccessException {
        try {
            return super.createSetterHandle(dstType, dstField);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
