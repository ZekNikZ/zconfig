package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.spec.properties.BasicPropertySpec;
import org.jetbrains.annotations.NotNull;

public class BasicPropertySpecBuilder<SerializedType, ActualType> extends PropertySpecBuilder<SerializedType, ActualType> {
    private final @NotNull Class<SerializedType> serializedType;
    private final @NotNull Class<ActualType> actualType;
    private final @NotNull Serializer<SerializedType, ActualType> serializer;

    public BasicPropertySpecBuilder(
        @NotNull String key,
        @NotNull Class<SerializedType> serializedType,
        @NotNull Class<ActualType> actualType,
        @NotNull Serializer<SerializedType, ActualType> serializer
    ) {
        super(key);
        this.serializedType = serializedType;
        this.actualType = actualType;
        this.serializer = serializer;
    }

    @Override
    public BasicPropertySpec<SerializedType, ActualType> build() {
        return new BasicPropertySpec<>(
            this.key,
            this.storageKey,
            this.comment,
            this.optional,
            this.defaultValueSupplier,
            this.validators,
            this.serializedType,
            this.actualType,
            this.serializer
        );
    }
}
