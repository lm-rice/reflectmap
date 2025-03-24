package com.reflectmap.core;

import com.reflectmap.exception.ReflectMapException;

import java.util.function.BiConsumer;

public final class ReflectMapDestinationLambdaStore extends ClassValue<BiConsumer<Object, Object>> {

    private final Class<?> srcType;

    public ReflectMapDestinationLambdaStore(Class<?> srcType) {
        this.srcType = srcType;
    }

    @Override
    protected BiConsumer<Object, Object> computeValue(Class<?> dstType) {
        try {
            return AnnotationDrivenCompilerStrategy.compile(srcType, dstType);
        } catch (ReflectMapException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException("Failed to compute mapping from " + srcType + " to " + dstType, e);
        }
    }

}
