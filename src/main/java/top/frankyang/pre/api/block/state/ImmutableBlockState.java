package top.frankyang.pre.api.block.state;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import top.frankyang.pre.api.block.type.BlockType;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * 包装类。包装原版类{@link BlockState}。该类是不可变的。
 */
public class ImmutableBlockState extends CastableImpl<BlockState> implements BlockStateLike {
    protected final Map<String, Property<?>> properties = new HashMap<>();

    public ImmutableBlockState(BlockState delegate) {
        super(delegate);
        for (Property<?> property : delegate.getProperties()) {
            properties.put(property.getName(), property);
        }
    }

    public Comparable<?> getProperty(String key) {
        return casted.get(properties.get(key));
    }

    @Override
    public BlockType getBlockType() {
        if (BlockType.isPythonCraft(casted.getBlock()))
            return BlockType.ofVanilla(casted.getBlock());
        throw new UnsupportedOperationException();  // TODO make a simple wrapper
    }
}
