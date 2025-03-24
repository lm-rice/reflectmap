package com.reflectmap.core;

import java.lang.invoke.MethodHandles;

final class ReflectMapLookupCache extends ClassValue<MethodHandles.Lookup> {

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
