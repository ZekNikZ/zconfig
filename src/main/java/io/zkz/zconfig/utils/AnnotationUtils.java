package io.zkz.zconfig.utils;

import io.zkz.zconfig.binding.BoundItem;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.function.Consumer;

public class AnnotationUtils {
    public static <T extends Annotation> boolean isPresent(AnnotatedElement element, Class<T> annotationClass) {
        return element.getAnnotation(annotationClass) != null;
    }

    public static <T extends Annotation> void ifPresent(AnnotatedElement element, Class<T> annotationClass, Consumer<T> consumer) {
        if (isPresent(element, annotationClass)) {
            consumer.accept(element.getAnnotation(annotationClass));
        }
    }

    public static <T extends Annotation> boolean isPresent(BoundItem<?> element, Class<T> annotationClass) {
        return element.getAnnotation(annotationClass) != null;
    }

    public static <T extends Annotation> void ifPresent(BoundItem<?> element, Class<T> annotationClass, Consumer<T> consumer) {
        if (isPresent(element, annotationClass)) {
            consumer.accept(element.getAnnotation(annotationClass));
        }
    }
}
