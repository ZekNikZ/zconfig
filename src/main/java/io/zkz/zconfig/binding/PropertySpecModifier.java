package io.zkz.zconfig.binding;

import io.zkz.zconfig.spec.builders.PropertySpecBuilder;

@FunctionalInterface
public
interface PropertySpecModifier {
    void accept(PropertySpecBuilder<?, ?> builder);
}
