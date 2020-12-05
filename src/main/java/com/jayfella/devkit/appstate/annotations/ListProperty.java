package com.jayfella.devkit.appstate.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ListProperty {

  String accessorName();

  ListType listType() default ListType.List;

  String tab() default "";
}
