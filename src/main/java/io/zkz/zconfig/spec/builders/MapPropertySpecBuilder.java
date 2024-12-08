package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.spec.properties.MapPropertySpec;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class MapPropertySpecBuilder<T> extends PropertySpecBuilder<Map, Map> {
    private final PropertySpec<?, T> valueSpec;

    public MapPropertySpecBuilder(String key, PropertySpec<?, T> valueSpec) {
        super(key);
        this.valueSpec = valueSpec;
    }

    @Override
    public MapPropertySpec<T> build() {
        return new MapPropertySpec<>(
            this.key,
            this.storageKey,
            this.comment,
            this.optional,
            this.defaultValueSupplier,
            this.validators,
            this.valueSpec
        );
    }
}
