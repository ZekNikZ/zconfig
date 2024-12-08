package io.zkz.zconfig.spec.properties;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class BasicPropertySpec<SerializedType, ActualType> implements PropertySpec<SerializedType, ActualType> {
    private final @NotNull String key;
    private final @NotNull String storageKey;
    private final @Nullable String comment;
    private final boolean optional;
    private final @Nullable Supplier<ActualType> defaultValueProvider;
    private final List<Validator<ActualType>> validators = new ArrayList<>();

    private final Class<SerializedType> serializedType;
    private final Class<ActualType> actualType;
    private final Serializer<SerializedType, ActualType> serializer;

    public BasicPropertySpec(
        @NotNull String key,
        @Nullable String storageKey,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<ActualType> defaultValueProvider,
        @NotNull Collection<? extends Validator<ActualType>> validators,
        @NotNull Class<SerializedType> serializedType,
        @NotNull Class<ActualType> actualType,
        @NotNull Serializer<SerializedType, ActualType> serializer
    ) {
        this.key = key;
        this.storageKey = storageKey != null ? storageKey : key;
        this.comment = comment;
        this.optional = optional;
        this.defaultValueProvider = defaultValueProvider;
        this.serializedType = serializedType;
        this.actualType = actualType;
        this.serializer = serializer;
        this.validators.addAll(validators);
    }

    @Override
    public @NotNull Class<SerializedType> getSerializedType() {
        return this.serializedType;
    }

    @Override
    public @NotNull Class<ActualType> getActualType() {
        return this.actualType;
    }

    @Override
    public @NotNull Serializer<SerializedType, ActualType> getSerializer() {
        return this.serializer;
    }

    @Override
    public @NotNull String getKey() {
        return this.key;
    }

    @Override
    public @NotNull String getStorageKey() {
        return this.storageKey;
    }

    @Override
    public @Nullable String getComment() {
        return this.comment;
    }

    @Override
    public boolean isOptional() {
        return this.optional;
    }

    @Override
    public boolean hasDefaultValue() {
        return this.defaultValueProvider != null;
    }

    @Override
    public ActualType getDefaultValue() {
        return Objects.requireNonNull(this.defaultValueProvider).get();
    }

    @Override
    public List<Validator<ActualType>> getValidators() {
        return this.validators;
    }

    @Override
    public @Nullable String validate(ActualType value) {
        for (Validator<ActualType> validator : this.validators) {
            String error = validator.validate(value);
            if (error != null) {
                return error;
            }
        }

        return null;
    }
}
