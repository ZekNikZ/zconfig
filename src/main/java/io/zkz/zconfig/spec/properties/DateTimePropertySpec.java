package io.zkz.zconfig.spec.properties;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;

public class DateTimePropertySpec extends SimpleDelegatedPropertySpec<String, Instant> {
    public DateTimePropertySpec(
        @NotNull String key,
        @Nullable String comment,
        boolean optional,
        @Nullable Supplier<Instant> defaultValueProvider,
        @NotNull List<Validator<Instant>> validators
    ) {
        super(
            Instant.class,
            SERIALIZER,
            defaultValueProvider,
            validators,
            (defaultValueProvider1, validators1) -> BasicPropertySpec.String(
                key,
                comment,
                optional,
                defaultValueProvider1,
                validators1
            )
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
