package io.zkz.zconfig.conversion;

public interface Serializer<SerializedType, ActualType> {
    ActualType deserialize(SerializedType value);

    SerializedType serialize(ActualType value);

    static <T> Serializer<T, T> identity() {
        return new Serializer<>() {
            @Override
            public T serialize(T value) {
                return value;
            }

            @Override
            public T deserialize(T value) {
                return value;
            }
        };
    }
}
