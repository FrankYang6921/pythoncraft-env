package top.frankyang.pre.api.block.state;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlockStateData {
    private final List<PropertyWrapper<?>> properties = new ArrayList<>();

    public BlockStateData() {
    }

    public BlockStateData value(String name, int initial, int min, int max) {
        if (initial < min || initial > max)
            throw new IllegalArgumentException("The default value must be in range [" + min + ", " + max + "].");
        properties.add(new PropertyWrapper<>(IntProperty.of(name, min, max), initial));
        return this;
    }

    public BlockStateData value(String name, boolean initial) {
        properties.add(new PropertyWrapper<>(BooleanProperty.of(name), initial));
        return this;
    }

    public <T extends Enum<T>> BlockStateData value(String name, Class<T> clazz, T initial) {
        properties.add(new PropertyWrapper<>(EnumProperty.of(name, clazz), initial));
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> BlockStateData value(String name, Class<T> clazz, T initial, T... range) {
        properties.add(new PropertyWrapper<>(EnumProperty.of(name, clazz, range), initial));
        return this;
    }

    public <T extends Enum<T>> BlockStateData value(String name, Class<T> clazz, T initial, Collection<T> range) {
        properties.add(new PropertyWrapper<>(EnumProperty.of(name, clazz, range), initial));
        return this;
    }

    public <T extends Enum<T>> BlockStateData value(String name, Class<T> clazz, T initial, Predicate<T> range) {
        properties.add(new PropertyWrapper<>(EnumProperty.of(name, clazz, range), initial));
        return this;
    }

    public void addAll(Consumer<Property<?>> consumer) {
        for (PropertyWrapper<?> property : properties) {
            consumer.accept(property.getProperty());
        }
    }

    public BlockState putAll(BlockState blockState) {
        for (PropertyWrapper<?> property : properties) {
            blockState = property.putTo(blockState);
        }
        return blockState;
    }
}
