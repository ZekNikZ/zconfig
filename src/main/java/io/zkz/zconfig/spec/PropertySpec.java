package io.zkz.zconfig.spec;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface PropertySpec<SerializedType, ActualType> extends Validator<ActualType> {
    @NotNull String getKey();

    @NotNull String getStorageKey();

    @Nullable String getComment();

    boolean isOptional();

    boolean hasDefaultValue();

    ActualType getDefaultValue();

    Collection<Validator<ActualType>> getValidators();

    @NotNull Class<SerializedType> getSerializedType();

    @NotNull Class<ActualType> getActualType();

    @NotNull Serializer<SerializedType, ActualType> getSerializer();

    @SuppressWarnings("unchecked")
    default @Nullable String validateObject(Object value) {
        if (this.getActualType().isInstance(value)) {
            return validate((ActualType) value);
        } else {
            return "Actual type '%s' is not compatible with expected type '%s'".formatted(value.getClass().getName(), this.getActualType().getName());
        }
    }
}
