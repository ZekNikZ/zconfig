package io.zkz.zconfig.binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restricts the range of legal integer values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IntRange {
    int min() default Integer.MIN_VALUE;

    int max() default Integer.MIN_VALUE;
}
