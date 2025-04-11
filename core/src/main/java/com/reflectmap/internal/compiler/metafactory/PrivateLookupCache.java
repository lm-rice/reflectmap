package com.reflectmap.internal.compiler.metafactory;

import java.lang.invoke.MethodHandles;

final class PrivateLookupCache extends ClassValue<MethodHandles.Lookup> {

    static final PrivateLookupCache INSTANCE = new PrivateLookupCache();

    @Override
    protected MethodHandles.Lookup computeValue(Class<?> type) {
        return PrivateLookupUtils.privateLookupIn(type);
    }

    private PrivateLookupCache() {}

}
