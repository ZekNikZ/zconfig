package io.zkz.zconfig.binding.binders;

import io.zkz.zconfig.binding.BoundItem;
import io.zkz.zconfig.binding.PropertySpecModifier;
import io.zkz.zconfig.binding.annotation.*;
import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.spec.builders.PropertySpecBuilder;
import io.zkz.zconfig.utils.AnnotationUtils;
import io.zkz.zconfig.validation.Validator;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class SimpleBinder<SerializedType, ActualType> implements Binder<SerializedType, ActualType> {
    private final BinderFactory<SerializedType, ActualType> supplier;

    public SimpleBinder(BinderFactory<SerializedType, ActualType> supplier) {
        this.supplier = supplier;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PropertySpec<SerializedType, ActualType> bind(
        Class<ActualType> itemType,
        BoundItem<ActualType> item,
        @Nullable PropertySpecModifier modifier
    ) {
        PropertySpecBuilder<SerializedType, ActualType> builder = supplier.bind(itemType, item);
        AnnotationUtils.ifPresent(item, Key.class, annotation -> builder.storageKey(annotation.value()));
        AnnotationUtils.ifPresent(item, Optional.class, annotation -> builder.optional());
        AnnotationUtils.ifPresent(item, Comment.class, annotation -> builder.comment(annotation.value()));
        AnnotationUtils.ifPresent(item, Validate.class, annotation -> {
            Class<? extends Validator<?>> validatorClass = annotation.value();
            try {
                Constructor<? extends Validator<?>> constructor = validatorClass.getConstructor();
                constructor.setAccessible(true);
                builder.validator((Validator<ActualType>) constructor.newInstance());
            } catch (NoSuchMethodException e) {
                // TODO: error (no parameterless default constructor)
            } catch (InstantiationException e) {
                // TODO: error (class is abstract)
            } catch (IllegalAccessException e) {
                // TODO: error (can't access ctor)
            } catch (InvocationTargetException e) {
                // TODO: error (ctor threw)
            }
        });
        AnnotationUtils.ifPresent(item, DefaultValueSupplier.class, annotation -> {
            Class<? extends Supplier<?>> supplierClass = annotation.value();
            try {
                Constructor<? extends Supplier<?>> constructor = supplierClass.getConstructor();
                constructor.setAccessible(true);
                builder.defaultValue((Supplier<ActualType>) constructor.newInstance());
            } catch (NoSuchMethodException e) {
                // TODO: error (no parameterless default constructor)
            } catch (InstantiationException e) {
                // TODO: error (class is abstract)
            } catch (IllegalAccessException e) {
                // TODO: error (can't access ctor)
            } catch (InvocationTargetException e) {
                // TODO: error (ctor threw)
            }
        });
        AnnotationUtils.ifPresent(item, DefaultValue.class, annotation -> {
            try {
                Constructor<ActualType> constructor = itemType.getConstructor();
                constructor.setAccessible(true);
                Object object = constructor.newInstance();
                ActualType defaultValue = item.valueGetter().apply(object);
                builder.defaultValue(() -> defaultValue);
            } catch (NoSuchMethodException e) {
                // TODO: error (no parameterless default constructor)
            } catch (InstantiationException e) {
                // TODO: error (class is abstract)
            } catch (IllegalAccessException e) {
                // TODO: error (can't access ctor or field value)
            } catch (InvocationTargetException e) {
                // TODO: error (ctor threw)
            }
        });
        if (modifier != null) {
            modifier.accept(builder);
        }
        return builder.build();
    }

    @FunctionalInterface
    public interface BinderFactory<SerializedType, ActualType> {
        PropertySpecBuilder<SerializedType, ActualType> bind(Class<ActualType> itemType, BoundItem<ActualType> item);
    }
}
