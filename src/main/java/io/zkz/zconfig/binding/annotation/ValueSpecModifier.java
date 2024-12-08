package io.zkz.zconfig.binding.annotation;

import io.zkz.zconfig.binding.PropertySpecModifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows modifying the spec of the value of a list/map.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueSpecModifier {
    Class<? extends PropertySpecModifier> value();
}

