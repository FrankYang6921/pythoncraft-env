package top.frankyang.pre.api.util;

import java.lang.reflect.Array;
import java.util.function.UnaryOperator;

@SuppressWarnings("SuspiciousSystemArraycopy")
public class ArrayUtils {
    public static Object mergeArrays(Object array, Object more) {
        int l0 = Array.getLength(array);
        int l1 = Array.getLength(more);
        Object newArray = Array.newInstance(Object.class, l0 + l1);
        System.arraycopy(array, 0, newArray, 0, l0);
        System.arraycopy(more, 0, newArray, l0, l1);
        return newArray;
    }

    public static Object[] mergeArrays(Object[] array, Object... more) {
        return mergeArrays(Object.class, array, more);
    }

    @SafeVarargs
    public static <U> U[] mergeArrays(Class<U> clazz, U[] array, U... more) {
        int l0 = array.length;
        int l1 = more.length;
        //noinspection unchecked
        U[] newArray = (U[]) Array.newInstance(clazz, l0 + l1);
        System.arraycopy(array, 0, newArray, 0, l0);
        System.arraycopy(more, 0, newArray, l0, l1);
        return newArray;
    }

    public static Object copy(Object src) {
        int l;
        Object dst;
        System.arraycopy(
            src, 0, dst = Array.newInstance(src.getClass().getComponentType(), l = Array.getLength(src)), 0, l
        );
        return dst;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] deepCopy(T[] src) {
        T[] dst = (T[]) Array.newInstance(src.getClass().getComponentType(), src.length);
        for (int i = 0; i < src.length; i++) {
            dst[i] = CopyUtils.deepCopy(src[i]);
        }
        return dst;
    }
}
