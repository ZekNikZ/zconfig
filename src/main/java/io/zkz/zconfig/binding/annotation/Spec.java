package io.zkz.zconfig.binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the spec of this object property.
 * If its fields are annotated, this will simply be the object class itself.
 * If the type's IConverter is registered with the ConfigLoader, this can be omitted.
 * Optionally, use {@link SerializedBy} instead for custom conversions.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Spec {
    Class<?> value();
}
