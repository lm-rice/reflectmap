package com.reflectmap.core;

import com.reflectmap.annotation.FieldMapping;
import com.reflectmap.core.utils.ReflectMapTypeUtils;
import com.reflectmap.exception.FieldNotFoundException;
import com.reflectmap.exception.IncompatibleFieldTypesException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public final class AnnotationDrivenCompilerStrategy {

    public static BiConsumer<Object, Object> compile(Class<?> srcType, Class<?> dstType) throws Throwable {
        List<BiConsumer<Object, Object>> intermediateLambdas = new ArrayList<>();
        for (Field dstField : dstType.getDeclaredFields()) {
            ReflectMappingInstruction instruction = createInstructionFromAnnotations(srcType, dstType, dstField);
            if (instruction != null) {
                intermediateLambdas.add(ReflectMapLambdaCompiler.compile(instruction));
            }
        }

        return ReflectMapLambdaCompiler.compile(intermediateLambdas);
    }

    private static ReflectMappingInstruction createInstructionFromAnnotations(Class<?> srcType, Class<?> dstType, Field dstField) {
        FieldMapping[] annotations = dstField.getDeclaredAnnotationsByType(FieldMapping.class);

        if (annotations.length == 0) {
            return null;
        }

        FieldMapping annotation = null;
        for (FieldMapping candidate : annotations) {
            if (ReflectMapTypeUtils.isTypeCompatible(srcType, candidate.srcType())) {
                annotation = candidate;
                break;
            }
        }

        if (annotation == null) {
            return null;
        }

        try {
            return createInstruction(srcType, annotation.srcFieldName(), dstType, dstField.getName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to derive mapping for field due to access control.", e);
        }
    }

    private static ReflectMappingInstruction createInstruction(Class<?> srcType, String srcFieldName, Class<?> dstType, String dstFieldName) throws IllegalAccessException {

        String[] srcFieldNames = srcFieldName.trim().split("\\.");
        String[] dstFieldNames = dstFieldName.trim().split("\\.");

        MethodHandle getter = createGetterHandle(srcType, srcFieldNames);
        MethodHandle setter = createSetterHandle(dstType, dstFieldNames);

        Class<?> getterFieldType = getter.type().returnType();
        Class<?> setterFieldType = setter.type().lastParameterType();

        if (!ReflectMapTypeUtils.isTypeCompatible(getterFieldType, setterFieldType)) {
            throw new IncompatibleFieldTypesException(srcType, srcFieldName, dstType, dstFieldName);
        }

        return new ReflectMappingInstruction(getter, setter, srcType);
    }

    private static MethodHandle createGetterHandle(Class<?> srcRootClass, String... fieldNames) throws IllegalAccessException {
        MethodHandle current = null;
        Class<?> currentClass = srcRootClass;

        try {
            for (String fieldName : fieldNames) {
                Field f = currentClass.getDeclaredField(fieldName);
                MethodHandle getter = ReflectMapLambdaCompiler.findGetterHandle(currentClass, f);

                if (current != null) {
                    current = MethodHandles.filterReturnValue(current, getter);
                } else {
                    current = getter;
                }

                currentClass = f.getType();
            }
        } catch (NoSuchFieldException e) {
            String fmtClassName = srcRootClass.getName();
            String fmtFieldNames = String.join(".", fieldNames);
            throw new FieldNotFoundException(fmtClassName, fmtFieldNames);
        }

        return current;
    }

    private static MethodHandle createSetterHandle(Class<?> dstRootClass, String... fieldNames) throws IllegalAccessException {
        try {
            if (fieldNames.length == 1) {
                Field f = dstRootClass.getDeclaredField(fieldNames[0]);
                return ReflectMapLambdaCompiler.findSetterHandle(dstRootClass, f);
            } else {
                String[] nestedFieldNames = Arrays.copyOf(fieldNames, fieldNames.length - 1);
                MethodHandle getter = createGetterHandle(dstRootClass, nestedFieldNames);
                Class<?> getterRetType = getter.type().returnType();

                String lastFieldName = fieldNames[fieldNames.length - 1];
                Field f = getterRetType.getDeclaredField(lastFieldName);
                MethodHandle setter = ReflectMapLambdaCompiler.findSetterHandle(getterRetType, f);

                return MethodHandles.foldArguments(setter, getter);
            }
        } catch (NoSuchFieldException e) {
            String fmtClassName = dstRootClass.getName();
            String fmtFieldNames = String.join(".", fieldNames);
            throw new FieldNotFoundException(fmtClassName, fmtFieldNames);
        }
    }
}
