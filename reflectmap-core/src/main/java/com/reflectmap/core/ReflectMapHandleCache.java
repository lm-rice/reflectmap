package com.reflectmap.core;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BiConsumer;

final class ReflectMapHandleCache {

    // Static helper MethodHandles.
    // PERFORM_COPY: Invokes a getter on the source and then a setter on the destination.
    // COMPOSE: Invokes two consumers sequentially to support optimized lambda compilation.
    public final MethodHandle PERFORM_COPY;
    public final MethodHandle COMPOSE;
    public final MethodHandle NO_OP;

    public ReflectMapHandleCache(ReflectMapLookupCache lookupCache) {
        try {
            MethodHandles.Lookup lookup = lookupCache.get(this.getClass());
            PERFORM_COPY = lookup.findStatic(
                    this.getClass(),
                    "performCopy",
                    MethodType.methodType(void.class, MethodHandle.class, MethodHandle.class, Object.class, Object.class)
            );
            COMPOSE = lookup.findStatic(
                    this.getClass(),
                    "compose",
                    MethodType.methodType(void.class, BiConsumer.class, BiConsumer.class, Object.class, Object.class)
            );
            NO_OP = lookup.findStatic(
                    this.getClass(),
                    "noOp",
                    MethodType.methodType(void.class, Object.class, Object.class)
            );
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Performs the copy for a compiled lambda.
     * Calls the provided getter on the source and then the setter on the destination.
     */
    private static void performCopy(MethodHandle getter, MethodHandle setter, Object src, Object dst) throws Throwable {
        Object value = getter.invoke(src);
        setter.invoke(dst, value);
    }

    /**
     * Call two BiConsumers sequentially.
     * @param a     the first BiConsumer.
     * @param b     the second BiConsumer.
     * @param src   the source object.
     * @param dst   the destination object.
     */
    private static void compose(BiConsumer<Object, Object> a, BiConsumer<Object, Object> b, Object src, Object dst) {
        a.accept(src, dst);
        b.accept(src, dst);
    }

    /**
     * Cached no-op method.
     */
    private static void noOp(Object src, Object dst) {}

}
