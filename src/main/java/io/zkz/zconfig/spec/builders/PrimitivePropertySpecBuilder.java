package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.spec.properties.PrimitivePropertySpec;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class PrimitivePropertySpecBuilder<T> extends PropertySpecBuilder<T, T> {
    private final BasicPropertySpecFactory<T> factory;

    public PrimitivePropertySpecBuilder(@NotNull java.lang.String key, BasicPropertySpecFactory<T> factory) {
        super(key);
        this.factory = factory;
    }

    @Override
    public PrimitivePropertySpec<T> build() {
        return this.factory.build(
            this.key,
            this.storageKey,
            this.comment,
            this.optional,
            this.defaultValueSupplier,
            this.validators
        );
    }

    @FunctionalInterface
    public interface BasicPropertySpecFactory<T> {
        PrimitivePropertySpec<T> build(
            @NotNull java.lang.String key,
            @Nullable java.lang.String storageKey,
            @Nullable java.lang.String comment,
            boolean optional,
            @Nullable Supplier<T> defaultValueSupplier,
            List<Validator<T>> validators
        );
    }

    public static class String extends PrimitivePropertySpecBuilder<java.lang.String> {
        protected String(java.lang.String key) {
            super(key, PrimitivePropertySpec::String);
        }

        public String regex(java.lang.String regex) {
            this.validators.add(x -> x.matches(regex) ? null : "Value does not match regex /%s/".formatted(regex));
            return this;
        }


        public String min(int min) {
            this.validators.add(x -> x.length() >= min ? null : "Length '%s' is out of acceptable range: %d <= value".formatted(x, min));
            return this;
        }

        public String max(int max) {
            this.validators.add(x -> x.length() <= max ? null : "Length '%s' is out of acceptable range: value <= %d".formatted(x, max));
            return this;
        }

        public String range(int min, int max) {
            this.validators.add(x -> x.length() >= min && x.length() <= max ? null : "Length '%s' is out of acceptable range: %d <= value <= %d".formatted(x, min, max));
            return this;
        }

        public String oneOf(java.lang.String... strings) {
            this.validators.add(x -> Arrays.asList(strings).contains(x) ? null : "Value is not one of [%s]".formatted(java.lang.String.join(", ", strings)));
            return this;
        }

        public String oneOf(Collection<java.lang.String> strings) {
            this.validators.add(x -> strings.stream().toList().contains(x) ? null : "Value is not one of [%s]".formatted(java.lang.String.join(", ", strings)));
            return this;
        }
    }

    public static class Integer extends PrimitivePropertySpecBuilder<java.lang.Integer> {
        protected Integer(java.lang.String key) {
            super(key, PrimitivePropertySpec::Integer);
        }

        public Integer min(int min) {
            this.validators.add(x -> x >= min ? null : "Value '%d' is out of acceptable range: %d <= value".formatted(x, min));
            return this;
        }

        public Integer max(int max) {
            this.validators.add(x -> x <= max ? null : "Value '%d' is out of acceptable range: value <= %d".formatted(x, max));
            return this;
        }

        public Integer range(int min, int max) {
            this.validators.add(x -> x >= min && x <= max ? null : "Value '%d' is out of acceptable range: %d <= value <= %d".formatted(x, min, max));
            return this;
        }

        public Integer oneOf(int... ints) {
            this.validators.add(x -> Arrays.stream(ints).anyMatch(y -> x == y) ? null : "Value '%d' is not one of [%s]".formatted(x, Arrays.stream(ints).mapToObj(java.lang.String::valueOf).collect(Collectors.joining(", "))));
            return this;
        }

        public Integer oneOf(Collection<java.lang.Integer> ints) {
            this.validators.add(x -> ints.stream().anyMatch(y -> Objects.equals(x, y)) ? null : "Value '%d' is not one of [%s]".formatted(x, ints.stream().map(java.lang.String::valueOf).collect(Collectors.joining(", "))));
            return this;
        }
    }

    public static class Double extends PrimitivePropertySpecBuilder<java.lang.Double> {
        protected Double(java.lang.String key) {
            super(key, PrimitivePropertySpec::Double);
        }

        public Double min(double min) {
            this.validators.add(x -> x >= min ? null : "Value '%s' is out of acceptable range: %s <= value".formatted(x, min));
            return this;
        }

        public Double max(double max) {
            this.validators.add(x -> x <= max ? null : "Value '%s' is out of acceptable range: value <= %s".formatted(x, max));
            return this;
        }

        public Double range(double min, double max) {
            this.validators.add(x -> x >= min && x <= max ? null : "Value '%s' is out of acceptable range: %s <= value <= %s".formatted(x, min, max));
            return this;
        }
    }

    public static class Boolean extends PrimitivePropertySpecBuilder<java.lang.Boolean> {
        protected Boolean(java.lang.String key) {
            super(key, PrimitivePropertySpec::Boolean);
        }
    }
}
