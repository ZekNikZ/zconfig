package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.spec.properties.ObjectPropertySpec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ObjectPropertySpecBuilder<T> extends PropertySpecBuilder<T, T> {
    private final List<PropertySpec<?, ?>> properties = new ArrayList<>();
    private final Class<T> type;

    public ObjectPropertySpecBuilder(String key, Class<T> type, @NotNull List<PropertySpec<?, ?>> properties) {
        super(key);
        this.type = type;
        this.properties.addAll(properties);
    }

    public ObjectPropertySpecBuilder<T> addProperty(Function<Types, PropertySpecBuilder<?, ?>> builder) {
        this.properties.add(builder.apply(new PropertySpecBuilder.Types()).build());
        return this;
    }

    @Override
    public ObjectPropertySpec<T> build() {
        return new ObjectPropertySpec<>(
            this.key,
            this.comment,
            this.optional,
            this.defaultValueSupplier,
            this.validators,
            this.properties,
            this.type
        );
    }
}
