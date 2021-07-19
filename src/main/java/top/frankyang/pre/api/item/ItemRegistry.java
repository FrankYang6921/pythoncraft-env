package top.frankyang.pre.api.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.python.core.PyDictionary;
import top.frankyang.pre.api.AbstractRegistry;

/**
 * 物品注册表。在某一个PythonCraft包的命名空间内注册物品。
 */
public class ItemRegistry extends AbstractRegistry {
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
    public void registerItem(String id, Item item) {
        Registry.register(Registry.ITEM, getIdentifier(id), item);
    }

    /**
     * 注册一个物品。该方法仅适用于创建一个简单的物品。
     *
     * @param id           所注册物品的命名空间ID。
     * @param itemSettings 所要注册的物品设置（Python字典，会自动解析设置并创建物品实例）。
     */
    public void registerItem(String id, PyDictionary itemSettings) {
        Registry.register(Registry.ITEM, getIdentifier(id), new Item(ItemSettings.parse(itemSettings)));
    }

    /**
     * 注册一个物品组。
     *
     * @param id     物品组的命名空间ID。
     * @param iconId 物品组图标所对应的物品的命名空间ID。
     * @return 注册的物品组。
     */
    public ItemGroup registerGroup(String id, String iconId) {
        return FabricItemGroupBuilder.create(getIdentifier(id)).icon(() -> new ItemStack(lookup(iconId))).build();
    }

    /**
     * 注册一个物品组。
     *
     * @param id     物品组的命名空间ID。
     * @param iconId 物品组图标所对应的物品的命名空间ID。
     * @return 注册的物品组。
     */
    public ItemGroup registerGroup(String id, Identifier iconId) {
        return FabricItemGroupBuilder.create(getIdentifier(id)).icon(() -> new ItemStack(lookup(iconId))).build();
    }

    /**
     * 查询一个物品，不限于当前模组的命名空间。
     *
     * @param id 所查询物品的命名空间ID。
     * @return 查询到的物品。
     */
    public Item lookup(String id) {
        if (id.contains(":")) return
            lookup(new Identifier(id));
        return lookup(getIdentifier(id));
    }
}
