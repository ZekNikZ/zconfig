package io.zkz.zconfig.spec.properties;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

public class DateTimePropertySpec extends BasicPropertySpec<String, Instant> {
    public DateTimePropertySpec(
        @NotNull String key,
        @Nullable String storageKey,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Instant> defaultValueProvider,
        @NotNull List<Validator<Instant>> validators
    ) {
        super(
            key,
            storageKey,
            comment,
            optional,
            defaultValueProvider,
            validators,
            String.class,
            Instant.class,
            SERIALIZER
        );
    }

    private static final Serializer<String, Instant> SERIALIZER = new Serializer<>() {
        @Override
        public Instant deserialize(String value) {
            return Instant.parse(value);
        }

        @Override
        public String serialize(Instant value) {
            return value.toString();
        }
    };
}
