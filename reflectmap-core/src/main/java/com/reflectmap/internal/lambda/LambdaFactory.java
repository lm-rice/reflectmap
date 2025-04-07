package com.reflectmap.internal.lambda;

import com.reflectmap.exception.ReflectMapException;

import java.util.function.BiConsumer;

public final class LambdaFactory extends ClassValue<ClassValue<BiConsumer<Object, Object>>> {

    private final Compiler compiler;

    public LambdaFactory(Compiler compiler) {
        this.compiler = compiler;
    }

    @Override
    protected ClassValue<BiConsumer<Object, Object>> computeValue(Class<?> srcType) {
        return new LambdaValueFactory(srcType);
    }

    private final class LambdaValueFactory extends ClassValue<BiConsumer<Object, Object>> {

        private final Class<?> srcType;

        LambdaValueFactory(Class<?> srcType) {
            this.srcType = srcType;
        }

        @Override
        protected BiConsumer<Object, Object> computeValue(Class<?> dstType) {
            try {
                return compiler.compile(srcType, dstType);
            } catch (ReflectMapException e) {
                throw e;
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to derive mapping for field due to access control.", e);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to compute mapping from " + srcType + " to " + dstType, e);
            }
        }
    }

}
