package top.frankyang.pre.api.util;

import java.lang.reflect.Array;

public class ArrayUtils {
    public static Object[] mergeArrays(Object[] array, Object... more) {
        Object[] newArray = (Object[]) Array.newInstance(Object.class, array.length + more.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        System.arraycopy(more, 0, newArray, array.length, more.length);
        return newArray;
    }

    @SafeVarargs
    public static <U> U[] mergeArrays(Class<U> clazz, U[] array, U... more) {
        //noinspection unchecked
        U[] newArray = (U[]) Array.newInstance(clazz, array.length + more.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        System.arraycopy(more, 0, newArray, array.length, more.length);
        return newArray;
    }
}
