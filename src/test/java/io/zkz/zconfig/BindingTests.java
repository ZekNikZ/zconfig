package io.zkz.zconfig;

import io.zkz.zconfig.binding.ZConfigBindings;
import io.zkz.zconfig.spec.PropertySpec;
import io.zkz.zconfig.spec.properties.ObjectPropertySpec;
import org.junit.jupiter.api.Test;

public class BindingTests {
    @Test
    public void test() throws Exception {
        ObjectPropertySpec<TestPOJO> result = ZConfigBindings.bindObject(null, TestPOJO.class, null);

        for (PropertySpec<?, ?> property : result.getProperties()) {
            System.out.printf("%s (%s): %s%n", property.getKey(), property.getActualType(), property);
        }
    }
}
