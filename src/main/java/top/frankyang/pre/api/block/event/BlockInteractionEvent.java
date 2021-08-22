package top.frankyang.pre.api.block.event;

import org.jetbrains.annotations.NotNull;
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.block.type.BlockType;
import top.frankyang.pre.api.entity.PlayerLike;
import top.frankyang.pre.api.event.EventType;
import top.frankyang.pre.api.world.WorldLike;

import java.util.Objects;

public class BlockInteractionEvent extends BlockEvent {
    private final EventType type;
    private final WorldLike world;
    private final PlayerLike<?> player;
    private final BlockStateLike state;

    public BlockInteractionEvent(EventType type, BlockType blockType, BlockPosition position, WorldLike world, PlayerLike<?> player, BlockStateLike state) {
        super(blockType, position);
        this.type = Objects.requireNonNull(type);
        this.world = world;
        this.player = player;
        this.state = state;
    }

    public @NotNull WorldLike getWorld() {
        return Objects.requireNonNull(world, "No world present for such event.");
    }

    public @NotNull PlayerLike<?> getPlayer() {
        return Objects.requireNonNull(player, "No player present for such event.");
    }

    public @NotNull BlockStateLike getState() {
        return Objects.requireNonNull(state, "No state present for such event.");
    }

    @Override
    public EventType getType() {
        return type;
    }
}
