package com.reflectmap.internal.lambda;

import java.util.function.BiConsumer;


public interface Compiler {

    BiConsumer<Object, Object> compile(Class<?> srcType, Class<?> dstType) throws Throwable;

}
