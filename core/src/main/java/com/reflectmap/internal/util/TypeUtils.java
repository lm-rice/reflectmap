package com.reflectmap.internal.util;

import com.reflectmap.internal.converter.PrimitiveTypeConverter;

public final class TypeUtils {

    private TypeUtils() {}

    public static boolean isTypeCompatible(Class<?> a, Class<?> b) {
        if (a == null || b == null) return false;

        Class<?> convA = PrimitiveTypeConverter.convert(a);
        Class<?> convB = PrimitiveTypeConverter.convert(b);

        return convA.isAssignableFrom(convB) || b == Object.class
            || convB.isAssignableFrom(convA) || a == Object.class;
    }

}
