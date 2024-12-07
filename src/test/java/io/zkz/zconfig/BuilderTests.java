package io.zkz.zconfig;

import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.spec.ConfigSpec;
import io.zkz.zconfig.spec.builders.PropertySpecBuilder;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;

public class BuilderTests {
    @Test
    public void builderTest() throws NoSuchFieldException {
//        ListPropertySpec<Integer> spec = ListPropertySpec.typed(
//            "test",
//            null,
//            false,
//            null,
//            List.of(list -> list.stream().allMatch(x -> x % 2 == 0) ? null : "bad modulus"),
//            BasicPropertySpec.Integer(
//                "value",
//                null,
//                false,
//                null,
//                List.of()
//            )
//        );
//
//        List<Integer> a = List.of(1, 2, 3);
//        List<Integer> b = List.of(2, 4, 6);
//
//        assertNotNull(spec.validate(a));
//        assertNull(spec.validate(b));

        ConfigSpec spec = ConfigSpec.builder()
            .addProperty(type ->
                type.Integer("myIntProperty")
                    .min(2)
                    .comment("Test integer")
                    .optional()
            )
            .addProperty(type ->
                type.String("myStringProperty")
                    .regex("^start-\\d+")
                    .comment("Test string")
            )
            .addProperty(type ->
                type.Boolean("myBooleanProperty")
                    .comment("Test boolean")
            )
            .addProperty(type ->
                type.List("myListProperty", subTypes ->
                        subTypes.Integer()
                            .max(5)
                            .optional()
                    )
                    .range(3, 5)
            )
            .addProperty(type ->
                type.Map("myMapProperty1", subTypes ->
                    subTypes.Integer()
                        .optional()
                )
            )
            .addProperty(type ->
                type.Map("myMapProperty2", subTypes ->
                    subTypes.Integer()
                        .optional()
                )
            )
            .addProperty(type ->
                type.Object("myObjectProperty1")
                    .addProperty(types ->
                        types.String("a")
                    )
                    .addProperty(types ->
                        types.Integer("b")
                    )
                    .addProperty(types ->
                        types.DateTime("date")
                            .after(Instant.now())
                    )
            )
            .addProperty(type ->
                type.Object("myObjectProperty2")
                    .addProperty(types ->
                        types.String("a")
                    )
                    .addProperty(types ->
                        types.Integer("b")
                    )
                    .addProperty(types ->
                        types.DateTime("date")
                            .after(Instant.now())
                    )
            )
            .addProperty(type ->
                type.Delegated("myDelegatedProperty", TestDelegateObject.class, new Serializer<>() {
                        @Override
                        public TestDelegateObject deserialize(String value) {
                            return new TestDelegateObject(value);
                        }

                        @Override
                        public String serialize(TestDelegateObject value) {
                            return value.message();
                        }
                    }, PropertySpecBuilder.SubTypes::String)
                    .validator(
                        x -> x.message.startsWith("ba") ? null : "Message must start with 'ba'"
                    )
            )
            .build();

        assertNull(
            spec.validate(
                new TestStructure(
                    2,
                    "start-12",
                    false,
                    new int[]{4, 5, 3, 2, 1},
                    Map.of("a", 1, "b", 2, "c", 4),
                    new TestSubStructureMap(1, 2),
                    new TestSubStructure(
                        "c",
                        4,
                        Instant.now()
                    ),
                    Map.of("a", "1", "b", 2, "date", Instant.now()),
                    new TestDelegateObject("bazinga")
                )
            )
        );
    }

    record TestStructure(
        Integer myIntProperty,
        String myStringProperty,
        boolean myBooleanProperty,
        int[] myListProperty,
        Map<String, Integer> myMapProperty1,
        TestSubStructureMap myMapProperty2,
        TestSubStructure myObjectProperty1,
        Map<String, Object> myObjectProperty2,
        TestDelegateObject myDelegatedProperty
    ) {
    }

    record TestSubStructure(
        String a,
        int b,
        Instant date
    ) {
    }

    record TestSubStructureMap(
        int a,
        int b
    ) {
    }

    record TestDelegateObject(
        String message
    ) {
    }
}
