package io.zkz.zconfig.binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the type of values of this property (usually List or Map) in cases
 * that reflection is unable to determine it. If providing a binded type,
 * use {@link ValueType} instead.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueSpec {
    Class<?> value();
}

