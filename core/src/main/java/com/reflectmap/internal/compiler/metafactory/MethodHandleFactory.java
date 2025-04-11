package com.reflectmap.internal.compiler.metafactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public final class MethodHandleFactory {

    private MethodHandleFactory() {}

    public static MethodHandle of(Class<?> memberClass, String methodName, MethodType type) {
        try {
            MethodHandles.Lookup lookup = PrivateLookupCache.INSTANCE.get(memberClass);
            return lookup.findStatic(memberClass, methodName, type);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MethodHandle getter(Class<?> memberClass, Field f) throws NoSuchFieldException, IllegalAccessException {
        return PrivateLookupCache.INSTANCE.get(memberClass).findGetter(memberClass, f.getName(), f.getType());
    }

    public static MethodHandle setter(Class<?> memberClass, Field f) throws NoSuchFieldException, IllegalAccessException {
        return PrivateLookupCache.INSTANCE.get(memberClass).findSetter(memberClass, f.getName(), f.getType());
    }

}
