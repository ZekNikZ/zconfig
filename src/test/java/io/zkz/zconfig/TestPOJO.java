package io.zkz.zconfig;

import io.zkz.zconfig.binding.annotation.*;
import io.zkz.zconfig.conversion.Serializer;
import io.zkz.zconfig.validation.Validator;

import java.util.List;
import java.util.Map;

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
    @Validate(MyValidator.class)
    public String myOptionalProperty;

    @Spec(TestSubObject.class)
    public TestSubObject mySubObject;

    @SerializedBy(MySerializer.class)
    public TestDynamicSubObject myDynamicSubObject;

    @LengthRange(min = 2, max = 5)
    @ValueType(Integer.class)
    public List<Integer> myIntList;

    @ValueSpec(TestSubObject.class)
    public List<TestSubObject> mySubObjectList;

    public int[] myIntList2;

    public Integer[] myIntList3;

    @ValueSpec(TestSubObject.class)
    public TestSubObject[] mySubObjectList2;

    @ValueSpec(TestSubObject.class)
    public Map<String, TestSubObject> myMap;

    // TODO: map enum key type annotation
    @ValueType(Integer.class)
    public Map<String, Integer> myEnumMap;

    public static class TestSubObject {
        @Validate(MyValidator.class)
        String myValidatedValue;
    }

    public record TestDynamicSubObject(String a, int b) {
    }
//
//    public enum TestEnum {
//        VALUE_A,
//        VALUE_B
//    }

    public static class MyValidator implements Validator<String> {
        @Override
        public String validate(String str) {
            return str.startsWith("test") && str.length() == 7 ? null : "Not valid!";
        }
    }

    public static class MySerializer implements Serializer<String, TestDynamicSubObject> {
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
}