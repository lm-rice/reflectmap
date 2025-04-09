package com.reflectmap.internal.lambda.factory;

import java.lang.invoke.MethodHandles;

final class PrivateLookupCache extends ClassValue<MethodHandles.Lookup> {

    static final PrivateLookupCache INSTANCE = new PrivateLookupCache();

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    @Override
    protected MethodHandles.Lookup computeValue(Class<?> type) {
        try {
            return MethodHandles.privateLookupIn(type, LOOKUP);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private PrivateLookupCache() {}

}
