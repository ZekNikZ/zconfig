package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.spec.properties.SimpleDelegatedPropertySpec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class SimpleDelegatedPropertySpecBuilder<SerializedType, ActualType> extends PropertySpecBuilder<SerializedType, ActualType> {
    private final @NotNull Class<ActualType> actualType;
    private final @NotNull Serializer<SerializedType, ActualType> serializer;
    private final @NotNull PropertySpecBuilder<SerializedType, SerializedType> delegate;

    protected SimpleDelegatedPropertySpecBuilder(
        @NotNull String key,
        @NotNull Class<ActualType> actualType,
        @NotNull Serializer<SerializedType, ActualType> serializer,
        @NotNull Function<SubTypes, PropertySpecBuilder<SerializedType, SerializedType>> delegateFactory
    ) {
        super(key);
        this.actualType = actualType;
        this.serializer = serializer;
        this.delegate = delegateFactory.apply(new SubTypes());
    }

    @Override
    public SimpleDelegatedPropertySpec<SerializedType, ActualType> build() {
        return new SimpleDelegatedPropertySpec<>(
            this.actualType,
            this.serializer,
            this.defaultValueSupplier,
            this.validators,
            (def, val) -> this.delegate
                .key(this.key)
                .optional(this.optional)
                .comment(this.comment)
                .defaultValue(def)
                .validators(val)
                .build()
        );
    }
}
