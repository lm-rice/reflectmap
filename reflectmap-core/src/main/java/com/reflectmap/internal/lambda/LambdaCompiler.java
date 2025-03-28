package com.reflectmap.internal.lambda;

import com.reflectmap.core.ReflectMappingInstruction;

import java.lang.invoke.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class LambdaCompiler {

    private LambdaCompiler() {}

    /**
     * Compiles a list of resolved mapping instructions into a single BiConsumer.
     * Builds a guard chain (using MethodHandles.guardWithTest) in reverse order,
     * then compiles the resulting MethodHandle into an inlinable lambda.
     *
     * @param instruction The list of mapping instructions for one destination field.
     * @return a BiConsumer that performs the mapping for this field
     * @throws Throwable if a method handle invocation fails.
     */
    public static BiConsumer<Object, Object> compile(ReflectMappingInstruction instruction) throws Throwable {
        MethodHandle getter = instruction.getter();
        MethodHandle setter = instruction.setter();
        MethodHandle handle = MethodHandles.insertArguments(LambdaHandleLookupTable.PERFORM_COPY.getHandle(), 0, getter, setter);
        return LambdaConverter.convert(handle);
    }

    /**
     * TODO Update
     * Recursively compiles an array of BiConsumers into one composite BiConsumer
     * using a balanced binary tree.
     * <p>
     * Instead of chaining consumers sequentially with BiConsumer.andThen() (which would yield a long, linear chain),
     * we divide the list into two roughly equal halves at every recursive step. The result is a composite lambda with
     * a logarithmic call stack depth. With reduced depth and therefore smaller lambdas, we support the ability of JIT
     * to inline and optimize composite lambdas.
     *
     * @param consumers a list of consumers
     * @return a composite lambda which executes each consumer sequentially.
     * @throws Throwable if a method handle invocation fails.
     */

    public static BiConsumer<Object, Object> compile(List<BiConsumer<Object, Object>> consumers) throws Throwable {
        if (consumers.isEmpty()) {
            // TODO: Probably don't need to do this; just cache the consumer globally.
            return LambdaConverter.convert(LambdaHandleLookupTable.NO_OP.getHandle());
        }

        while (consumers.size() > 1) {
            List<BiConsumer<Object, Object>> nextRound = new ArrayList<>();
            for (int i = 0; i < consumers.size(); i += 2) {
                if (i + 1 < consumers.size()) {
                    nextRound.add(compile(consumers.get(i), consumers.get(i + 1)));
                } else {
                    nextRound.add(consumers.get(i));
                }
            }
            consumers = nextRound;
        }
        return consumers.get(0);
    }


    /**
     * Combines two BiConsumers into one composite BiConsumer.
     * <p>
     * We avoid the use of BiConsumer.andThen() because it creates a new BiConsumer wrapper
     * on each invocation. This can result in deeply nested stack frames, especially when
     * reflectively copying a large number of fields, and thereby degrade the ability of JIT
     * to inline these lambdas and aggressively optimize them.
     * <p>
     * To work around this, we directly compose the two BiConsumers using {@link MethodHandles}
     * and the {@link LambdaMetafactory} as a single fused lambda with no intermediate indirection.
     *
     * @param a the first BiConsumer.
     * @param b the second BiConsumer.
     * @return a composite BiConsumer the first BiConsumer followed by the second.
     * @throws Throwable if a method handle invocation fails.
     */
    private static BiConsumer<Object, Object> compile(BiConsumer<Object, Object> a, BiConsumer<Object, Object> b) throws Throwable {
        return LambdaConverter.convert(MethodHandles.insertArguments(LambdaHandleLookupTable.COMPOSE.getHandle(), 0, a, b));
    }

}
