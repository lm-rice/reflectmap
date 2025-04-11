package com.reflectmap.internal.compiler.metafactory;

import java.lang.invoke.*;
import java.util.function.Function;

public final class GetterFunctionFactory {

    private GetterFunctionFactory() {}

    /**
     * Corresponds to {@link #of(MethodHandle)}.
     */
    private static final MethodType FACTORY_TYPE = MethodType.methodType(Function.class, MethodHandle.class);

    private static final CallSite CALL_SITE;
    static {
        try {
            CALL_SITE = LambdaMetafactory.metafactory(
                    PrivateLookupUtils.privateLookupIn(GetterFunction.class),
                    GetterFunction.METHOD_NAME,
                    FACTORY_TYPE,
                    GetterFunction.METHOD_TYPE.erase(),
                    MethodHandles.exactInvoker(GetterFunction.METHOD_TYPE),
                    GetterFunction.METHOD_TYPE
            );
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Converts the given getter MethodHandle to a Function<Object, Object>.
     * The passed getter is adapted to the signature (Object)Object.
     */
    @SuppressWarnings("unchecked")
    public static Function<Object, Object> of(MethodHandle handle) throws Throwable {
        handle = handle.asType(GetterFunction.METHOD_TYPE);
        return (Function<Object, Object>) CALL_SITE.getTarget().invokeExact(handle);
    }

    private static final class GetterFunction {

        private GetterFunction() {}

        /**
         * The name of {@link #apply(Object)}.
         */
        static final String METHOD_NAME = "apply";

        /**
         * The signature of {@link #apply(Object)}.
         */
        static final MethodType METHOD_TYPE = MethodType.methodType(Object.class, Object.class);

        /**
         * This is a placeholder for the target (SAM) method where LambdaMetaFactory will be bind the getter to.
         */
        @SuppressWarnings("unused")
        public static Object apply(Object t) {
            throw new UnsupportedOperationException("This method should never be called directly.");
        }

    }
}
