package io.zkz.zconfig.binding;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record BoundItem<T>(
    String name,
    Class<T> type,
    Map<Class<? extends Annotation>, Annotation> annotations,
    Function<Object, T> valueGetter
) {
    @SuppressWarnings("unchecked")
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return (A) annotations().get(annotationClass);
    }

    @SuppressWarnings("unchecked")
    public static <F> BoundItem<F> fromField(Field field, Class<F> fieldType) {
        return new BoundItem<>(
            field.getName(),
            fieldType,
            Arrays.stream(field.getAnnotations()).collect(Collectors.toUnmodifiableMap(Annotation::annotationType, Function.identity())),
            (obj) -> {
                try {
                    return (F) field.get(obj);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
}
