package com.project.NovelWeb.utils;

public class EnumUtils {
    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String value) {
        if (value == null) {
            return false;
        }
        try {
            Enum.valueOf(enumClass, value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
