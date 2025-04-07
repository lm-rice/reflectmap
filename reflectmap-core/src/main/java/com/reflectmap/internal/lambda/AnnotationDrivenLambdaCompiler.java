package com.reflectmap.internal.lambda;

import com.reflectmap.annotation.FieldMapping;
import com.reflectmap.core.ReflectMappingInstruction;
import com.reflectmap.core.utils.ReflectMapTypeUtils;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

final class AnnotationDrivenLambdaCompiler extends AbstractLambdaCompiler {

    AnnotationDrivenLambdaCompiler() {}

    @Override
    protected ReflectMappingInstruction createInstruction(Class<?> srcType, Class<?> dstType, Field dstField) throws IllegalAccessException {
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

        String[] srcFieldNames = annotation.srcFieldName().trim().split("\\.");
        String[] dstFieldNames = dstField.getName().trim().split("\\.");

        MethodHandle getter = createGetterHandle(srcType, srcFieldNames);
        MethodHandle setter = createSetterHandle(dstType, dstFieldNames);

        return super.createInstruction(getter, setter, srcType, dstType);
    }
}
