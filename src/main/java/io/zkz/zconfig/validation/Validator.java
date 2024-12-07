package io.zkz.zconfig.validation;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface Validator<T> {
    /// Returns null if test is successful, String validation message otherwise
    @Nullable String validate(T value);
}
