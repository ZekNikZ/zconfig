package io.zkz.zconfig.spec;

import io.zkz.zconfig.spec.builders.ConfigSpecBuilder;
import io.zkz.zconfig.spec.properties.ObjectPropertySpec;

import java.util.List;

public class ConfigSpec extends ObjectPropertySpec<Object> {
    public ConfigSpec(List<PropertySpec<?, ?>> properties) {
        super(
            "root",
            null,
            false,
            null,
            List.of(),
            properties,
            Object.class
        );
    }

    public static ConfigSpecBuilder builder() {
        return new ConfigSpecBuilder();
    }

    public static ConfigSpec fromAnnotations(Class<?> clazz) {
        return null;
    }
}