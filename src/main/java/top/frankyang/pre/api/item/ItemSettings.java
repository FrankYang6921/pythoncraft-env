package top.frankyang.pre.api.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.python.core.PyDictionary;
import top.frankyang.pre.api.item.food.Food;
import top.frankyang.pre.api.item.food.FoodLike;
import top.frankyang.pre.api.item.food.VanillaFood;
import top.frankyang.pre.api.item.group.Group;
import top.frankyang.pre.api.item.group.GroupLike;
import top.frankyang.pre.api.item.group.VanillaGroup;
import top.frankyang.pre.api.misc.DelegatedCastable;
import top.frankyang.pre.api.util.TypedDictionary;

/**
 * 包装类，包装原版类{@link FabricItemSettings}。
 */
public class ItemSettings extends DelegatedCastable<FabricItemSettings> {
    protected ItemSettings(FabricItemSettings delegate) {
        super(delegate);
    }

    /**
     * 将一个Python字典解析为物品设置。
     *
     * @param dictionary 用于解析的Python字典。
     * @return 解析后的<code>ItemSettings</code>实例
     */
    @SuppressWarnings("CodeBlock2Expr")
    public static ItemSettings parse(PyDictionary dictionary) {
        TypedDictionary dict = new TypedDictionary(dictionary);
        FabricItemSettings fabricItemSettings = new FabricItemSettings();

        dict.ifTrue("fireproof", fabricItemSettings::fireproof);
        dict.ifPresent("group", GroupLike.class, g -> {
            fabricItemSettings.group(g.cast());
        });
        dict.ifPresent("group", PyDictionary.class, d -> {
            fabricItemSettings.group(Group.of(d).cast());
        });
        dict.ifPresent("group", String.class, s -> {
            fabricItemSettings.group(VanillaGroup.valueOf(s).cast());
        });
        dict.ifPresent("rarity", String.class, s -> {
            fabricItemSettings.rarity(Rarity.valueOf(s));
        });
        dict.ifPresent("equipmentSlot", String.class, s -> {
            fabricItemSettings.equipmentSlot(i -> EquipmentSlot.valueOf(s));
        });
        dict.ifPresent("food", FoodLike.class, f -> {
            fabricItemSettings.food(f.cast());
        });
        dict.ifPresent("food", PyDictionary.class, d -> {
            fabricItemSettings.food(Food.of(d).cast());
        });
        dict.ifPresent("food", String.class, s -> {
            fabricItemSettings.food(VanillaFood.valueOf(s).cast());
        });
        dict.ifPresent("maxCount", Number.class, n -> {
            fabricItemSettings.maxCount(n.intValue());
        });
        dict.ifPresent("maxDamage", Number.class, n -> {
            fabricItemSettings.maxDamage(n.intValue());
        });
        dict.ifPresent("recipeRemainder", Item.class, fabricItemSettings::recipeRemainder);  // TODO 包装掉Item类
        dict.ifPresent("recipeRemainder", String.class, s -> {
            Item item = Registry.ITEM.get(new Identifier(s));
            if (item != Items.AIR)
                fabricItemSettings.recipeRemainder(item);
        });
        // Allows a identifier or an instance.

        return new ItemSettings(fabricItemSettings);
    }
}
