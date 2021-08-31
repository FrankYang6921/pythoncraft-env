package top.frankyang.pre.api.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import top.frankyang.pre.api.item.food.Food;
import top.frankyang.pre.api.item.food.FoodLike;
import top.frankyang.pre.api.item.group.GroupLike;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

/**
 * 包装类，包装原版类{@link FabricItemSettings}。
 */
public class ItemSettings extends CastableImpl<FabricItemSettings> {
    public ItemSettings() {
        super(new FabricItemSettings());
    }

    public ItemSettings preferredArmorSlot(String armorSlot) {
        return preferredArmorSlot(ArmorSlot.valueOf(armorSlot));
    }

    public ItemSettings preferredArmorSlot(ArmorSlot armorSlot) {
        casted.equipmentSlot(i -> armorSlot.cast());
        return this;
    }

    public ItemSettings food(FoodLike food) {
        casted.food(food.cast());
        return this;
    }

    public ItemSettings food(Food.Builder food) {
        return food(food.build());
    }

    public ItemSettings maxCount(int maxCount) {
        casted.maxCount(maxCount);
        return this;
    }

    public ItemSettings maxDamageIfAbsent(int maxDamage) {
        casted.maxDamageIfAbsent(maxDamage);
        return this;
    }

    public ItemSettings maxDamage(int maxDamage) {
        casted.maxDamage(maxDamage);
        return this;
    }

    public ItemSettings recipeRemainder(Item recipeRemainder) {  // TODO wrap class `Item`
        casted.recipeRemainder(recipeRemainder);
        return this;
    }

    public ItemSettings group(GroupLike group) {
        casted.group(group.cast());
        return this;
    }

    public ItemSettings rarity(String rarity) {
        casted.rarity(Rarity.valueOf(rarity));
        return this;
    }

    public ItemSettings fireproof() {
        casted.fireproof();
        return this;
    }
}
