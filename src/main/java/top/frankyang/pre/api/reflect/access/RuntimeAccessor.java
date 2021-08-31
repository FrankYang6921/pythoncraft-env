package top.frankyang.pre.api.reflect.access;

import org.jetbrains.annotations.Nullable;
import top.frankyang.pre.api.misc.conversion.Castable;
import top.frankyang.pre.api.reflect.mapping.SymbolResolver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * 运行时访问器。可以动态地按照映射表访问一个类。
 *
 * @param <T> 要访问的类。
 */
@FunctionalInterface
public interface RuntimeAccessor<T> extends Castable<T>, Accessor<T> {
    /**
     * @param clazz 要访问的类对象。
     * @param <T>   要访问的类。
     * @return 静态运行时访问器。
     */
    static <T> RuntimeAccessor<T> of(Class<T> clazz) {
        return () -> clazz;
    }

    /**
     * @param object 要访问的实例。
     * @param <T>    要访问的类。
     * @return 运行时访问器。
     */
    static <T> RuntimeAccessor<T> of(T object) {
        //noinspection unchecked
        Class<T> clazz = (Class<T>) object.getClass();

        return new RuntimeAccessor<T>() {
            @Override
            public Class<T> getClazz() {
                return clazz;
            }

            @Override
            public T getTarget() {
                return object;
            }
        };
    }

    /**
     * @return 该（静态）运行时访问器指向的类。
     */
    Class<? extends T> getClazz();

    /**
     * @return 该运行时访问器指向的对象。
     */
    default @Nullable T getTarget() {
        return null;
    }

    default T cast() {
        return Objects.requireNonNull(
            getTarget(), "Cannot convert to a target object from a static-only runtime accessor!");
    }

    @Override
    default <U> U getField(String name) {
        if (getTarget() == null) {
            throw new IllegalStateException(
                "Cannot access instance field '" + name + "' from this static-only runtime accessor!");
        }
        Field field = SymbolResolver.getInstance().runtimeField(getClazz(), name);
        try {
            //noinspection unchecked
            return (U) field.get(getTarget());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default <U> U getStatic(String name) {
        Field field = SymbolResolver.getInstance().runtimeField(getClazz(), name);
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalStateException(
                "Cannot access instance field '" + name + "' when explicitly accessing a static field!");
        }
        try {
            //noinspection unchecked
            return (U) field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default void setField(String name, Object value) {
        if (getTarget() == null) {
            throw new IllegalStateException(
                "Cannot access instance field '" + name + "' from this static-only runtime accessor!");
        }
        Field field = SymbolResolver.getInstance().runtimeField(getClazz(), name);
        try {
            field.set(getTarget(), value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default void setStatic(String name, Object value) {
        Field field = SymbolResolver.getInstance().runtimeField(getClazz(), name);
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalStateException(
                "Cannot access instance field '" + name + "' when explicitly accessing a static field!");
        }
        try {
            field.set(null, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default <U> U invoke(String name, Class<?>[] types, Object[] args) {
        if (getTarget() == null) {
            throw new IllegalStateException(
                "Cannot access instance method '" + name + "' from this static-only runtime accessor!");
        }
        Method method = SymbolResolver.getInstance().runtimeMethod(getClazz(), name, types);
        try {
            //noinspection unchecked
            return (U) method.invoke(getTarget(), args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default <U> U invokeStatic(String name, Class<?>[] types, Object[] args) {
        Method method = SymbolResolver.getInstance().runtimeMethod(getClazz(), name, types);
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalStateException(
                "Cannot access instance method '" + name + "' when explicitly accessing a static method!");
        }
        try {
            //noinspection unchecked
            return (U) method.invoke(null, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
