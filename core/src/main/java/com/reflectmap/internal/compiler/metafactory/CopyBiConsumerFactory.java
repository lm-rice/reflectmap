package com.reflectmap.internal.compiler.metafactory;

import com.reflectmap.internal.compiler.LambdaCompilerInstruction;

import java.util.function.BiConsumer;
import java.util.function.Function;

public final class CopyBiConsumerFactory {

    private CopyBiConsumerFactory() {}

    /**
     * Compile a copying consumer from a resolved instruction.
     *
     * @param instruction An instruction resolved by another part of the compiler.
     * @return A consumer that, when executed, performs copies from a source field to a destination field.
     * @throws Throwable Thrown if a method handle invocation fails.
     */
    public static BiConsumer<Object, Object> of(LambdaCompilerInstruction instruction) throws Throwable {
        Function<Object, Object> getter = GetterFunctionFactory.of(instruction.getter());
        BiConsumer<Object, Object> setter = SetterBiConsumerFactory.of(instruction.setter());
        return of(getter, setter);
    }

    /**
     * Compose a copying lambda from the getter and setter lambdas.
     */
    private static BiConsumer<Object, Object> of(Function<Object, Object> getter, BiConsumer<Object, Object> setter) {
        return (src, dst) -> setter.accept(dst, getter.apply(src));
    }
}