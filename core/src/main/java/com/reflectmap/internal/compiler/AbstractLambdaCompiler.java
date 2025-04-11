package com.reflectmap.internal.compiler;

import com.reflectmap.exception.FieldNotFoundException;
import com.reflectmap.exception.FieldsNotFoundException;
import com.reflectmap.internal.compiler.metafactory.CompositeBiConsumerFactory;
import com.reflectmap.internal.compiler.metafactory.CopyBiConsumerFactory;
import com.reflectmap.internal.compiler.metafactory.MethodHandleFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.function.BiConsumer;

public abstract class AbstractLambdaCompiler implements Compiler {

    @Override
    public BiConsumer<Object, Object> compile(Class<?> srcType, Class<?> dstType) throws Throwable {
        Queue<BiConsumer<Object, Object>> consumers = new ArrayDeque<>();
        for (Field dstField : dstType.getDeclaredFields()) {
            LambdaCompilerInstruction instruction = createInstruction(srcType, dstType, dstField);
            if (instruction != null) {
                consumers.offer(CopyBiConsumerFactory.of(instruction));
            }
        }

        if (consumers.isEmpty()) {
            throw new FieldsNotFoundException(srcType, dstType);
        }

        return CompositeBiConsumerFactory.of(consumers);
    }

    protected abstract LambdaCompilerInstruction createInstruction(Class<?> srcType, Class<?> dstType, Field dstField) throws IllegalAccessException;

    protected MethodHandle createGetterHandle(Class<?> srcType, String srcFieldName) throws NoSuchFieldException, IllegalAccessException {
        Field srcField = srcType.getDeclaredField(srcFieldName);
        return MethodHandleFactory.getter(srcType, srcField);
    }

    protected MethodHandle createGetterHandle(Class<?> srcType, String... srcFieldNames) throws IllegalAccessException {
        if (srcFieldNames.length == 0) {
            return null;
        }

        MethodHandle current = null;
        Class<?> currentType = srcType;

        try {
            for (String fieldName : srcFieldNames) {
                MethodHandle getter = createGetterHandle(currentType, fieldName);

                if (current != null) {
                    current = MethodHandles.filterReturnValue(current, getter);
                } else {
                    current = getter;
                }

                currentType = getter.type().returnType();
            }
        } catch (NoSuchFieldException e) {
            String fmtClassName = currentType.getName();
            String fmtFieldNames = String.join(".", srcFieldNames);
            throw new FieldNotFoundException(fmtClassName, fmtFieldNames);
        }

        return current;
    }

    protected MethodHandle createSetterHandle(Class<?> dstType, Field dstField) throws NoSuchFieldException, IllegalAccessException {
        return MethodHandleFactory.setter(dstType, dstField);
    }

    protected MethodHandle createSetterHandle(Class<?> dstType, String dstFieldName) throws NoSuchFieldException, IllegalAccessException {
        Field dstField = dstType.getDeclaredField(dstFieldName);
        return createSetterHandle(dstType, dstField);
    }

    protected MethodHandle createSetterHandle(Class<?> dstType, String... dstFieldNames) throws IllegalAccessException {
        if (dstFieldNames.length == 0) {
            throw new FieldNotFoundException(dstType.getName());
        }

        try {
            if (dstFieldNames.length == 1) {
                return createSetterHandle(dstType, dstFieldNames[0]);
            }

            String[] nestedFieldNames = Arrays.copyOf(dstFieldNames, dstFieldNames.length - 1);
            MethodHandle getter = createGetterHandle(dstType, nestedFieldNames);
            Class<?> getterRetType = getter.type().returnType();

            String lastFieldName = dstFieldNames[dstFieldNames.length - 1];
            Field f = getterRetType.getDeclaredField(lastFieldName);
            MethodHandle setter = createSetterHandle(getterRetType, f);

            return MethodHandles.foldArguments(setter, getter);
        } catch (NoSuchFieldException e) {
            String fmtClassName = dstType.getName();
            String fmtFieldNames = String.join(".", dstFieldNames);
            throw new FieldNotFoundException(fmtClassName, fmtFieldNames);
        }
    }
}
