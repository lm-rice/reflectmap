package com.reflectmap;


import com.reflectmap.core.ReflectMapSourceLambdaStore;

import java.lang.reflect.InvocationTargetException;

public final class ReflectMap {

    private static final ReflectMapSourceLambdaStore store = new ReflectMapSourceLambdaStore();

    private ReflectMap() {}

    /**
     * High performance entrypoint to ReflectMap. Creates zero garbage for every run following the first.
     * Allocates a small amount of memory per srcType -> dstType pair.
     */
    public static void map(Object src, Class<?> srcType, Object dst, Class<?> dstType) {
        store.get(srcType).get(dstType).accept(src, dst);
    }

    /**
     * Convenience entrypoint to ReflectMap which creates garbage in the form of mapped objects.
     * For performance-critical use cases, it is recommended to pool destination objects
     * and call {@code map(Object, Class, Object, Dst)}.
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
