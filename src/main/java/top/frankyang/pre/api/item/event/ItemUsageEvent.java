package top.frankyang.pre.api.item.event;

import top.frankyang.pre.api.entity.PlayerLike;
import top.frankyang.pre.api.event.EventType;
import top.frankyang.pre.api.item.ItemInstance;
import top.frankyang.pre.api.item.type.ItemType;
import top.frankyang.pre.api.world.WorldLike;

public class ItemUsageEvent extends ItemEvent {
    private final WorldLike world;
    private final PlayerLike<?> player;
    private final boolean mainHand;
    private final ItemInstance instance;

    public ItemUsageEvent(ItemType itemType, WorldLike world, PlayerLike<?> player, boolean mainHand, ItemInstance instance) {
        super(itemType);
        this.world = world;
        this.player = player;
        this.mainHand = mainHand;
        this.instance = instance;
    }

    public WorldLike getWorld() {
        return world;
    }

    public PlayerLike<?> getPlayer() {
        return player;
    }

    public boolean isMainHand() {
        return mainHand;
    }

    public ItemInstance getInstance() {
        return instance;
    }

    @Override
    public EventType getType() {
        return EventType.ITEM_USAGE;
    }
}
