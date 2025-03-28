package com.reflectmap.internal.lambda;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

public class LambdaHandleFactory {

    private static final LambdaPrivateLookupCache LOOKUP_CACHE = LambdaPrivateLookupCache.INSTANCE;

    public static MethodHandle getterHandle(Class<?> cls, Field f) throws NoSuchFieldException, IllegalAccessException {
        return LOOKUP_CACHE.get(cls).findGetter(cls, f.getName(), f.getType());
    }

    public static MethodHandle setterHandle(Class<?> cls, Field f) throws NoSuchFieldException, IllegalAccessException {
        return LOOKUP_CACHE.get(cls).findSetter(cls, f.getName(), f.getType());
    }

}
