package com.reflectmap.internal.compiler;

import com.reflectmap.annotation.FieldMapping;
import com.reflectmap.internal.util.TypeUtils;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

final class AnnotationDrivenLambdaCompiler extends AbstractLambdaCompiler {

    AnnotationDrivenLambdaCompiler() {}

    @Override
    protected LambdaCompilerInstruction createInstruction(Class<?> srcType, Class<?> dstType, Field dstField) throws IllegalAccessException {
        FieldMapping[] annotations = dstField.getDeclaredAnnotationsByType(FieldMapping.class);

        if (annotations.length == 0) {
            return null;
        }

        FieldMapping annotation = null;
        for (FieldMapping candidate : annotations) {
            if (TypeUtils.isTypeCompatible(srcType, candidate.srcType())) {
                annotation = candidate;
                break;
            }
        }

        if (annotation == null) {
            return null;
        }

        String[] srcFieldNames = annotation.srcFieldName().trim().split("\\.");
        String[] dstFieldNames = dstField.getName().trim().split("\\.");

        MethodHandle getter = createGetterHandle(srcType, srcFieldNames);
        MethodHandle setter = createSetterHandle(dstType, dstFieldNames);

        return new LambdaCompilerInstruction(getter, setter, srcType, dstType);
    }
}
