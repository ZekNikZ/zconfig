package io.zkz.zconfig.spec.properties;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

public class BoundPropertySpec<SerializedType, ActualType> extends BasicPropertySpec<SerializedType, ActualType> {
    public BoundPropertySpec(
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
        super(
            key,
            storageKey,
            comment,
            optional,
            defaultValueProvider,
            validators,
            serializedType,
            actualType,
            serializer
        );
    }
}
