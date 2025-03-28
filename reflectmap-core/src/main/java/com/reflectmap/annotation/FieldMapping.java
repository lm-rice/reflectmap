package com.reflectmap.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldMapping {
    Class<?> srcType();
    String srcFieldName();
    String dstFieldName() default "";
}
