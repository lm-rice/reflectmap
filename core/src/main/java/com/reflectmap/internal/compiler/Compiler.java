package com.reflectmap.internal.compiler;

import java.util.function.BiConsumer;


public interface Compiler {

    BiConsumer<Object, Object> compile(Class<?> srcType, Class<?> dstType) throws Throwable;

}
