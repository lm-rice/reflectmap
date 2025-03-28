package com.reflectmap.internal.lambda;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
final class LambdaHandleMethods {

    private LambdaHandleMethods() {}

    static final MethodType PERFORM_COPY_TYPE = MethodType.methodType(void.class, MethodHandle.class, MethodHandle.class, Object.class, Object.class);
    static final MethodType COMPOSE_TYPE = MethodType.methodType(void.class, BiConsumer.class, BiConsumer.class, Object.class, Object.class);
    static final MethodType NOOP_TYPE = MethodType.methodType(void.class, Object.class, Object.class);

    /**
     * Performs the copy for a compiled lambda.
     * Calls the provided getter on the source and then the setter on the destination.
     */
    public static void performCopy(MethodHandle getter, MethodHandle setter, Object src, Object dst) throws Throwable {
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
    public static void compose(BiConsumer<Object, Object> a, BiConsumer<Object, Object> b, Object src, Object dst) {
        a.accept(src, dst);
        b.accept(src, dst);
    }

    /**
     * No-op method with two parameters.
     */
    public static void noOp(Object src, Object dst) {}

}
