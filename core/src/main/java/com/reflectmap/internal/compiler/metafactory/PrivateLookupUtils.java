package com.reflectmap.internal.compiler.metafactory;

import java.lang.invoke.MethodHandles;

final class PrivateLookupUtils {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    public static MethodHandles.Lookup privateLookupIn(Class<?> cls) {
        try {
            return MethodHandles.privateLookupIn(cls, LOOKUP);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private PrivateLookupUtils() {}


}
