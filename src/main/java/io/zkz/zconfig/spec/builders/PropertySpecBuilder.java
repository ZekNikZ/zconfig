package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class PropertySpecBuilder<SerializedType, ActualType> {
    protected @NotNull String key;
    protected @Nullable String storageKey;
    protected boolean optional = false;
    protected @Nullable String comment;
    protected @Nullable Supplier<ActualType> defaultValueSupplier;
    protected final List<Validator<ActualType>> validators = new ArrayList<>();

    protected PropertySpecBuilder(@NotNull String key) {
        this.key = key;
    }

    public PropertySpecBuilder<SerializedType, ActualType> key(@NotNull String key) {
        this.key = key;
        return this;
    }

    public PropertySpecBuilder<SerializedType, ActualType> storageKey(@NotNull String storageKey) {
        this.storageKey = storageKey;
        return this;
    }

    public PropertySpecBuilder<SerializedType, ActualType> optional() {
        this.optional = true;
        return this;
    }

    public PropertySpecBuilder<SerializedType, ActualType> optional(boolean optional) {
        this.optional = optional;
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

    public PropertySpecBuilder<SerializedType, ActualType> validators(Collection<? extends Validator<ActualType>> validators) {
        this.validators.addAll(validators);
        return this;
    }

    public abstract PropertySpec<SerializedType, ActualType> build();

    public static class Types {
        public PrimitivePropertySpecBuilder.String String(String key) {
            return new PrimitivePropertySpecBuilder.String(key);
        }

        public PrimitivePropertySpecBuilder.Integer Integer(String key) {
            return new PrimitivePropertySpecBuilder.Integer(key);
        }

        public PrimitivePropertySpecBuilder.Double Double(String key) {
            return new PrimitivePropertySpecBuilder.Double(key);
        }

        public PrimitivePropertySpecBuilder.Boolean Boolean(String key) {
            return new PrimitivePropertySpecBuilder.Boolean(key);
        }

        public DateTimePropertySpecBuilder DateTime(String key) {
            return new DateTimePropertySpecBuilder(key);
        }

        public <S, A> ListPropertySpecBuilder<A> List(String key, Function<SubTypes, PropertySpecBuilder<S, A>> valueSpec) {
            return new ListPropertySpecBuilder<>(key, valueSpec.apply(new SubTypes()).build());
        }

        public <S, A> ListPropertySpecBuilder<A> List(String key, PropertySpec<S, A> valueSpec) {
            return new ListPropertySpecBuilder<>(key, valueSpec);
        }

        public <S, A> MapPropertySpecBuilder<A> Map(String key, Function<SubTypes, PropertySpecBuilder<S, A>> valueSpec) {
            return new MapPropertySpecBuilder<>(key, valueSpec.apply(new SubTypes()).build());
        }

        public <S, A> MapPropertySpecBuilder<A> Map(String key, PropertySpec<S, A> valueSpec) {
            return new MapPropertySpecBuilder<>(key, valueSpec);
        }

        public ObjectPropertySpecBuilder<Object> Object(String key) {
            return new ObjectPropertySpecBuilder<>(key, Object.class, List.of());
        }

        public <T> ObjectPropertySpecBuilder<T> Object(String key, @NotNull Class<T> type, @NotNull List<PropertySpec<?, ?>> properties) {
            return new ObjectPropertySpecBuilder<>(key, type, properties);
        }

        public ObjectPropertySpecBuilder<Object> Object(String key, @NotNull List<PropertySpec<?, ?>> properties) {
            return new ObjectPropertySpecBuilder<>(key, Object.class, properties);
        }

        public <S, A> BasicPropertySpecBuilder<S, A> Basic(String key, Class<S> serializedType, Class<A> actualType, Serializer<S, A> serializer) {
            return new BasicPropertySpecBuilder<>(key, serializedType, actualType, serializer);
        }
    }

    public static class SubTypes {
        public PrimitivePropertySpecBuilder.String String() {
            return new PrimitivePropertySpecBuilder.String("value");
        }

        public PrimitivePropertySpecBuilder.Integer Integer() {
            return new PrimitivePropertySpecBuilder.Integer("value");
        }

        public PrimitivePropertySpecBuilder.Double Double() {
            return new PrimitivePropertySpecBuilder.Double("value");
        }

        public PrimitivePropertySpecBuilder.Boolean Boolean() {
            return new PrimitivePropertySpecBuilder.Boolean("value");
        }

        public DateTimePropertySpecBuilder DateTime() {
            return new DateTimePropertySpecBuilder("value");
        }

        public <S, A> ListPropertySpecBuilder<A> List(Function<SubTypes, PropertySpecBuilder<S, A>> valueSpec) {
            return new ListPropertySpecBuilder<>("value", valueSpec.apply(new SubTypes()).build());
        }

        public <S, A> ListPropertySpecBuilder<A> List(PropertySpec<S, A> valueSpec) {
            return new ListPropertySpecBuilder<>("value", valueSpec);
        }

        public <S, A> MapPropertySpecBuilder<A> Map(Function<SubTypes, PropertySpecBuilder<S, A>> valueSpec) {
            return new MapPropertySpecBuilder<>("value", valueSpec.apply(new SubTypes()).build());
        }

        public <S, A> MapPropertySpecBuilder<A> Map(PropertySpec<S, A> valueSpec) {
            return new MapPropertySpecBuilder<>("value", valueSpec);
        }

        public ObjectPropertySpecBuilder<Object> Object() {
            return new ObjectPropertySpecBuilder<>("value", Object.class, List.of());
        }

        public <T> ObjectPropertySpecBuilder<T> Object(@NotNull Class<T> type, @NotNull List<PropertySpec<?, ?>> properties) {
            return new ObjectPropertySpecBuilder<>("value", type, properties);
        }

        public ObjectPropertySpecBuilder<Object> Object(@NotNull List<PropertySpec<?, ?>> properties) {
            return new ObjectPropertySpecBuilder<>("value", Object.class, properties);
        }

        public <S, A> BasicPropertySpecBuilder<S, A> Basic(Class<S> serializedType, Class<A> actualType, Serializer<S, A> serializer) {
            return new BasicPropertySpecBuilder<>("value", serializedType, actualType, serializer);
        }
    }
}