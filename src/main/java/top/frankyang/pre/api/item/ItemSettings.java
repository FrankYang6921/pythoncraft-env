package top.frankyang.pre.api.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import top.frankyang.pre.api.item.food.Food;
import top.frankyang.pre.api.item.food.FoodLike;
import top.frankyang.pre.api.item.group.GroupLike;
import top.frankyang.pre.api.misc.DelegatedCastable;

/**
 * 包装类，包装原版类{@link FabricItemSettings}。
 */
public class ItemSettings extends DelegatedCastable<FabricItemSettings> {
    public ItemSettings() {
        super(new FabricItemSettings());
    }

    public ItemSettings equipmentSlot(String equipmentSlot) {
        delegate.equipmentSlot(i -> EquipmentSlot.valueOf(equipmentSlot));
        return this;
    }

    public ItemSettings food(FoodLike food) {
        delegate.food(food.cast());
        return this;
    }

    public ItemSettings food(Food.Builder food) {
        delegate.food(food.build().cast());
        return this;
    }

    public ItemSettings maxCount(int maxCount) {
        delegate.maxCount(maxCount);
        return this;
    }

    public ItemSettings maxDamageIfAbsent(int maxDamage) {
        delegate.maxDamageIfAbsent(maxDamage);
        return this;
    }

    public ItemSettings maxDamage(int maxDamage) {
        delegate.maxDamage(maxDamage);
        return this;
    }

    public ItemSettings recipeRemainder(Item recipeRemainder) {  // TODO wrap class `Item`
        delegate.recipeRemainder(recipeRemainder);
        return this;
    }

    public ItemSettings group(GroupLike group) {
        delegate.group(group.cast());
        return this;
    }

    public ItemSettings rarity(String rarity) {
        delegate.rarity(Rarity.valueOf(rarity));
        return this;
    }

    public ItemSettings fireproof() {
        delegate.fireproof();
        return this;
    }
}
