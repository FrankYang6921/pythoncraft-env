package top.frankyang.pre.api.reflect.access;

public interface Accessor<T> {
    /**
     * 获取一个字段。
     *
     * @param name 字段名。
     * @return 获取到的字段。
     */
    <U> U getField(String name);

    /**
     * 获取一个静态字段。
     *
     * @param name 字段名。
     * @return 获取到的静态字段。
     */
    <U> U getStatic(String name);

    /**
     * 写入一个字段。
     *
     * @param name  字段名。
     * @param value 字段值。
     */
    void setField(String name, Object value);

    /**
     * 写入一个静态字段。
     *
     * @param name  字段名。
     * @param value 字段值。
     */
    void setStatic(String name, Object value);

    /**
     * 调用一个方法。
     *
     * @param name  方法名。
     * @param types 方法形参类型数组。
     * @param args  方法实参数组。
     * @return 方法的返回值。
     */
    <U> U invoke(String name, Class<?>[] types, Object[] args);

    /**
     * 调用一个静态方法。
     *
     * @param name 方法名。
     * @param type 方法形参类型。
     * @param arg 方法实参。
     * @return 方法的返回值。
     */
    default <U> U invoke(String name, Class<?> type, Object arg) {
        return invoke(name, new Class<?>[]{type}, new Object[]{arg});
    }

    /**
     * 调用一个方法。
     *
     * @param name 方法名。
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
     * @return 方法的返回值。
     */
    <U> U invokeStatic(String name, Class<?>[] types, Object[] args);

    /**
     * 调用一个静态方法。
     *
     * @param name 方法名。
     * @param type 方法形参类型。
     * @param arg 方法实参。
     * @return 方法的返回值。
     */
    default <U> U invokeStatic(String name, Class<?> type, Object arg) {
        return invokeStatic(name, new Class<?>[]{type}, new Object[]{arg});
    }

    /**
     * 调用一个静态方法。
     *
     * @param name 方法名。
     * @return 方法的返回值。
     */
    default <U> U invokeStatic(String name) {
        return invokeStatic(name, new Class[0], new Object[0]);
    }
}
