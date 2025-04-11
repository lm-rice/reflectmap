package com.reflectmap.internal;

import com.reflectmap.exception.ReflectMapException;
import com.reflectmap.internal.compiler.Compiler;

import java.util.function.BiConsumer;

public final class CompiledLambdaStore extends ClassValue<ClassValue<BiConsumer<Object, Object>>> {

    private final Compiler compiler;

    public CompiledLambdaStore(Compiler compiler) {
        this.compiler = compiler;
    }

    @Override
    protected ClassValue<BiConsumer<Object, Object>> computeValue(Class<?> srcType) {
        return new CompiledLambdaFactory(srcType);
    }

    private final class CompiledLambdaFactory extends ClassValue<BiConsumer<Object, Object>> {

        private final Class<?> srcType;

        CompiledLambdaFactory(Class<?> srcType) {
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
