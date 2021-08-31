package top.frankyang.pre.api.item.event;

import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.entity.PlayerLike;
import top.frankyang.pre.api.event.EventType;
import top.frankyang.pre.api.item.ItemInstance;
import top.frankyang.pre.api.item.type.ItemType;
import top.frankyang.pre.api.math.Facing;
import top.frankyang.pre.api.world.WorldLike;

import java.util.ArrayList;

public class ItemUsageOnBlockEvent extends ItemUsageEvent {
    private final BlockPosition position;
    private final Facing surface;

    public ItemUsageOnBlockEvent(ItemType itemType, WorldLike world, PlayerLike<?> player, BlockPosition position, boolean mainHand, ItemInstance instance, Facing surface) {
        super(itemType, world, player, mainHand, instance);
        this.position = position;
        this.surface = surface;
    }

    public BlockPosition getPosition() {
        return position;
    }

    public Facing getSurface() {
        return surface;
    }

    @Override
    public EventType getType() {
        return EventType.ITEM_USAGE_ON_BLOCK;
    }
}
