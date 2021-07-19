package top.frankyang.pre.api.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.python.core.PyDictionary;
import top.frankyang.pre.api.util.MagicConstants;
import top.frankyang.pre.api.util.TypedDictionary;

public final class ItemSettings {
    private static final MagicConstants<ItemGroup> itemGroups = new MagicConstants<>(ItemGroup.class);

    private ItemSettings() {
    }

    /**
     * 将一个Python字典解析为物品设置。
     *
     * @param dictionary 用于解析的Python字典。
     * @return 解析后的<code>FabricItemSettings</code>实例
     */
    @SuppressWarnings("CodeBlock2Expr")
    public static FabricItemSettings parse(PyDictionary dictionary) {
        TypedDictionary dict = new TypedDictionary(dictionary);
        FabricItemSettings fabricItemSettings = new FabricItemSettings();

        dict.ifPresent("group", String.class, s -> {
            fabricItemSettings.group(itemGroups.get(s));
        });
        dict.ifPresent("group", ItemGroup.class, fabricItemSettings::group);
        // Allows a magic constant or an instance.
        if (dict.getOrDefault("fireproof", Boolean.class, false)) {
            fabricItemSettings.fireproof();
        }
        dict.ifPresent("equipmentSlot", String.class, s -> {
            fabricItemSettings.equipmentSlot(i -> EquipmentSlot.valueOf(s));
        });
        dict.ifPresent("equipmentSlot", EquipmentSlot.class, e -> {
            fabricItemSettings.equipmentSlot(i -> e);
        });
        // Allows a enum key or an enum element.
        dict.ifPresent("rarity", String.class, s -> {
            fabricItemSettings.rarity(Rarity.valueOf(s));
        });
        dict.ifPresent("rarity", Rarity.class, fabricItemSettings::rarity);
        // Allows a enum key or an enum element.
        dict.ifPresent("food", FoodComponent.class, fabricItemSettings::food);
        dict.ifPresent("maxCount", Integer.class, fabricItemSettings::maxCount);
        dict.ifPresent("maxDamage", Integer.class, fabricItemSettings::maxDamage);
        dict.ifPresent("recipeRemainder", Item.class, fabricItemSettings::recipeRemainder);
        dict.ifPresent("recipeRemainder", String.class, s -> {
            Item item = Registry.ITEM.get(new Identifier(s));
            if (item != Items.AIR)
                fabricItemSettings.recipeRemainder(item);
        });
        // Allows a identifier or an instance.

        return fabricItemSettings;
    }
}
