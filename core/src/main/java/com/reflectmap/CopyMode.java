package com.reflectmap;

import com.reflectmap.internal.compiler.Compilers;
import com.reflectmap.internal.CompiledLambdaStore;

import java.util.function.BiConsumer;

public enum CopyMode {
    /**
     * Attempt to copy by all available modes.
     */
    ALL(null),
    /**
     * Attempt to copy only to fields annotated by {@code FieldMapping}.
     */
    ANNOTATION_DRIVEN(new CompiledLambdaStore(Compilers.ANNOTATION_DRIVEN)),
    /**
     * Attempt to copy only to fields with the same name in the source and destination class.
     */
    DIRECT_COPY(new CompiledLambdaStore(Compilers.DIRECT_COPY));

    final CompiledLambdaStore store;

    CopyMode(CompiledLambdaStore store) {
        this.store = store;
    }

    BiConsumer<Object, Object> get(Class<?> src, Class<?> dst) {
        return store.get(src).get(dst);
    }
}
