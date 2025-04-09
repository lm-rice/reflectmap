package com.reflectmap.internal.lambda.compiler;

public final class Compilers {

    // todo introduce copy MODE parameter for direct copy vs annotation driven vs all maybe

    public static final Compiler ANNOTATION_DRIVEN = new AnnotationDrivenLambdaCompiler();
    public static final Compiler DIRECT_COPY = new DirectCopyLambdaCompiler();

    private Compilers() {}

}
