package io.zkz.zconfig.binding.annotation;

import java.lang.annotation.*;
import java.util.function.Supplier;

/**
 * Provides a default value to the property. Alternatively, just set the
 * default value as the initialized value of the field and use {@link DefaultValue}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DefaultValueSupplier {
    Class<? extends Supplier<?>> value();
}
