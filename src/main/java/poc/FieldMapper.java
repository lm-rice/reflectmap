package poc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * FieldMapper precomputes a “map plan” for each destination class.
 * For every destination field annotated with {@code @FieldMapping},
 * it finds candidate source fields (optionally via a container field) and
 * caches a compiled mapper (a BiConsumer) for fast, zero‐allocation mapping.
 *
 * <p>This version uses several techniques to maximize performance:
 * <ul>
 *   <li>
 *     <strong>Precomputation and Inlining:</strong> Reflective lookups and MethodHandles
 *     are performed once during plan creation. The mapping operations are compiled via
 *     LambdaMetafactory into an inlinable lambda.
 *   </li>
 *   <li>
 *     <strong>Elimination of Per‐Call Iteration:</strong> Instead of iterating over an array of mappers,
 *     all field mappings are fused into a single composite BiConsumer.
 *   </li>
 *   <li>
 *     <strong>Balanced Composition via Binary Tree:</strong> The individual lambdas are combined into a balanced binary tree,
 *     ensuring a shallow call stack for optimal JIT inlining.
 *   </li>
 *   <li>
 *     <strong>Cache Locality and Zero‐GC:</strong> The composite mapper is cached per destination class via ClassValue,
 *     so runtime mapping is executed in a single, inlinable call with no per‐call allocations.
 *   </li>
 * </ul>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FieldMapper {

    // A lookup instance for creating MethodHandles.
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    // Cache of computed mappers per destination class.
    private static final ClassValue<BiConsumer<Object, Object>> mapperCache = new ClassValue<BiConsumer<Object, Object>>() {
        @Override
        protected BiConsumer<Object, Object> computeValue(Class<?> type) {
            return buildMapper(type);
        }
    };

    // Mapping from primitive types to their corresponding wrapper classes.
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS = new HashMap<>();
    static {
        PRIMITIVE_WRAPPERS.put(boolean.class, Boolean.class);
        PRIMITIVE_WRAPPERS.put(byte.class, Byte.class);
        PRIMITIVE_WRAPPERS.put(char.class, Character.class);
        PRIMITIVE_WRAPPERS.put(short.class, Short.class);
        PRIMITIVE_WRAPPERS.put(int.class, Integer.class);
        PRIMITIVE_WRAPPERS.put(long.class, Long.class);
        PRIMITIVE_WRAPPERS.put(float.class, Float.class);
        PRIMITIVE_WRAPPERS.put(double.class, Double.class);
    }

    /**
     * Helper class holding a candidate’s getter, setter, and candidate type.
     */
    @RequiredArgsConstructor
    private static final class AccessorPair {
        final MethodHandle getter;
        final MethodHandle setter;
        final Class<?> candidateType;
    }

    /**
     * Static helper MethodHandles.
     * <ul>
     *   <li>
     *     PERFORM_COPY: Invokes a getter on the source and then a setter on the destination.
     *   </li>
     *   <li>
     *     IS_CANDIDATE_MH: Checks whether the source is an instance of the candidate type.
     *   </li>
     *   <li>
     *     NO_OP: A no‐op handle used as a chain terminator.
     *   </li>
     * </ul>
     */
    private static final MethodHandle PERFORM_COPY;
    private static final MethodHandle IS_CANDIDATE_MH;
    private static final MethodHandle NO_OP;
    static {
        try {
            PERFORM_COPY = LOOKUP.findStatic(
                    FieldMapper.class,
                    "performCopy",
                    MethodType.methodType(void.class, MethodHandle.class, MethodHandle.class, Object.class, Object.class)
            );
            IS_CANDIDATE_MH = LOOKUP.findStatic(
                    FieldMapper.class,
                    "isCandidate",
                    MethodType.methodType(boolean.class, Class.class, Object.class)
            );
            NO_OP = LOOKUP.findStatic(
                    FieldMapper.class,
                    "noOp",
                    MethodType.methodType(void.class, Object.class, Object.class)
            );
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Cached CallSite for generating composite lambdas.
    private static final CallSite CALL_SITE;
    static {
        MethodType mapMethodType = MethodType.methodType(void.class, Object.class, Object.class);
        try {
            CALL_SITE = LambdaMetafactory.metafactory(
                    LOOKUP,
                    "accept",
                    MethodType.methodType(BiConsumer.class, MethodHandle.class),
                    mapMethodType.erase(),
                    MethodHandles.exactInvoker(mapMethodType),
                    mapMethodType
            );
        } catch (LambdaConversionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Performs the copy for a candidate:
     * Calls the provided getter on the source and then the setter on the destination.
     */
    private static void performCopy(MethodHandle getter, MethodHandle setter, Object src, Object dst) throws Throwable {
        Object value = getter.invoke(src);
        setter.invoke(dst, value);
    }

    /**
     * Tests whether the source is an instance of the candidate type.
     */
    private static boolean isCandidate(Class<?> candidate, Object src) {
        return candidate.isInstance(src);
    }

    /**
     * No‐op operation for when no candidate matches.
     */
    private static void noOp(Object src, Object dst) {
        // Intentionally empty.
    }

    /**
     * Public API: Copies values from the source instance to the destination instance.
     * The composite mapper is cached per destination class.
     *
     * @param dstClass the destination class to scan for {@code @FieldMapping} annotations
     * @param src      the source instance from which values are read
     * @param dst      the destination instance to which values are written
     */
    public static void map(Class<?> dstClass, Object src, Object dst) {
        mapperCache.get(dstClass).accept(src, dst);
    }

    /**
     * Builds a composite mapper for the given destination class by scanning its fields
     * for {@code @FieldMapping} annotations.
     *
     * @param dstClass the destination class to build a mapper for
     * @return a composite BiConsumer representing all field mappings.
     */
    private static BiConsumer<Object, Object> buildMapper(Class<?> dstClass) {
        List<BiConsumer<Object, Object>> mapFunctions = new ArrayList<>();
        Field[] dstFields = dstClass.getDeclaredFields();
        for (Field dstField : dstFields) {
            FieldMapping mapping = dstField.getAnnotation(FieldMapping.class);
            if (mapping != null) {
                dstField.setAccessible(true);
                List<AccessorPair> candidates = new ArrayList<>();
                for (Class<?> candidateType : mapping.types()) {
                    try {
                        MethodHandle getter = createGetter(candidateType, mapping);
                        Class<?> srcFieldType = getSourceFieldType(candidateType, mapping);
                        if (!isTypeCompatible(dstField.getType(), srcFieldType)) {
                            throw new IllegalArgumentException(String.format(
                                    "Incompatible field types: %s cannot be assigned to %s in field '%s'",
                                    srcFieldType.getName(),
                                    dstField.getType().getName(),
                                    dstField.getName()));
                        }
                        MethodHandle setter = LOOKUP.unreflectSetter(dstField);
                        candidates.add(new AccessorPair(getter, setter, candidateType));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException("Failed to build mapper for field: " + dstField.getName(), e);
                    }
                }
                if (!candidates.isEmpty()) {
                    try {
                        BiConsumer<Object, Object> mapFn = compileFunction(candidates);
                        mapFunctions.add(mapFn);
                    } catch (Throwable t) {
                        throw new RuntimeException("Failed to compile lambda for field: " + dstField.getName(), t);
                    }
                }
            }
        }
        // Combine individual mappers into one composite mapper using balanced binary tree composition.
        BiConsumer<Object, Object> composite;
        try {
            @SuppressWarnings("unchecked")
            BiConsumer<Object, Object>[] mappersArray = mapFunctions.toArray(new BiConsumer[0]);
            composite = combineBiConsumers(mappersArray, 0, mappersArray.length);
        } catch (Throwable t) {
            throw new RuntimeException("Failed to build composite mapper", t);
        }
        return composite;
    }

    /**
     * Recursively combines an array of BiConsumers into one composite BiConsumer
     * using a balanced binary tree.
     *
     * @param consumers the array of BiConsumers
     * @param start     the starting index (inclusive)
     * @param end       the ending index (exclusive)
     * @return a composite BiConsumer that calls all consumers in sequence.
     * @throws Throwable if a method handle invocation fails.
     */
    private static BiConsumer<Object, Object> combineBiConsumers(BiConsumer<Object, Object>[] consumers, int start, int end) throws Throwable {
        int count = end - start;
        if (count == 0) {
            return (src, dst) -> {}; // No-op.
        }
        if (count == 1) {
            return consumers[start];
        }
        int mid = start + (count >> 1);
        BiConsumer<Object, Object> left = combineBiConsumers(consumers, start, mid);
        BiConsumer<Object, Object> right = combineBiConsumers(consumers, mid, end);
        return combineTwo(left, right);
    }

    /**
     * Combines two BiConsumers into one composite BiConsumer without using .andThen().
     * This binds the two consumers to a helper method via MethodHandles.
     *
     * @param a the first BiConsumer.
     * @param b the second BiConsumer.
     * @return a composite BiConsumer that calls a followed by b.
     * @throws Throwable if a method handle invocation fails.
     */
    private static BiConsumer<Object, Object> combineTwo(BiConsumer<Object, Object> a, BiConsumer<Object, Object> b) throws Throwable {
        MethodHandle compositeMH = LOOKUP.findStatic(
                FieldMapper.class,
                "compositeCall",
                MethodType.methodType(void.class, BiConsumer.class, BiConsumer.class, Object.class, Object.class)
        );
        MethodHandle bound = MethodHandles.insertArguments(compositeMH, 0, a, b)
                .asType(MethodType.methodType(void.class, Object.class, Object.class));
        return (BiConsumer<Object, Object>) CALL_SITE.getTarget().invokeExact(bound);
    }

    /**
     * Helper method used by combineTwo.
     * Calls two BiConsumers sequentially.
     *
     * @param a   the first BiConsumer.
     * @param b   the second BiConsumer.
     * @param src the source object.
     * @param dst the destination object.
     */
    private static void compositeCall(BiConsumer<Object, Object> a, BiConsumer<Object, Object> b, Object src, Object dst) {
        a.accept(src, dst);
        b.accept(src, dst);
    }

    /**
     * Compiles a chain of candidate mappings into a single BiConsumer.
     * Builds a guard chain (using MethodHandles.guardWithTest) in reverse order,
     * then compiles the resulting MethodHandle into an inlinable lambda.
     *
     * @param candidates the list of candidate mappings for one destination field.
     * @return a BiConsumer that performs the mapping for this field.
     * @throws Throwable if a method handle invocation fails.
     */
    private static BiConsumer<Object, Object> compileFunction(List<AccessorPair> candidates) throws Throwable {
        MethodHandle chain = NO_OP; // Terminal fallback.
        for (int i = candidates.size() - 1; i >= 0; i--) {
            chain = buildMethodChain(candidates.get(i), chain);
        }
        @SuppressWarnings("unchecked")
        BiConsumer<Object, Object> f = (BiConsumer<Object, Object>) CALL_SITE.getTarget().invokeExact(chain);
        return f;
    }

    /**
     * Builds a guard chain for a candidate mapping.
     * Tests if the source is an instance of the candidate type; if so, performs the copy; otherwise, falls back.
     *
     * @param candidate the candidate AccessorPair.
     * @param fallback  the fallback MethodHandle.
     * @return a MethodHandle representing the guarded mapping operation.
     */
    private static MethodHandle buildMethodChain(AccessorPair candidate, MethodHandle fallback) {
        MethodHandle op = MethodHandles.insertArguments(PERFORM_COPY, 0, candidate.getter, candidate.setter)
                .asType(MethodType.methodType(void.class, Object.class, Object.class));
        MethodHandle test = MethodHandles.insertArguments(IS_CANDIDATE_MH, 0, candidate.candidateType);
        test = MethodHandles.dropArguments(test, 1, Object.class);
        return MethodHandles.guardWithTest(test, op, fallback);
    }

    /**
     * Creates a getter MethodHandle for a given candidate type and FieldMapping.
     * If a container field is specified, the getter first accesses the container, then the nested field.
     *
     * @param candidateType the candidate source type.
     * @param mapping       the FieldMapping annotation.
     * @return a MethodHandle for the getter.
     * @throws NoSuchFieldException   if a field cannot be found.
     * @throws IllegalAccessException if access to the field is denied.
     */
    private static MethodHandle createGetter(Class<?> candidateType, FieldMapping mapping)
            throws NoSuchFieldException, IllegalAccessException {
        if (!mapping.containerField().isEmpty()) {
            Field containerField = candidateType.getDeclaredField(mapping.containerField());
            containerField.setAccessible(true);
            MethodHandle containerGetter = LOOKUP.unreflectGetter(containerField);
            Field nestedField = containerField.getType().getDeclaredField(mapping.fieldName());
            nestedField.setAccessible(true);
            MethodHandle nestedGetter = LOOKUP.unreflectGetter(nestedField);
            return MethodHandles.collectArguments(nestedGetter, 0, containerGetter);
        } else {
            Field srcField = candidateType.getDeclaredField(mapping.fieldName());
            srcField.setAccessible(true);
            return LOOKUP.unreflectGetter(srcField);
        }
    }

    /**
     * Determines the type of the source field for a candidate mapping.
     * Returns the nested field type if a container field is specified; otherwise, returns the direct field type.
     *
     * @param candidateType the candidate source type.
     * @param mapping       the FieldMapping annotation.
     * @return the Class representing the source field type.
     * @throws NoSuchFieldException if the field cannot be found.
     */
    private static Class<?> getSourceFieldType(Class<?> candidateType, FieldMapping mapping)
            throws NoSuchFieldException {
        if (!mapping.containerField().isEmpty()) {
            Field containerField = candidateType.getDeclaredField(mapping.containerField());
            containerField.setAccessible(true);
            Field nestedField = containerField.getType().getDeclaredField(mapping.fieldName());
            nestedField.setAccessible(true);
            return nestedField.getType();
        } else {
            Field srcField = candidateType.getDeclaredField(mapping.fieldName());
            srcField.setAccessible(true);
            return srcField.getType();
        }
    }

    /**
     * Checks whether a source type is compatible with a destination type,
     * taking into account primitive-to-wrapper conversions.
     *
     * @param destType the destination field type.
     * @param srcType  the source field type.
     * @return true if srcType can be assigned to destType; false otherwise.
     */
    private static boolean isTypeCompatible(Class<?> destType, Class<?> srcType) {
        if (destType.isAssignableFrom(srcType)) {
            return true;
        }
        if (srcType.isPrimitive()) {
            return destType.isAssignableFrom(wrap(srcType));
        }
        if (destType.isPrimitive()) {
            return wrap(destType).isAssignableFrom(srcType);
        }
        return false;
    }

    /**
     * Returns the wrapper class for a given primitive type.
     *
     * @param primitive the primitive type.
     * @return the corresponding wrapper class.
     */
    private static Class<?> wrap(Class<?> primitive) {
        Class<?> wrapper = PRIMITIVE_WRAPPERS.get(primitive);
        if (wrapper == null) {
            throw new IllegalArgumentException("Type " + primitive.getName() + " is not a primitive type");
        }
        return wrapper;
    }
}