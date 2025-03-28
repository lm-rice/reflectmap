package com.reflectmap.core;

public final class ReflectMapSourceLambdaStore extends ClassValue<ReflectMapDestinationLambdaStore> {

    @Override
    protected ReflectMapDestinationLambdaStore computeValue(Class<?> srcType) {
        return new ReflectMapDestinationLambdaStore(srcType);
    }

}
