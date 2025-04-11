package com.reflectmap.internal.compiler.metafactory;

import java.lang.invoke.*;
import java.util.function.BiConsumer;

public class InvokedBiConsumerFactory {

    /**
     * Corresponds to {@link #of(MethodHandle)}.
     */
    private static final MethodType FACTORY_TYPE = MethodType.methodType(BiConsumer.class, MethodHandle.class);

    // The call site is created using a cached private lookup for the inner class.
    private static final CallSite CALL_SITE;
    static {
        try {
            CALL_SITE = LambdaMetafactory.metafactory(
                    PrivateLookupUtils.privateLookupIn(InvokedBiConsumer.class),
                    InvokedBiConsumer.METHOD_NAME,
                    FACTORY_TYPE,
                    InvokedBiConsumer.METHOD_TYPE.erase(),
                    MethodHandles.exactInvoker(InvokedBiConsumer.METHOD_TYPE),
                    InvokedBiConsumer.METHOD_TYPE
            );
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private InvokedBiConsumerFactory() {}

    /**
     * Creates a BiConsumer from the provided MethodHandle.
     *
     * <p>The provided MethodHandle should conform to the signature (Object, Object)void.
     * The resulting BiConsumer will dispatch to that handle via a lambda generated
     * by the LambdaMetafactory.</p>
     *
     * @param handle a MethodHandle to be invoked when the BiConsumer is used
     * @param <T> the type of the first argument
     * @param <U> the type of the second argument
     * @return a BiConsumer that delegates to the provided MethodHandle
     * @throws Throwable if the dynamic invocation fails
     */
    @SuppressWarnings("unchecked")
    public static <T, U> BiConsumer<T, U> of(MethodHandle handle) throws Throwable {
        return (BiConsumer<T, U>) CALL_SITE.getTarget().invokeExact(handle);
    }

    /**
     * The inner class provides a stable method signature for the LambdaMetafactory
     * and helps produce cleaner stack traces. Its sole purpose is to declare a static
     * method matching the BiConsumer.accept(Object, Object) signature.
     */
    private static final class InvokedBiConsumer {

        private InvokedBiConsumer() {}

        /**
         * The name of {@link #accept(Object, Object)}.
         */
        static final String METHOD_NAME = "accept";

        /**
         * Corresponds to {@link #accept(Object, Object)}.
         */
        static final MethodType METHOD_TYPE = MethodType.methodType(void.class, Object.class, Object.class);

        /**
         * The accept method is never intended to be called directly. Instead, it acts as a
         * target method for the LambdaMetafactory to generate a lambda. If ever invoked,
         * it will throw an exception.
         *
         * @param t the first argument
         * @param u the second argument
         */
        @SuppressWarnings("unused")
        public static void accept(Object t, Object u) {
            throw new UnsupportedOperationException("This method should never be called directly.");
        }

    }
}
