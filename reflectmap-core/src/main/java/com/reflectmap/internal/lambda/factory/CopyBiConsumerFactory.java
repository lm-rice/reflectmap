package com.reflectmap.internal.lambda.factory;

import com.reflectmap.internal.lambda.compiler.LambdaCompilerInstruction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BiConsumer;

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
        MethodHandle getter = instruction.getter();
        MethodHandle setter = instruction.setter();
        MethodHandle copier = MethodHandles.insertArguments(CopyConsumer.HANDLE, 0, getter, setter);
        return InvokedBiConsumerFactory.of(copier);
    }

    private static final class CopyConsumer {

        private CopyConsumer() {}

        static final MethodHandle HANDLE;
        static {
            Class<?> memberClass = CopyConsumer.class;
            String methodName = "accept";
            MethodType consumerType = MethodType.methodType(void.class, MethodHandle.class, MethodHandle.class, Object.class, Object.class);
            HANDLE = MethodHandleFactory.of(memberClass, methodName, consumerType);
        }

        /**
         * Compose invoke a setter and getter method handle.
         */
        @SuppressWarnings("unused")
        public static void accept(MethodHandle getter, MethodHandle setter, Object src, Object dst) throws Throwable {
            Object value = getter.invoke(src);
            setter.invoke(dst, value);
        }

    }
}
