package top.frankyang.pre.api.world;

import net.minecraft.world.WorldAccess;
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.entity.BlockEntityLike;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.misc.Castable;

public interface WorldLike extends Castable<WorldAccess> {
    int lightLevelAt(BlockPosition position);

    int getDifficulty();

    float difficultyAt(BlockPosition position);

    BlockStateLike blockStateAt(BlockPosition position);

    BlockEntityLike blockEntityAt(BlockPosition position);


}
