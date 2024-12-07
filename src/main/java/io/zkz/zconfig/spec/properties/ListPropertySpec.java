package io.zkz.zconfig.spec.properties;

import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static io.zkz.zconfig.utils.ErrorUtils.tagError;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ListPropertySpec<T> extends BasicPropertySpec<List> {
    final PropertySpec<?, T> valueSpec;

    public ListPropertySpec(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<List> defaultValueProvider,
        @NotNull List<Validator<List>> validators,
        @NotNull PropertySpec<?, T> valueSpec
    ) {
        super(
            key,
            comment,
            optional,
            defaultValueProvider,
            validators,
            List.class
        );
        this.valueSpec = valueSpec;
    }

    public static <T> ListPropertySpec<T> typed(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<List<T>> defaultValueProvider,
        @NotNull List<Validator<List<T>>> validators,
        @NotNull PropertySpec<?, T> valueSpec
    ) {
        return new ListPropertySpec<>(
            key,
            comment,
            optional,
            () -> defaultValueProvider != null ? defaultValueProvider.get() : null,
            validators.stream().map(ListPropertySpec::unwrapValidator).toList(),
            valueSpec
        );
    }

    @Override
    public @Nullable String validate(List list) {
        String error = super.validate(list);
        if (error != null) {
            return error;
        }

        for (int i = 0; i < list.size(); i++) {
            error = valueSpec.validateObject(list.get(i));
            if (error != null) {
                return tagError("[%s]".formatted(i), error);
            }
        }

        return null;
    }

    private static <T> Validator<List> unwrapValidator(Validator<List<T>> validator) {
        return validator::validate;
    }

    @Override
    public @Nullable String validateObject(Object value) {
        if (this.getActualType().isInstance(value)) {
            return validate((List) value);
        } else if (value.getClass().isArray()) {
            List list = new ArrayList();
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                list.add(Array.get(value, i));
            }
            return validate(list);
        } else {
            return "Actual type '%s' is not compatible with expected type '%s'".formatted(value.getClass().getName(), this.getActualType().getName());
        }
    }
}
