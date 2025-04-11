package com.reflectmap;

import java.lang.reflect.InvocationTargetException;

public final class ReflectMap {

    private static final CopyMode DEFAULT_COPY_MODE = CopyMode.ANNOTATION_DRIVEN;

    private ReflectMap() {}

    /**
     * High performance entrypoint to ReflectMap. Creates zero garbage for every run following the first.
     * Allocates a small amount of memory per srcType -> dstType pair.
     */
    public static void map(Object src, Class<?> srcType, Object dst, Class<?> dstType, CopyMode copyMode) {
        copyMode.get(srcType, dstType).accept(src, dst);
    }

    /**
     * Copies values from one object to another using reflection.
     * For full behavior details, see {@link #map(Object, Class, Object, Class, CopyMode)}.
     * @see #map(Object, Class, Object, Class, CopyMode)
     */
    public static void map(Object src, Class<?> srcType, Object dst, Class<?> dstType) {
        map(src, srcType, dst, dstType, DEFAULT_COPY_MODE);
    }

    /**
     * Convenience entrypoint to ReflectMap. Creates garbage in the form of mapped objects.
     * For performance-critical use cases, it's recommended to pool destination objects and call a different map method.
     * @see #map(Object, Class, Object, Class)
     * @see #map(Object, Class, Object, Class, CopyMode)
     */
    public static <S, D> D map(S src, Class<D> dstType, CopyMode copyMode) {
        D dst;
        try {
            dst = dstType.getDeclaredConstructor().newInstance();
            map(src, src.getClass(), dst, dstType, copyMode);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return dst;
    }

    /**
     * Convenience entrypoint to ReflectMap. Creates garbage in the form of mapped objects.
     * For full behavior details, see {@link #map(Object, Class, CopyMode)}.
     * @see #map(Object, Class, CopyMode)
     */
    public static <S, D> D map(S src, Class<D> dstType) {
        D dst;
        try {
            dst = dstType.getDeclaredConstructor().newInstance();
            map(src, src.getClass(), dst, dstType);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return dst;
    }
}
