package top.frankyang.pre.api.block.event;

import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.type.BlockType;
import top.frankyang.pre.api.event.Event;

import java.util.Objects;

public abstract class BlockEvent implements Event {
    protected final BlockType blockType;
    protected final BlockPosition position;

    public BlockEvent(BlockType blockType, BlockPosition position) {
        this.blockType = Objects.requireNonNull(blockType);
        this.position = position;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public BlockPosition getPosition() {
        return Objects.requireNonNull(position, "No position present for such event.");
    }
}
