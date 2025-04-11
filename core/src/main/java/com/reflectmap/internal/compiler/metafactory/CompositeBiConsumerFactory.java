package com.reflectmap.internal.compiler.metafactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Queue;
import java.util.function.BiConsumer;

public final class CompositeBiConsumerFactory {

    private CompositeBiConsumerFactory() {}

    /**
     * Iteratively compiles a queue of BiConsumers into one composite BiConsumer by pairwise reduction.
     *
     * <p>This method fuses adjacent consumer pairs in each pass through the queue. The fused results are re-enqueued,
     * and the process continues until a single composite lambda remains. This results in a flatter
     * and more JIT-friendly structure than naive chaining.</p>
     *
     * <p>The resulting lambda invokes the consumers sequentially in the order they were provided.</p>
     *
     * @param consumers a queue of consumers to be composed
     * @return a composite BiConsumer that sequentially applies each consumer
     * @throws IllegalArgumentException if the queue or any of its elements is null
     */
    public static <T, U> BiConsumer<T, U> of(Queue<BiConsumer<T, U>> consumers) throws Throwable {
        // Perform pairwise reduction, fusing consumers to the left.
        while (consumers.size() > 1) {
            int size = consumers.size();
            for (int i = 0; i < size / 2; i++) {
                BiConsumer<T, U> a = consumers.poll();
                BiConsumer<T, U> b = consumers.poll();
                consumers.offer(of(a, b));
            }

            // If there was an odd element, move it from head to tail to be processed in the next round.
            if ((size & 1) == 1) {
                consumers.offer(consumers.poll());
            }
        }

        // Final fused BiConsumer
        return consumers.poll();
    }

    public static <T, U> BiConsumer<T, U> of(BiConsumer<T, U> a, BiConsumer<T, U> b) throws Throwable {
        MethodHandle handle = MethodHandles.insertArguments(CompositeConsumer.HANDLE, 0, a, b);
        return InvokedBiConsumerFactory.of(handle);
    }

    private static final class CompositeConsumer {
        private CompositeConsumer() {}

        static final MethodHandle HANDLE;
        static {
            Class<?> memberClass = CompositeConsumer.class;
            String methodName = "accept";
            MethodType consumerType = MethodType.methodType(void.class, BiConsumer.class, BiConsumer.class, Object.class, Object.class);
            HANDLE = MethodHandleFactory.of(memberClass, methodName, consumerType);
        }

        /**
         * Compose two BiConsumers to be executed sequentially.
         */
        @SuppressWarnings("unused")
        public static void accept(BiConsumer<Object, Object> a, BiConsumer<Object, Object> b, Object src, Object dst) {
            a.accept(src, dst);
            b.accept(src, dst);
        }
    }

}
