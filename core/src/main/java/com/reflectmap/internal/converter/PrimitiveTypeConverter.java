package com.reflectmap.internal.converter;

public final class PrimitiveTypeConverter {

    private PrimitiveTypeConverter() {}

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

    public static Class<?> convert(Class<?> cls) {
        if (cls.isPrimitive()) {
            return PRIMITIVE_WRAPPERS.get(cls);
        }
        return cls;
    }

}
