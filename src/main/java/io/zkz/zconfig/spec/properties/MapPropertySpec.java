package io.zkz.zconfig.spec.properties;

import io.zkz.zconfig.binding.annotation.Exclude;
import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.*;
import java.util.function.Supplier;

import static io.zkz.zconfig.utils.ErrorUtils.tagError;

@SuppressWarnings({"rawtypes"})
public class MapPropertySpec<T> extends PrimitivePropertySpec<Map> {
    final PropertySpec<?, T> valueSpec;

    public MapPropertySpec(
        @NotNull String key,
        @Nullable String storageKey,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Map> defaultValueProvider,
        @NotNull List<Validator<Map>> validators,
        @NotNull PropertySpec<?, T> valueSpec
    ) {
        super(
            key,
            storageKey,
            comment,
            optional,
            defaultValueProvider,
            validators,
            Map.class
        );
        this.valueSpec = valueSpec;
    }

    public static <T> MapPropertySpec<T> typed(
        @NotNull String key,
        @Nullable String storageKey,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Map<?, T>> defaultValueProvider,
        @NotNull List<Validator<Map<?, T>>> validators,
        @NotNull PropertySpec<?, T> valueSpec
    ) {
        return new MapPropertySpec<>(
            key,
            storageKey,
            comment,
            optional,
            () -> defaultValueProvider != null ? defaultValueProvider.get() : null,
            validators.stream().map(MapPropertySpec::unwrapValidator).toList(),
            valueSpec
        );
    }

    @Override
    public @Nullable String validate(Map map) {
        String error = super.validate(map);
        if (error != null) {
            return error;
        }

        for (Object key : map.keySet()) {
            Object value = map.get(key);
            error = valueSpec.validateObject(value);
            if (error != null) {
                return tagError(key.toString(), error);
            }
        }

        return null;
    }

    private static <T> Validator<Map> unwrapValidator(Validator<Map<?, T>> validator) {
        return validator::validate;
    }

    @Override
    public @Nullable String validateObject(Object obj) {
        if (obj instanceof Map map) {
            return validate(map);
        } else if (obj != null) {
            Field[] fields = obj.getClass().getDeclaredFields();
            Map<String, Object> map = new HashMap<>();
            for (Field field : fields) {
                if (field.getDeclaredAnnotation(Exclude.class) != null) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(obj));
                } catch (IllegalAccessException | InaccessibleObjectException e) {
                    return tagError(field.getName(), "Key is inaccessible on type '%s'".formatted(obj.getClass().getName()));
                }
            }
            return validate(map);
        } else {
            return "Actual type 'null' for is not compatible with expected type '%s'".formatted(this.getActualType().getName());
        }
    }
}
