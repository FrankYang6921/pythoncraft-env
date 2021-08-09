package top.frankyang.pre.api.block.event;

import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.entity.PlayerLike;
import top.frankyang.pre.api.event.EventType;
import top.frankyang.pre.api.util.reflection.MethodContainer;
import top.frankyang.pre.api.world.WorldLike;

public class BlockBreakageEvent extends BlockEvent<Void> {
    private final EventType type;
    private WorldLike world;
    private BlockPosition position;
    private PlayerLike<?> source;

    public BlockBreakageEvent(EventType type, MethodContainer superMethod, Object... args) {
        super(superMethod, args);
        this.type = type;
    }


    @Override
    public EventType getType() {
        return type;
    }
}
