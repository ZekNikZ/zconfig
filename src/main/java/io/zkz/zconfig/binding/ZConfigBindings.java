package io.zkz.zconfig.binding;

import io.zkz.zconfig.binding.annotation.*;
import io.zkz.zconfig.binding.binders.Binder;
import io.zkz.zconfig.binding.binders.SimpleBinder;
import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.spec.builders.*;
import io.zkz.zconfig.spec.properties.ObjectPropertySpec;
import io.zkz.zconfig.utils.AnnotationUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

// TODO: for the annotations that have class references, do a check to ensure they are public

public class ZConfigBindings {
    private static final Map<Class<?>, Binder<?, ?>> BINDERS = new HashMap<>();

    public static <S, A> void registerBinder(Binder<S, A> binder, Class<A> mainClass, Class<?>... otherSupportedClasses) {
        BINDERS.put(mainClass, binder);
        for (Class<?> otherSupportedClass : otherSupportedClasses) {
            BINDERS.put(otherSupportedClass, binder);
        }
    }

    @SuppressWarnings("unchecked")
    public static <S, A> Binder<S, A> getBinder(Class<A> clazz) {
        return (Binder<S, A>) BINDERS.get(clazz);
    }

    static {
        PropertySpecBuilder.Types types = new PropertySpecBuilder.Types();
        registerBinder(
            new SimpleBinder<>((itemType, item) -> {
                PrimitivePropertySpecBuilder.String builder = types.String(item.name());
                AnnotationUtils.ifPresent(item, Regex.class, annotation -> builder.regex(annotation.value()));
                AnnotationUtils.ifPresent(item, LengthRange.class, annotation -> builder.range(annotation.min(), annotation.max()));
                AnnotationUtils.ifPresent(item, StringOneOf.class, annotation -> builder.oneOf(annotation.value()));
                return builder;
            }),
            String.class
        );
        registerBinder(
            new SimpleBinder<>((itemType, item) -> {
                PrimitivePropertySpecBuilder.Integer builder = types.Integer(item.name());
                AnnotationUtils.ifPresent(item, IntRange.class, annotation -> builder.range(annotation.min(), annotation.max()));
                AnnotationUtils.ifPresent(item, IntOneOf.class, annotation -> builder.oneOf(annotation.value()));
                return builder;
            }),
            Integer.class,
            int.class
        );
        registerBinder(
            new SimpleBinder<>((itemType, item) -> {
                PrimitivePropertySpecBuilder.Double builder = types.Double(item.name());
                AnnotationUtils.ifPresent(item, DoubleRange.class, annotation -> builder.range(annotation.min(), annotation.max()));
                return builder;
            }),
            Double.class,
            double.class
        );
        registerBinder(
            new SimpleBinder<>((itemType, item) -> types.Boolean(item.name())),
            Boolean.class,
            boolean.class
        );
        registerBinder(
            new SimpleBinder<>((itemType, item) -> types.DateTime(item.name())),
            Instant.class
        );
        registerBinder(
            new SimpleBinder<>((itemType, item) -> {
                // Figure out item type
                Class<?> valueType;
                if (itemType.isArray()) {
                    valueType = itemType.getComponentType();
                } else if (AnnotationUtils.isPresent(item, ValueType.class)) {
                    valueType = item.getAnnotation(ValueType.class).value();
                } else if (AnnotationUtils.isPresent(item, ValueSpec.class)) {
                    valueType = item.getAnnotation(ValueSpec.class).value();
                } else {
                    // TODO: error
                    throw new RuntimeException("cant figure out value type");
                }

                AtomicReference<PropertySpecModifier> modifier = new AtomicReference<>(null);
                AnnotationUtils.ifPresent(item, ValueSpecModifier.class, annotation -> {
                    try {
                        Constructor<? extends PropertySpecModifier> constructor = annotation.value().getConstructor();
                        constructor.setAccessible(true);
                        modifier.set(constructor.newInstance());
                    } catch (NoSuchMethodException e) {
                        // TODO: error (no parameterless default constructor)
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        // TODO: error (class is abstract)
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        // TODO: error (can't access ctor)
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        // TODO: error (ctor threw)
                        throw new RuntimeException(e);
                    }
                });

                return types.List(
                    item.name(),
                    bindItem(
                        new BoundItem<>(
                            "value",
                            valueType,
                            item.getAnnotation(ValueSpec.class) != null ?
                                Map.of(Spec.class, new Spec() {
                                    @Override
                                    public Class<? extends Annotation> annotationType() {
                                        return Spec.class;
                                    }

                                    @Override
                                    public Class<?> value() {
                                        return item.getAnnotation(ValueSpec.class).value();
                                    }
                                })
                                : Map.of(),
                            null
                        ),
                        modifier.get() != null ? modifier.get() : null
                    )
                );
            }),
            List.class
        );
        registerBinder(
            new SimpleBinder<>((itemType, item) -> {
                // Figure out item type
                Class<?> valueType;
                if (AnnotationUtils.isPresent(item, ValueType.class)) {
                    valueType = item.getAnnotation(ValueType.class).value();
                } else if (AnnotationUtils.isPresent(item, ValueSpec.class)) {
                    valueType = item.getAnnotation(ValueSpec.class).value();
                } else {
                    // TODO: error
                    throw new RuntimeException("cant figure out value type");
                }

                AtomicReference<PropertySpecModifier> modifier = new AtomicReference<>(null);
                AnnotationUtils.ifPresent(item, ValueSpecModifier.class, annotation -> {
                    try {
                        Constructor<? extends PropertySpecModifier> constructor = annotation.value().getConstructor();
                        constructor.setAccessible(true);
                        modifier.set(constructor.newInstance());
                    } catch (NoSuchMethodException e) {
                        // TODO: error (no parameterless default constructor)
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        // TODO: error (class is abstract)
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        // TODO: error (can't access ctor)
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        // TODO: error (ctor threw)
                        throw new RuntimeException(e);
                    }
                });

                return types.Map(
                    item.name(),
                    bindItem(
                        new BoundItem<>(
                            "value",
                            valueType,
                            item.getAnnotation(ValueSpec.class) != null ?
                                Map.of(Spec.class, new Spec() {
                                    @Override
                                    public Class<? extends Annotation> annotationType() {
                                        return Spec.class;
                                    }

                                    @Override
                                    public Class<?> value() {
                                        return item.getAnnotation(ValueSpec.class).value();
                                    }
                                })
                                : Map.of(),
                            null
                        ),
                        modifier.get() != null ? modifier.get() : null
                    )
                );
            }),
            Map.class
        );
    }

    @SuppressWarnings("unchecked")
    public static <SerializedType, ActualType> PropertySpec<SerializedType, ActualType> bindItem(
        BoundItem<ActualType> item,
        @Nullable PropertySpecModifier modifier
    ) {
        /* TODO: psuedocode
            if (@Serializer) make delegated property with serializer and repeat this process for serialized type
            else if (@Spec) use spec with object spec
            else if (class type has registered binder) use that
            else error
         */

        if (AnnotationUtils.isPresent(item, SerializedBy.class)) {
            Class<? extends Serializer<SerializedType, ActualType>> serializerClass = (Class<? extends Serializer<SerializedType, ActualType>>) item.getAnnotation(SerializedBy.class).value();
            try {
                Constructor<? extends Serializer<SerializedType, ActualType>> constructor = serializerClass.getConstructor();
                constructor.setAccessible(true);
                Serializer<SerializedType, ActualType> serializer = constructor.newInstance();
                Class<SerializedType> serializedType = null;
                for (Type genericInterface : serializer.getClass().getGenericInterfaces()) {
                    if (genericInterface instanceof ParameterizedType type && type.getRawType().equals(Serializer.class)) {
                        serializedType = (Class<SerializedType>) type.getActualTypeArguments()[0];
                        break;
                    }
                }
                if (serializedType == null) {
                    // TODO: error
                    throw new RuntimeException("no serailzied type");
                }
                final Class<SerializedType> finalSerializedType = serializedType;

                return new SimpleBinder<SerializedType, ActualType>(
                    (itemType, item1) -> new BasicPropertySpecBuilder<>(
                        item1.name(),
                        finalSerializedType,
                        itemType,
                        serializer
                    )
                ).bind(item.type(), item, modifier);
            } catch (NoSuchMethodException e) {
                // TODO: error (no parameterless default constructor)
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                // TODO: error (class is abstract)
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                // TODO: error (can't access ctor)
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                // TODO: error (ctor threw)
                throw new RuntimeException(e);
            }
        } else if (AnnotationUtils.isPresent(item, Spec.class)) {
            return (PropertySpec<SerializedType, ActualType>) bindObject(item.name(), item.getAnnotation(Spec.class).value(), modifier);
        } else if (item.type().isArray()) {
            // Special case for arrays
            return ((Binder<SerializedType, ActualType>) getBinder(List.class)).bind(item.type(), item, modifier);
        } else if (getBinder(item.type()) != null) {
            return ((Binder<SerializedType, ActualType>) getBinder(item.type())).bind(item.type(), item, modifier);
        }

        // TODO: error
        throw new RuntimeException("TODO / %s".formatted(item));
    }

    public static <T> ObjectPropertySpec<T> bindObject(
        String key,
        Class<T> containingClass,
        @Nullable PropertySpecModifier modifier
    ) {
        List<PropertySpec<?, ?>> propertySpecs = new ArrayList<>();
        Field[] declaredFields = containingClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getAnnotation(Exclude.class) != null) {
                continue;
            }

            try {
                propertySpecs.add(bindItem(BoundItem.fromField(field, field.getType()), null));
            } catch (Exception e) {
                // TODO: proper exception
                System.out.printf("%s / %s%n", field.getName(), e);
                throw new RuntimeException(e);
            }
        }
        ObjectPropertySpecBuilder<T> builder = new PropertySpecBuilder.Types().Object(key, containingClass, propertySpecs);
        if (modifier != null) {
            modifier.accept(builder);
        }
        return builder.build();
    }
}
