package top.frankyang.pre.api.block.event;

import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.type.BlockType;
import top.frankyang.pre.api.entity.EntityLike;
import top.frankyang.pre.api.event.EventType;
import top.frankyang.pre.api.world.WorldLike;

import java.util.Objects;

public class EntityPhysicsEvent extends BlockEvent {
    private final EventType type;
    private final WorldLike world;
    private final EntityLike<?> entity;

    public EntityPhysicsEvent(EventType type, BlockType blockType, BlockPosition position, WorldLike world, EntityLike<?> entity) {
        super(blockType, position);
        this.type = Objects.requireNonNull(type);
        this.world = world;
        this.entity = entity;
    }

    public WorldLike getWorld() {
        return world;
    }

    public EntityLike<?> getEntity() {
        return entity;
    }

    @Override
    public EventType getType() {
        return type;
    }
}
