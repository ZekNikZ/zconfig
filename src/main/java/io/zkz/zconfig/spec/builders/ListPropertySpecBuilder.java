package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.spec.properties.ListPropertySpec;

import java.util.List;

@SuppressWarnings("rawtypes")
public class ListPropertySpecBuilder<T> extends PropertySpecBuilder<List, List> {
    private final PropertySpec<?, T> valueSpec;

    public ListPropertySpecBuilder(String key, PropertySpec<?, T> valueSpec) {
        super(key);
        this.valueSpec = valueSpec;
    }

    public ListPropertySpecBuilder<T> min(int min) {
        this.validators.add(l -> l.size() >= min ? null : "Length '%d' is out of acceptable range: %d <= value".formatted(l.size(), min));
        return this;
    }

    public ListPropertySpecBuilder<T> max(int max) {
        this.validators.add(l -> l.size() <= max ? null : "Length '%d' is out of acceptable range: value <= %d".formatted(l.size(), max));
        return this;
    }

    public ListPropertySpecBuilder<T> range(int min, int max) {
        this.validators.add(l -> l.size() >= min && l.size() <= max ? null : "Length '%d' is out of acceptable range: %d <= value <= %d".formatted(l.size(), min, max));
        return this;
    }

    @Override
    public ListPropertySpec<T> build() {
        return new ListPropertySpec<>(
            this.key,
            this.comment,
            this.optional,
            this.defaultValueSupplier,
            this.validators,
            this.valueSpec
        );
    }
}
