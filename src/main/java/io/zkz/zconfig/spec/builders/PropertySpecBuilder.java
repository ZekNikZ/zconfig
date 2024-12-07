package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class PropertySpecBuilder<SerializedType, ActualType> {
    protected final String key;
    protected boolean optional = false;
    protected @Nullable String comment;
    protected @Nullable Supplier<ActualType> defaultValueSupplier;
    protected final List<Validator<ActualType>> validators = new ArrayList<>();

    protected PropertySpecBuilder(String key) {
        this.key = key;
    }

    public PropertySpecBuilder<SerializedType, ActualType> optional() {
        this.optional = true;
        return this;
    }

    public PropertySpecBuilder<SerializedType, ActualType> comment(String comment) {
        this.comment = comment;
        return this;
    }

    public PropertySpecBuilder<SerializedType, ActualType> defaultValue(Supplier<ActualType> defaultValueSupplier) {
        this.defaultValueSupplier = defaultValueSupplier;
        return this;
    }

    public PropertySpecBuilder<SerializedType, ActualType> validator(Validator<ActualType> validator) {
        this.validators.add(validator);
        return this;
    }

    public abstract PropertySpec<SerializedType, ActualType> build();

    public static class Types {
        public BasicPropertySpecBuilder.String String(String key) {
            return new BasicPropertySpecBuilder.String(key);
        }

        public BasicPropertySpecBuilder.Integer Integer(String key) {
            return new BasicPropertySpecBuilder.Integer(key);
        }

        public BasicPropertySpecBuilder.Double Double(String key) {
            return new BasicPropertySpecBuilder.Double(key);
        }

        public BasicPropertySpecBuilder.Boolean Boolean(String key) {
            return new BasicPropertySpecBuilder.Boolean(key);
        }

        public DateTimePropertySpecBuilder DateTime(String key) {
            return new DateTimePropertySpecBuilder(key);
        }

        public <T> ListPropertySpecBuilder<T> List(String key, Function<SubTypes, PropertySpecBuilder<?, T>> valueSpec) {
            return new ListPropertySpecBuilder<>(key, valueSpec.apply(new SubTypes()).build());
        }

        public <T> MapPropertySpecBuilder<T> Map(String key, Function<SubTypes, PropertySpecBuilder<?, T>> valueSpec) {
            return new MapPropertySpecBuilder<>(key, valueSpec.apply(new SubTypes()).build());
        }

        public ObjectPropertySpecBuilder<Object> Object(String key) {
            return new ObjectPropertySpecBuilder<>(key, Object.class, List.of());
        }

        public <T> ObjectPropertySpecBuilder<T> Object(String key, Class<T> type) {
            throw new RuntimeException("TODO");
        }

        public ObjectPropertySpecBuilder<Object> Object(String key, @NotNull List<PropertySpec<?, ?>> properties) {
            return new ObjectPropertySpecBuilder<>(key, Object.class, properties);
        }
    }

    public static class SubTypes {
        public BasicPropertySpecBuilder.String String() {
            return new BasicPropertySpecBuilder.String("value");
        }

        public BasicPropertySpecBuilder.Integer Integer() {
            return new BasicPropertySpecBuilder.Integer("value");
        }

        public BasicPropertySpecBuilder.Double Double() {
            return new BasicPropertySpecBuilder.Double("value");
        }

        public BasicPropertySpecBuilder.Boolean Boolean() {
            return new BasicPropertySpecBuilder.Boolean("value");
        }

        public DateTimePropertySpecBuilder DateTime() {
            return new DateTimePropertySpecBuilder("value");
        }

        public <T> ListPropertySpecBuilder<T> List(Function<SubTypes, PropertySpecBuilder<?, T>> valueSpec) {
            return new ListPropertySpecBuilder<>("value", valueSpec.apply(new SubTypes()).build());
        }

        public <T> MapPropertySpecBuilder<T> Map(Function<SubTypes, PropertySpecBuilder<?, T>> valueSpec) {
            return new MapPropertySpecBuilder<>("value", valueSpec.apply(new SubTypes()).build());
        }

        public ObjectPropertySpecBuilder<Object> Object() {
            return new ObjectPropertySpecBuilder<>("value", Object.class, List.of());
        }

        public <T> ObjectPropertySpecBuilder<T> Object(Class<T> type) {
            throw new RuntimeException("TODO");
        }

        public ObjectPropertySpecBuilder<Object> Object(@NotNull List<PropertySpec<?, ?>> properties) {
            return new ObjectPropertySpecBuilder<>("value", Object.class, properties);
        }
    }
}