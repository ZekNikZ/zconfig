package io.zkz.zconfig.binding.binders;

import io.zkz.zconfig.binding.BoundItem;
import io.zkz.zconfig.binding.PropertySpecModifier;
import io.zkz.zconfig.spec.PropertySpec;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface Binder<SerializedType, ActualType> {
    PropertySpec<SerializedType, ActualType> bind(
        Class<ActualType> itemType,
        BoundItem<ActualType> item,
        @Nullable PropertySpecModifier modifier
    );
}
