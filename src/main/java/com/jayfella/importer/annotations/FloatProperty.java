package com.jayfella.importer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FloatProperty {

    float min() default Integer.MIN_VALUE;
    float max() default Integer.MAX_VALUE;
    float step() default 1;
    boolean editable() default false;

}
