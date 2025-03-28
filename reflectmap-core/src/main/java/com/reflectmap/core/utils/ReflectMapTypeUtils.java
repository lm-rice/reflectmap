package com.reflectmap.core.utils;

public final class ReflectMapTypeUtils {

    private static final ClassValue<Class<?>> PRIMITIVE_WRAPPERS = new ClassValue<>() {
        @Override
        protected Class<?> computeValue(Class<?> type) {
            if (type.equals(boolean.class)) return Boolean.TYPE;
            if (type.equals(byte.class)) return Byte.TYPE;
            if (type.equals(char.class)) return Character.TYPE;
            if (type.equals(short.class)) return Short.TYPE;
            if (type.equals(int.class)) return Integer.TYPE;
            if (type.equals(long.class)) return Long.TYPE;
            if (type.equals(float.class)) return Float.TYPE;
            if (type.equals(double.class)) return Double.TYPE;
            if (type.equals(void.class)) return Void.TYPE;
            return type; // This should never happen.
        }
    };

    // Warm-up the type cache
    static {
        PRIMITIVE_WRAPPERS.get(boolean.class);
        PRIMITIVE_WRAPPERS.get(byte.class);
        PRIMITIVE_WRAPPERS.get(char.class);
        PRIMITIVE_WRAPPERS.get(short.class);
        PRIMITIVE_WRAPPERS.get(int.class);
        PRIMITIVE_WRAPPERS.get(long.class);
        PRIMITIVE_WRAPPERS.get(float.class);
        PRIMITIVE_WRAPPERS.get(double.class);
        PRIMITIVE_WRAPPERS.get(void.class);
    }

    private ReflectMapTypeUtils() {}

    public static boolean isTypeCompatible(Class<?> a, Class<?> b) {
        if (a == null || b == null) return false;

        Class<?> wrappedA = wrap(a);
        Class<?> wrappedB = wrap(b);

        return wrappedA.isAssignableFrom(wrappedB) || b == Object.class
            || wrappedB.isAssignableFrom(wrappedA) || a == Object.class;
    }

    private static Class<?> wrap(Class<?> cls) {
        if (cls.isPrimitive()) {
            return PRIMITIVE_WRAPPERS.get(cls);
        }
        return cls;
    }
}
