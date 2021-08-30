package top.frankyang.pre.api.item.event;

import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.entity.PlayerLike;
import top.frankyang.pre.api.event.EventType;
import top.frankyang.pre.api.item.type.ItemType;
import top.frankyang.pre.api.world.WorldLike;

public class ItemUsageEvent extends ItemEvent {
    private final WorldLike world;
    private final PlayerLike<?> player;
    private final BlockPosition position;
    private final boolean mainHand;

    public ItemUsageEvent(ItemType itemType, WorldLike world, PlayerLike<?> player, BlockPosition position, boolean mainHand) {
        super(itemType);
        this.world = world;
        this.player = player;
        this.position = position;
        this.mainHand = mainHand;
    }

    public WorldLike getWorld() {
        return world;
    }

    public PlayerLike<?> getPlayer() {
        return player;
    }

    public BlockPosition getPosition() {
        return position;
    }

    public boolean isMainHand() {
        return mainHand;
    }

    @Override
    public EventType getType() {
        return EventType.ITEM_USAGE;
    }
}
