package com.skhu.bobinlee.skhuapp.util;

/**
 * Created by BoBinLee on 2014-09-11.
 */
public class StringUtils {
    public static <T> String join(T[] array, String cement) {
        StringBuilder builder = new StringBuilder();
        if(array == null || array.length == 0) {
            return null;
        }
        for (T t : array) {
            builder.append(t).append(cement);
        }
        builder.delete(builder.length() - cement.length(), builder.length());
        return builder.toString();
    }
}
