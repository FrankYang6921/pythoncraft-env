package top.frankyang.pre.api.block;

import net.minecraft.util.math.BlockPos;
import top.frankyang.pre.api.math.Vector3;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

/**
 * 包装类，包装原版类{@link BlockPos}。
 */
public class BlockPosition extends CastableImpl<BlockPos> implements Vector3<Integer> {
    public BlockPosition(BlockPos delegate) {
        super(delegate);
    }

    public BlockPosition(int x, int y, int z) {
        super(new BlockPos(x, y, z));
    }

    @Override
    public Integer getX() {
        return casted.getX();
    }

    @Override
    public Integer getY() {
        return casted.getY();
    }

    @Override
    public Integer getZ() {
        return casted.getZ();
    }

    @Override
    public String toString() {
        return "BlockPosition{" +
            getX() + ", " +
            getY() + ", " +
            getZ() + '}';
    }
}
