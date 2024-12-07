package io.zkz.zconfig.spec.properties;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

public class BasicPropertySpec<T> extends AbstractPropertySpec<T, T> {
    public BasicPropertySpec(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<T> defaultValueProvider,
        @NotNull Collection<? extends Validator<T>> validators,
        @NotNull Class<T> type
    ) {
        super(key, comment, optional, defaultValueProvider, validators, type, type, Serializer.identity());
    }

    public static BasicPropertySpec<String> String(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<String> defaultValueProvider,
        @NotNull Collection<? extends Validator<String>> validators
    ) {
        return new BasicPropertySpec<>(key, comment, optional, defaultValueProvider, validators, String.class);
    }

    public static BasicPropertySpec<Integer> Integer(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Integer> defaultValueProvider,
        @NotNull Collection<? extends Validator<Integer>> validators
    ) {
        return new BasicPropertySpec<>(key, comment, optional, defaultValueProvider, validators, Integer.class);
    }

    public static BasicPropertySpec<Double> Double(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Double> defaultValueProvider,
        @NotNull Collection<? extends Validator<Double>> validators
    ) {
        return new BasicPropertySpec<>(key, comment, optional, defaultValueProvider, validators, Double.class);
    }

    public static BasicPropertySpec<Boolean> Boolean(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Boolean> defaultValueProvider,
        @NotNull Collection<? extends Validator<Boolean>> validators
    ) {
        return new BasicPropertySpec<>(key, comment, optional, defaultValueProvider, validators, Boolean.class);
    }
}
