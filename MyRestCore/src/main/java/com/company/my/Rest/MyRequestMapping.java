package com.company.my.rest;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * URL mapping.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {
    String name() default "";

    @AliasFor("path")
    String[] value() default {};

    @AliasFor("value")
    String[] path() default {};

    MyRequestMethod[] method() default {};

    String[] params() default {};
}
