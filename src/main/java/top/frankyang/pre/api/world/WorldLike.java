package top.frankyang.pre.api.world;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.WorldAccess;
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.entity.BlockEntityImpl;
import top.frankyang.pre.api.block.entity.BlockEntityLike;
import top.frankyang.pre.api.block.entity.VanillaBlockEntityImpl;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.block.state.MutableBlockState;
import top.frankyang.pre.api.misc.Castable;

public interface WorldLike extends Castable<WorldAccess> {
    default int lightLevelAt(BlockPosition position) {
        return cast().getLuminance(position.cast());
    }

    default int getDifficulty() {
        return cast().getDifficulty().getId();
    }

    default float difficultyAt(BlockPosition position) {
        return cast().getLocalDifficulty(position.cast()).getClampedLocalDifficulty();
    }

    default BlockStateLike blockStateAt(BlockPosition position) {
        return new MutableBlockState(cast(), position.cast());
    }

    default BlockEntityLike blockEntityAt(BlockPosition position) {
        BlockEntity entity = cast().getBlockEntity(position.cast());
        if (entity == null) {
            throw new IllegalStateException("No block entity present for such position (yet): " + position);
        }
        if (entity.isRemoved()) {
            throw new IllegalStateException("Block entity already removed for such position: " + position);

        }
        if (entity instanceof BlockEntityImpl.MyBlockEntity) {  // Created in PythonCraft!
            return ((BlockEntityImpl.MyBlockEntity) entity).getParent();
        }

        return new VanillaBlockEntityImpl(entity, this);
    }
}
