package io.zkz.zconfig.spec.properties;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

public class PrimitivePropertySpec<T> extends BasicPropertySpec<T, T> {
    public PrimitivePropertySpec(
        @NotNull String key,
        @Nullable String storageKey,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<T> defaultValueProvider,
        @NotNull Collection<? extends Validator<T>> validators,
        @NotNull Class<T> type
    ) {
        super(
            key,
            storageKey,
            comment,
            optional,
            defaultValueProvider,
            validators,
            type,
            type,
            Serializer.identity()
        );
    }

    public static PrimitivePropertySpec<String> String(
        @NotNull String key,
        @Nullable String storageKey,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<String> defaultValueProvider,
        @NotNull Collection<? extends Validator<String>> validators
    ) {
        return new PrimitivePropertySpec<>(key, storageKey, comment, optional, defaultValueProvider, validators, String.class);
    }

    public static PrimitivePropertySpec<Integer> Integer(
        @NotNull String key,
        @Nullable String storageKey,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Integer> defaultValueProvider,
        @NotNull Collection<? extends Validator<Integer>> validators
    ) {
        return new PrimitivePropertySpec<>(key, storageKey, comment, optional, defaultValueProvider, validators, Integer.class);
    }

    public static PrimitivePropertySpec<Double> Double(
        @NotNull String key,
        @Nullable String storageKey,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Double> defaultValueProvider,
        @NotNull Collection<? extends Validator<Double>> validators
    ) {
        return new PrimitivePropertySpec<>(key, storageKey, comment, optional, defaultValueProvider, validators, Double.class);
    }

    public static PrimitivePropertySpec<Boolean> Boolean(
        @NotNull String key,
        @Nullable String storageKey,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Boolean> defaultValueProvider,
        @NotNull Collection<? extends Validator<Boolean>> validators
    ) {
        return new PrimitivePropertySpec<>(key, storageKey, comment, optional, defaultValueProvider, validators, Boolean.class);
    }
}
