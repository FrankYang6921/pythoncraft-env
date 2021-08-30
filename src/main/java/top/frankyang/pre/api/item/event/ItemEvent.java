package top.frankyang.pre.api.item.event;

import top.frankyang.pre.api.event.Event;
import top.frankyang.pre.api.item.type.ItemType;

import java.util.Objects;

public abstract class ItemEvent implements Event {
    protected final ItemType itemType;

    protected ItemEvent(ItemType itemType) {
        this.itemType = Objects.requireNonNull(itemType);
    }

    public ItemType getItemType() {
        return itemType;
    }
}
