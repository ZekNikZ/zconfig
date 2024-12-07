package io.zkz.zconfig.utils;

public class ErrorUtils {
    public static String tagError(String prefix, String error) {
        if (error.startsWith("[")) {
            return "[%s.%s".formatted(prefix, error.substring(1));
        } else {
            return "[%s] %s".formatted(prefix, error);
        }
    }
}
