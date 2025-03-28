package com.reflectmap.internal.lambda;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

enum LambdaHandleLookupTable {

    PERFORM_COPY("performCopy", LambdaHandleMethods.PERFORM_COPY_TYPE),
    COMPOSE("compose", LambdaHandleMethods.COMPOSE_TYPE),
    NO_OP("noOp", LambdaHandleMethods.NOOP_TYPE);

    private final MethodHandle handle;

    LambdaHandleLookupTable(String methodName, MethodType type) {
        try {
            MethodHandles.Lookup lookup = LambdaPrivateLookupCache.INSTANCE.get(LambdaHandleLookupTable.class);
            this.handle = lookup.findStatic(LambdaHandleMethods.class, methodName, type);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    MethodHandle getHandle() {
        return handle;
    }

}
