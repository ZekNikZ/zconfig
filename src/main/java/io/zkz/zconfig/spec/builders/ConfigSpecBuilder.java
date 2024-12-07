package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.spec.ConfigSpec;
import io.zkz.zconfig.spec.PropertySpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ConfigSpecBuilder {
    private final List<PropertySpec<?, ?>> properties = new ArrayList<>();

    public <T> ConfigSpecBuilder addProperty(Function<PropertySpecBuilder.Types, PropertySpecBuilder<?, T>> builder) {
        this.properties.add(builder.apply(new PropertySpecBuilder.Types()).build());
        return this;
    }

    public ConfigSpec build() {
        return new ConfigSpec(this.properties);
    }
}
