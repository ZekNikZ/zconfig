package io.zkz.zconfig;

import io.zkz.zconfig.annotation.*;
import io.zkz.zconfig.conversion.Serializer;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TestPOJO {
    @Comment("Test integer")
    public int myIntProperty;

    @StringOneOf({"A", "B", "C"})
    public String myStringProperty;

    @Regex("^start-\\d+")
    public String myRegexProperty;

    @IntRange(min = 2, max = 10)
    public int myRestrictedIntProperty;

    @DoubleRange(max = 0)
    public double myDoubleProperty;

    @Key("actuallyThisKey")
    @Optional
    public String myOptionalProperty;

    @Spec(TestSubObject.class)
    public TestSubObject mySubObject;

    @Converter(Utils.Converter.class)
    public TestDynamicSubObject myDynamicSubObject;

    @LengthRange(min = 2, max = 5)
    @ValueType(List.class)
    public List<Integer> myIntList;

    @Spec(TestSubObject.class)
    public List<TestSubObject> mySubObjectList;

    @Spec(TestSubObject.class)
    public Map<String, TestSubObject> myMap;

    @KeyType(TestEnum.class)
    public Map<TestEnum, Integer> myEnumMap;

    public static class TestSubObject {
        @Validator(Utils.Validator.class)
        String myValidatedValue;
    }

    public record TestDynamicSubObject(String a, int b) {
    }

    public enum TestEnum {
        VALUE_A,
        VALUE_B
    }

    private static class Utils {
        private static class Converter implements Serializer<String, TestDynamicSubObject> {
            @Override
            public TestDynamicSubObject deserialize(String value) {
                String[] parts = value.split("-", 2);
                return new TestDynamicSubObject(parts[0], Integer.parseInt(parts[1]));
            }

            @Override
            public String serialize(TestDynamicSubObject value) {
                return value.a + "-" + value.b;
            }
        }

        private static class Validator implements Predicate<Object> {
            @Override
            public boolean test(Object o) {
                if (!(o instanceof String str)) {
                    return false;
                }

                return str.startsWith("test") && str.length() == 7;
            }
        }
    }
}