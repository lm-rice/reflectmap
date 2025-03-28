package com.reflectmap;


import com.reflectmap.core.ReflectMapSourceLambdaStore;

public final class ReflectMap {

    private static final ReflectMapSourceLambdaStore store = new ReflectMapSourceLambdaStore();

    private ReflectMap() {}

    public static void map(Object src, Class<?> srcType, Object dst, Class<?> dstType) {
        store.get(srcType).get(dstType).accept(src, dst);
    }
}
