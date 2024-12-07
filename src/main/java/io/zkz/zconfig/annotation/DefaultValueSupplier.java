package io.zkz.zconfig.annotation;

import java.lang.annotation.*;
import java.util.function.Supplier;

/**
 * Provides a default value to the property. Alternatively, just set the
 * default value as the initialized value of the field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DefaultValueSupplier {
    Class<? extends Supplier<?>> value();

    WhenValue[] when() default {WhenValue.IS_MISSING, WhenValue.IS_NULL};

    enum WhenValue {
        IS_MISSING,
        IS_NULL,
    }
}
