package io.zkz.zconfig.annotation;

import io.zkz.zconfig.conversion.Serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the class that will handle converting this object property.
 * Optionally, use {@link Spec} if automatic conversion will do.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Converter {
    Class<? extends Serializer<?, ?>> value();
}
