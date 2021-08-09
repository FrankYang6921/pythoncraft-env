package top.frankyang.pre.api.block.state;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

public class PropertyWrapper<T extends Comparable<T>> {
    private final Property<T> property;
    private final T value;

    public PropertyWrapper(Property<T> property, T value) {
        this.property = property;
        this.value = value;
    }

    public BlockState putTo(BlockState state) {
        return state.with(property, value);
    }

    public Property<T> getProperty() {
        return property;
    }
}
