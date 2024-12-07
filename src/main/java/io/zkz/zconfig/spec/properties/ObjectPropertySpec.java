package io.zkz.zconfig.spec.properties;

import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static io.zkz.zconfig.utils.ErrorUtils.tagError;

public class ObjectPropertySpec<T> extends BasicPropertySpec<T> {
    private final List<PropertySpec<?, ?>> properties = new ArrayList<>();

    public ObjectPropertySpec(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<T> defaultValueProvider,
        @NotNull List<Validator<T>> validators,
        @NotNull List<PropertySpec<?, ?>> properties,
        @NotNull Class<T> type
    ) {
        super(key, comment, optional, defaultValueProvider, validators, type);
        this.properties.addAll(properties);
    }

    public ObjectPropertySpec<Object> untyped(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Object> defaultValueProvider,
        @NotNull List<Validator<Object>> validators,
        @NotNull List<PropertySpec<?, ?>> properties
    ) {
        return new ObjectPropertySpec<>(key, comment, optional, defaultValueProvider, validators, properties, Object.class);
    }

    @Override
    public @Nullable String validate(Object value) {
        if (value instanceof Map<?, ?> map) {
            // Map
            for (PropertySpec<?, ?> property : properties) {
                Object mapValue = map.get(property.getKey());
                if (mapValue == null) {
                    if (property.isOptional() || property.hasDefaultValue()) {
                        continue;
                    } else {
                        return tagError(property.getKey(), "Value for key is missing/null");
                    }
                }

                String error = property.validateObject(mapValue);
                if (error != null) {
                    return tagError(property.getKey(), error);
                }
            }
        } else {
            // Object
            for (PropertySpec<?, ?> property : properties) {
                try {
                    Field field = value.getClass().getDeclaredField(property.getKey());
                    field.setAccessible(true);
                    Object mapValue = field.get(value);
                    if (mapValue == null) {
                        if (property.isOptional() || property.hasDefaultValue()) {
                            continue;
                        } else {
                            return tagError(property.getKey(), "Value for key is missing/null");
                        }
                    }

                    String error = property.validateObject(mapValue);
                    if (error != null) {
                        return tagError(property.getKey(), error);
                    }
                } catch (NoSuchFieldException e) {
                    return tagError(property.getKey(), "Key does not exist on type '%s'".formatted(value.getClass().getName()));
                } catch (IllegalAccessException | InaccessibleObjectException e) {
                    return tagError(property.getKey(), "Key is inaccessible on type '%s'".formatted(value.getClass().getName()));
                }
            }
        }

        return null;
    }

    @Override
    public @Nullable String validateObject(Object value) {
        if (this.getActualType().isInstance(value)) {
            return validate(value);
        } else {
            return "Actual type '%s' for is not compatible with expected type '%s'".formatted(value.getClass().getName(), this.getActualType().getName());
        }
    }
}
