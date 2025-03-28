package com.reflectmap.internal.lambda;

import java.lang.invoke.*;
import java.util.function.BiConsumer;

final class LambdaConverter {

    private static final MethodType CONSUMER_TYPE = MethodType.methodType(void.class, Object.class, Object.class);
    private static final MethodType FACTORY_TYPE = MethodType.methodType(BiConsumer.class, MethodHandle.class);
    private static final CallSite CALL_SITE;

    // Caching the call site with an erased consumer type tricks JIT into optimizing every accept() invocation together,
    // rather than only optimizing on repeatedly used call sites.
    static {
        try {
            CALL_SITE = LambdaMetafactory.metafactory(
                    LambdaPrivateLookupCache.INSTANCE.get(LambdaCompiler.class),
                    "accept",
                    FACTORY_TYPE,
                    CONSUMER_TYPE.erase(),
                    MethodHandles.exactInvoker(CONSUMER_TYPE),
                    CONSUMER_TYPE
            );
        } catch (LambdaConversionException e) {
            throw new RuntimeException(e);
        }
    }

    private LambdaConverter() {}

    @SuppressWarnings("unchecked")
    static BiConsumer<Object, Object> convert(MethodHandle handle) throws Throwable {
        return (BiConsumer<Object, Object>) CALL_SITE.getTarget().invokeExact(handle);
    }

}
