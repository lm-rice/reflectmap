package com.reflectmap.core;

import java.lang.invoke.MethodHandle;

public record ReflectMappingInstruction(MethodHandle getter, MethodHandle setter, Class<?> candidateType) {
}
