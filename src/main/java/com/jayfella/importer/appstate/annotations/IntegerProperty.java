package com.jayfella.importer.appstate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IntegerProperty {
    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;
    int step() default 1;

    String tab() default "";
}
