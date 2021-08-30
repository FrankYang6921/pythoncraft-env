package top.frankyang.pre.api.reflect;

import org.jetbrains.annotations.Nullable;
import top.frankyang.pre.api.misc.Castable;
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
public interface RuntimeAccessor<T> extends Castable<T> {
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
        Class<? extends T> clazz = (Class<? extends T>) object.getClass();

        return new RuntimeAccessor<T>() {
            @Override
            public Class<? extends T> getClazz() {
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

    /**
     * 获取一个字段。
     *
     * @param name 字段名。
     * @param <U>  语法糖性质的类型参数。允许隐式转型到任意一个类型。
     * @return 获取到的字段。
     */
    default <U> U get(String name) {
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

    /**
     * 获取一个静态字段。
     *
     * @param name 字段名。
     * @param <U>  语法糖性质的类型参数。允许隐式转型到任意一个类型。
     * @return 获取到的静态字段。
     */
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

    /**
     * 写入一个字段。
     *
     * @param name  字段名。
     * @param value 字段值。
     */
    default void set(String name, Object value) {
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

    /**
     * 写入一个静态字段。
     *
     * @param name  字段名。
     * @param value 字段值。
     */
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

    /**
     * 调用一个方法。
     *
     * @param name  方法名。
     * @param types 方法形参类型数组。
     * @param args  方法实参数组。
     * @param <U>   语法糖性质的类型参数。允许隐式转型到任意一个类型。
     * @return 方法的返回值。
     */
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

    /**
     * 调用一个方法。
     *
     * @param name 方法名。
     * @param type 方法形参类型。
     * @param arg  方法实参。
     * @param <U>  语法糖性质的类型参数。允许隐式转型到任意一个类型。
     * @return 方法的返回值。
     */
    default <U> U invoke(String name, Class<?> type, Object arg) {
        return invoke(name, new Class[]{type}, new Object[]{arg});
    }

    /**
     * 调用一个方法。
     *
     * @param name 方法名。
     * @param <U>  语法糖性质的类型参数。允许隐式转型到任意一个类型。
     * @return 方法的返回值。
     */
    default <U> U invoke(String name) {
        return invoke(name, new Class[0], new Object[0]);
    }

    /**
     * 调用一个静态方法。
     *
     * @param name  方法名。
     * @param types 方法形参类型数组。
     * @param args  方法实参数组。
     * @param <U>   语法糖性质的类型参数。允许隐式转型到任意一个类型。
     * @return 方法的返回值。
     */
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
