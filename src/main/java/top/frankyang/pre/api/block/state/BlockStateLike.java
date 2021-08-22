package top.frankyang.pre.api.block.state;

import net.minecraft.block.BlockState;
import top.frankyang.pre.api.misc.Castable;

/**
 * 包装类接口。包装原版类{@link BlockState}。
 */
public interface BlockStateLike extends Castable<BlockState> {
    /**
     * 获取该方块状态的一个属性。
     *
     * @param key 属性名。
     * @return 属性值。
     */
    Comparable<?> getProperty(String key);

    /**
     * 获取该方块状态的一个布尔属性。
     *
     * @param key 属性名。
     * @return 属性值。
     */
    default boolean getBoolean(String key) {
        Object o = getProperty(key);
        if (o instanceof Boolean) {
            return (Boolean) getProperty(key);
        }
        throw new ClassCastException(
            "Property " + key + " cannot be casted as a boolean. It is a " + o.getClass().getName() + " instance.");
    }

    /**
     * 获取该方块状态的一个整形属性。
     *
     * @param key 属性名。
     * @return 属性值。
     */
    default int getInteger(String key) {
        Object o = getProperty(key);
        if (o instanceof Integer) {
            return (Integer) getProperty(key);
        }
        throw new ClassCastException(
            "Property " + key + " cannot be casted as a integer. It is a " + o.getClass().getName() + " instance.");
    }

    /**
     * 获取该方块状态的一个枚举属性。
     *
     * @param key 属性名。
     * @return 属性值。
     */
    default <T extends Enum<T>> T getEnum(String key, Class<T> clazz) {
        Object o = getProperty(key);
        if (clazz.isInstance(o)) {
            return clazz.cast(getProperty(key));
        }
        throw new ClassCastException(
            "Property " + key + " cannot be casted as a enum. It is a " + o.getClass().getName() + " instance.");
    }

    default <T extends Comparable<T>> T getCustom(String key, Class<T> clazz) {
        Object o = getProperty(key);
        if (clazz.isInstance(o)) {
            return clazz.cast(getProperty(key));
        }
        throw new ClassCastException(
            "Property " + key + " cannot be casted as a " + clazz.getName() + " instance. It is a " + o.getClass().getName() + " instance.");
    }

    /**
     * 设置该方块状态的一个属性。该方法可以连锁调用。
     *
     * @param key   属性名。
     * @param value 属性值。
     */
    default <T extends Comparable<T>> BlockStateLike setProperty(String key, T value) {
        throw new UnsupportedOperationException();
    }

    /**
     * 设置该方块状态的一个布尔属性。该方法可以连锁调用。该方法是为了在Python中调用时可以将{@code value}解析为布尔。
     *
     * @param key   属性名。
     * @param value 属性值。
     */
    default BlockStateLike setBoolean(String key, boolean value) {
        setProperty(key, value);
        return this;
    }

    /**
     * 设置该方块状态的一个整形属性。该方法可以连锁调用。该方法是为了在Python中调用时可以将{@code value}解析为整形。
     *
     * @param key   属性名。
     * @param value 属性值。
     */
    default BlockStateLike setInteger(String key, int value) {
        setProperty(key, value);
        return this;
    }
}
