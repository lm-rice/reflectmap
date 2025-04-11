package com.reflectmap.internal.compiler.metafactory;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BiConsumer;

public final class SetterBiConsumerFactory {

    private SetterBiConsumerFactory() {}

    /**
     * Corresponds to {@link #of(MethodHandle)}.
     */
    private static final MethodType FACTORY_TYPE = MethodType.methodType(BiConsumer.class, MethodHandle.class);

    private static final CallSite CALL_SITE;
    static {
        try {
            CALL_SITE = LambdaMetafactory.metafactory(
                    PrivateLookupUtils.privateLookupIn(SetterBiConsumerFactory.class),
                    SetterConsumer.METHOD_NAME,
                    FACTORY_TYPE,
                    SetterConsumer.METHOD_TYPE.erase(),
                    MethodHandles.exactInvoker(SetterConsumer.METHOD_TYPE),
                    SetterConsumer.METHOD_TYPE
            );
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Converts the given setter MethodHandle to a BiConsumer<Object, Object>.
     * The setter is adapted to the signature (Object, Object)void.
     */
    @SuppressWarnings("unchecked")
    public static BiConsumer<Object, Object> of(MethodHandle handle) throws Throwable {
        handle = handle.asType(SetterConsumer.METHOD_TYPE);
        return (BiConsumer<Object, Object>) CALL_SITE.getTarget().invokeExact(handle);
    }

    private static final class SetterConsumer {

        /**
         * The name of {@link #accept(Object, Object)}.
         */
        private static final String METHOD_NAME = "accept";

        /**
         * The signature of {@link #accept(Object, Object)}.
         */
        private static final MethodType METHOD_TYPE = MethodType.methodType(void.class, Object.class, Object.class);

        /**
         * This is a placeholder for the target (SAM) method where LambdaMetaFactory will be bind the setter to.
         */
        @SuppressWarnings("unused")
        public static void accept(Object t, Object u) {
            throw new UnsupportedOperationException("This method should never be called directly.");
        }

    }

}