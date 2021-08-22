package top.frankyang.pre.api.block.state;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.Objects;

/**
 * 包装类。包装原版类{@link BlockState}。该类是可变的。
 */
public class MutableBlockState extends ImmutableBlockState implements BlockStateLike {
    private final WorldAccess access;
    private final BlockPos position;

    public MutableBlockState(BlockState delegate, WorldAccess access, BlockPos position) {
        super(delegate);
        this.access = access;
        this.position = position;
    }

    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> BlockStateLike setProperty(String key, T value) {
        Property<?> property = Objects.requireNonNull(properties.get(key), "No such key present: " + key);
        if (!property.getType().isInstance(value)) {
            throw new ClassCastException(value + "?");
        }
        // Flag 3 is the default behaviour as seen in net.minecraft.world.World.
        access.setBlockState(position, delegate = delegate.with((Property<T>) property, value), 3);
        return this;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
