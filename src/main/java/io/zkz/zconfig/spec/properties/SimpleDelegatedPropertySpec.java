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

public abstract class SimpleDelegatedPropertySpec<SerializedType, ActualType> implements PropertySpec<SerializedType, ActualType> {
    private final @NotNull Class<ActualType> actualType;
    private final @NotNull Serializer<SerializedType, ActualType> serializer;
    private final @NotNull PropertySpec<SerializedType, SerializedType> delegate;
    private final @Nullable Supplier<ActualType> defaultValueProvider;
    private final List<Validator<ActualType>> validators = new ArrayList<>();

    public SimpleDelegatedPropertySpec(
        @NotNull Class<ActualType> actualType,
        @NotNull Serializer<SerializedType, ActualType> serializer,
        @Nullable Supplier<ActualType> defaultValueProvider,
        @NotNull List<Validator<ActualType>> validators,
        @NotNull DelegateFactory<SerializedType> delegateFactory
    ) {
        this.actualType = actualType;
        this.serializer = serializer;
        this.defaultValueProvider = defaultValueProvider;
        this.validators.addAll(validators);
        this.delegate = delegateFactory.build(
            defaultValueProvider != null ? () -> serializer.serialize(defaultValueProvider.get()) : null,
            this.validators.stream().map(validator -> new Validator<SerializedType>() {
                @Override
                public @Nullable String validate(SerializedType value) {
                    return validator.validate(serializer.deserialize(value));
                }
            }).toList()
        );
    }

    @Override
    public @NotNull String getKey() {
        return this.delegate.getKey();
    }

    @Override
    public @Nullable String getComment() {
        return this.delegate.getComment();
    }

    @Override
    public boolean isOptional() {
        return this.delegate.isOptional();
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
    public Collection<Validator<ActualType>> getValidators() {
        return this.validators;
    }

    @Override
    public @NotNull Class<SerializedType> getSerializedType() {
        return this.delegate.getSerializedType();
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
    public @Nullable String validate(ActualType value) {
        return this.delegate.validate(this.serializer.serialize(value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable String validateObject(Object value) {
        if (this.actualType.isInstance(value)) {
            return this.delegate.validateObject(this.serializer.serialize((ActualType) value));
        } else {
            return "Actual type '%s' is not compatible with expected type '%s'".formatted(value.getClass().getName(), this.getActualType().getName());
        }
    }

    @FunctionalInterface
    public interface DelegateFactory<T> {
        PropertySpec<T, T> build(
            @Nullable Supplier<T> defaultValueProvider,
            Collection<? extends Validator<T>> validators
        );
    }
}
