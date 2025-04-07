package com.reflectmap;

import com.reflectmap.internal.lambda.Compilers;
import com.reflectmap.internal.lambda.LambdaFactory;

import java.util.function.BiConsumer;

public enum CopyMode {
    /**
     * Attempt to copy by all available modes.
     */
    ALL(null),
    /**
     * Attempt to copy only to fields annotated by {@code FieldMapping}.
     */
    ANNOTATION_DRIVEN(new LambdaFactory(Compilers.ANNOTATION_DRIVEN)),
    /**
     * Attempt to copy only to fields with the same name in the source and destination class.
     */
    DIRECT_COPY(new LambdaFactory(Compilers.DIRECT_COPY));

    final LambdaFactory factory;

    CopyMode(LambdaFactory factory) {
        this.factory = factory;
    }

    BiConsumer<Object, Object> get(Class<?> src, Class<?> dst) {
        return factory.get(src).get(dst);
    }
}
