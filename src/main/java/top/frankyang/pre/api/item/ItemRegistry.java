package top.frankyang.pre.api.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.python.core.PyDictionary;
import top.frankyang.pre.api.AbstractRegistry;
import top.frankyang.pre.api.misc.Convertable;

/**
 * 物品注册表。在某一个PythonCraft包的命名空间内注册物品。
 */
public final class ItemRegistry extends AbstractRegistry {
    public ItemRegistry(String namespace) {
        super(namespace);
    }

    /**
     * 查询一个物品，不限于当前模组的命名空间。
     *
     * @param id 所查询物品的完整命名空间ID。
     * @return 查询到的物品。
     */
    public static Item lookup(Identifier id) {
        Item item = Registry.ITEM.get(id);
        if (item == Items.AIR)
            throw new IllegalArgumentException("Cannot find the item for the identifier: " + id);
        return item;
    }

    /**
     * 注册一个物品。
     *
     * @param id   所注册物品的命名空间ID。
     * @param item 所要注册的物品实例。
     */
    public synchronized void registerItem(String id, Object item) {
        Registry.register(Registry.ITEM, getIdentifier(id), Convertable.convert(item, Item.class));
    }

    /**
     * 注册一个物品。该方法仅适用于创建一个简单的物品。
     *
     * @param id           所注册物品的命名空间ID。
     * @param itemSettings 所要注册的物品设置（Python字典，会自动解析设置并创建物品实例）。
     */
    public synchronized void registerItem(String id, PyDictionary itemSettings) {
        Registry.register(Registry.ITEM, getIdentifier(id), new Item(ItemSettings.parse(itemSettings).convert()));
    }

    /**
     * 查询一个物品，不限于当前模组的命名空间。
     *
     * @param id 所查询物品的命名空间ID。
     * @return 查询到的物品。
     */
    public Item lookup(String id) {
        if (id.contains(":"))
            return lookup(new Identifier(id));
        return lookup(getIdentifier(id));
    }
}
