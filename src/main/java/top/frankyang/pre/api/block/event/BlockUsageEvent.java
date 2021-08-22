package top.frankyang.pre.api.block.event;

import top.frankyang.pre.api.math.Facing;
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.block.type.BlockType;
import top.frankyang.pre.api.entity.PlayerLike;
import top.frankyang.pre.api.event.EventType;
import top.frankyang.pre.api.world.WorldLike;

public class BlockUsageEvent extends BlockInteractionEvent {
    private final boolean mainHand;
    private final Facing surface;

    public BlockUsageEvent(BlockType blockType, BlockPosition position, WorldLike world, PlayerLike<?> player, BlockStateLike state, boolean mainHand, Facing surface) {
        super(EventType.BLOCK_USAGE, blockType, position, world, player, state);
        this.mainHand = mainHand;
        this.surface = surface;
    }

    public boolean isMainHand() {
        return mainHand;
    }

    public Facing getSurface() {
        return surface;
    }
}
