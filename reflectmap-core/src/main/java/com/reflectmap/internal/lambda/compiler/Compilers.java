package com.reflectmap.internal.lambda.compiler;

public final class Compilers {

    private Compilers() {}

    public static final Compiler ANNOTATION_DRIVEN = new AnnotationDrivenLambdaCompiler();
    public static final Compiler DIRECT_COPY = new DirectCopyLambdaCompiler();

}
