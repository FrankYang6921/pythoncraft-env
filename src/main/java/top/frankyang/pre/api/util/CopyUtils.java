package top.frankyang.pre.api.util;

import top.frankyang.pre.loader.exceptions.ImpossibleException;

import java.lang.reflect.Method;

public final class CopyUtils {
    private static final Method cloneMethod;

    static {
        try {
            cloneMethod = Object.class.getDeclaredMethod("clone");
            cloneMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ImpossibleException(e);
        }
    }

    private CopyUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T shallowCopy(T t) {
        if (t == null) {
            return null;
        }
        if (t instanceof Cloneable) {
            return (T) ReflectUtils.tryThrow(() -> cloneMethod.invoke(t));
        }
        Class<T> clazz = (Class<T>) t.getClass();
        if (ReflectUtils.isPrimitiveOnly(clazz)) {
            return t;
        }
        T target = ReflectUtils.forceNew(clazz);
        ReflectUtils.assignTo(t, target);
        return target;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T t) {
        if (t == null) {
            return null;
        }
        Class<T> clazz = (Class<T>) t.getClass();
        if (clazz.isArray()) {
            if (clazz.getComponentType().isPrimitive()) {
                return (T) ArrayUtils.copy(t);
            }
            return (T) ArrayUtils.deepCopy((Object[]) t);
        }
        if (ReflectUtils.isPrimitiveOnly(clazz)) {
            return t;
        }
        T target = ReflectUtils.forceNew(clazz);
        ReflectUtils.deepAssignTo(t, target);
        return target;
    }
}
