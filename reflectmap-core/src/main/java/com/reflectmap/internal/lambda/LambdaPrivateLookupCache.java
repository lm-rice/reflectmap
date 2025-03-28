package com.reflectmap.internal.lambda;

import java.lang.invoke.MethodHandles;

final class LambdaPrivateLookupCache extends ClassValue<MethodHandles.Lookup> {

    static final LambdaPrivateLookupCache INSTANCE = new LambdaPrivateLookupCache();

    private LambdaPrivateLookupCache() {}

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    @Override
    protected MethodHandles.Lookup computeValue(Class<?> type) {
        try {
            return MethodHandles.privateLookupIn(type, LOOKUP);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
