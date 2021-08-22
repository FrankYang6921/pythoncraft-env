package top.frankyang.pre.api.block.state;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import top.frankyang.pre.api.block.state.property.EnumProperty;
import top.frankyang.pre.api.block.state.property.PropertyValue;
import top.frankyang.pre.api.block.state.property.UserProperty;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 方块状态工厂。注意与{@link BlockStateLike}类区分，该类并不包含实际数据，只包含数据模型——包含所有的键及其默认值。
 */
public class BlockStateFactory {
    private final List<PropertyValue<?>> properties = new ArrayList<>();

    /**
     * 以建造者模式在方块属性数据中增加一个整形键。
     *
     * @param name    属性名。
     * @param initial 默认值。
     * @param min     最小值。
     * @param max     最大值。
     */
    public BlockStateFactory property(String name, int initial, int min, int max) {
        if (initial < min || initial > max)
            throw new IllegalArgumentException("The default property must be in range [" + min + ", " + max + "].");
        properties.add(new PropertyValue<>(IntProperty.of(name, min, max), initial));
        return this;
    }

    /**
     * 以建造者模式在方块属性数据中增加一个布尔键。
     *
     * @param name    属性名。
     * @param initial 默认值。
     */
    public BlockStateFactory property(String name, boolean initial) {
        properties.add(new PropertyValue<>(BooleanProperty.of(name), initial));
        return this;
    }

    /**
     * 以建造者模式在方块属性数据中增加一个枚举键。
     *
     * @param name    属性名。
     * @param clazz   枚举类类型。
     * @param initial 默认值。
     */
    public <T extends Enum<T>> BlockStateFactory property(String name, Class<T> clazz, T initial) {
        properties.add(new PropertyValue<>(EnumProperty.of(name, clazz), initial));
        return this;
    }

    /**
     * 以建造者模式在方块属性数据中增加一个枚举键。
     *
     * @param name    属性名。
     * @param clazz   枚举类类型。
     * @param initial 默认值。
     * @param range   允许存储的枚举实例。
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> BlockStateFactory property(String name, Class<T> clazz, T initial, T... range) {
        properties.add(new PropertyValue<>(EnumProperty.of(name, clazz, range), initial));
        return this;
    }

    /**
     * 以建造者模式在方块属性数据中增加一个枚举键。
     *
     * @param name    属性名。
     * @param clazz   枚举类类型。
     * @param initial 默认值。
     * @param range   允许存储的枚举实例。
     */
    public <T extends Enum<T>> BlockStateFactory property(String name, Class<T> clazz, T initial, Collection<T> range) {
        properties.add(new PropertyValue<>(EnumProperty.of(name, clazz, range), initial));
        return this;
    }

    /**
     * 以建造者模式在方块属性数据中增加一个枚举键。
     *
     * @param name    属性名。
     * @param clazz   枚举类类型。
     * @param initial 默认值。
     * @param range   允许存储的枚举实例。
     */
    public <T extends Enum<T>> BlockStateFactory property(String name, Class<T> clazz, T initial, Predicate<T> range) {
        properties.add(new PropertyValue<>(EnumProperty.of(name, clazz, range), initial));
        return this;
    }

    /**
     * 以建造者模式在方块属性数据中增加一个自定义键。这是一个很困难的操作，如果您不理解如何使用它，我们建议您优先使用其它的方法。
     *
     * @param name    属性名。
     * @param clazz   自定义键的类实例。
     * @param writer  规定如何将一个值转化为字符串。
     * @param reader  规定如何将字符串转化为一个值。
     * @param initial 默认值。
     * @param range   允许存储的实例。
     * @param <T>     自定义键的类。
     */
    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> BlockStateFactory property(
        String name,
        Class<T> clazz,
        Function<T, String> writer,
        Function<String, T> reader,
        T initial,
        T... range
    ) {
        properties.add(new PropertyValue<>(UserProperty.of(name, clazz, writer, reader, range), initial));
        return this;
    }

    /**
     * 以建造者模式在方块属性数据中增加一个自定义键。这是一个很困难的操作，如果您不理解如何使用它，我们建议您优先使用其它的方法。
     *
     * @param name    属性名。
     * @param clazz   自定义键的类实例。
     * @param writer  规定如何将一个值转化为字符串。
     * @param reader  规定如何将字符串转化为一个值。
     * @param initial 默认值。
     * @param range   允许存储的实例。
     * @param <T>     自定义键的类。
     */
    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> BlockStateFactory property(
        String name,
        Class<T> clazz,
        Function<T, String> writer,
        Function<String, T> reader,
        T initial,
        Collection<T> range
    ) {
        T[] array = (T[]) Array.newInstance(clazz, range.size());
        Iterator<T> iterator = range.iterator();
        for (int i = 0; i < array.length; i++) {
            array[i] = iterator.next();
        }
        property(name, clazz, writer, reader, initial, array);
        return this;
    }

    public void addProperties(Consumer<Property<?>> consumer) {
        for (PropertyValue<?> property : properties) {
            consumer.accept(property.getProperty());
        }
    }

    public BlockState putInitial(BlockState blockState) {
        for (PropertyValue<?> property : properties) {
            blockState = property.putTo(blockState);
        }
        return blockState;
    }
}
