package top.frankyang.pre.api.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import top.frankyang.pre.api.item.ArmorSlot;
import top.frankyang.pre.api.item.ItemInstance;

import java.util.HashSet;
import java.util.Set;

public interface CreatureLike<T extends LivingEntity> extends EntityLike<T> {  // TODO add status effects
    default float getHealthLevel() {
        return cast().getHealth();
    }

    default float getMaxHealthLevel() {
        return cast().getMaxHealth();
    }

    default double getHealthPercentage() {
        return getHealthLevel() / getMaxHealthLevel();
    }

    default boolean isBreathableInWater() {
        return cast().canBreatheInWater();
    }

    default boolean isImmature() {
        return cast().isBaby();
    }

    default boolean isAlive() {
        return cast().isAlive();
    }

    default void kill() {
        cast().kill();
    }

    default int getLastAttackedTime() {
        return cast().getLastAttackedTime();
    }

    default boolean isMainHand() {
        return cast().getActiveHand() == Hand.MAIN_HAND;
    }

    default ItemInstance getItemInMainHand() {
        return new ItemInstance(cast().getStackInHand(Hand.MAIN_HAND));
    }

    default ItemInstance getItemInOffHand() {
        return new ItemInstance(cast().getStackInHand(Hand.OFF_HAND));
    }

    default Set<ItemInstance> getItemInHands() {
        Set<ItemInstance> target = new HashSet<>();
        for (ItemStack itemStack : cast().getItemsHand()) {
            target.add(new ItemInstance(itemStack));
        }
        return target;
    }

    default Set<ItemInstance> getArmorItems() {
        Set<ItemInstance> target = new HashSet<>();
        for (ItemStack itemStack : cast().getArmorItems()) {
            target.add(new ItemInstance(itemStack));
        }
        return target;
    }

    default ItemInstance getItemInArmorSlot(ArmorSlot armorSlot) {
        return new ItemInstance(cast().getEquippedStack(armorSlot.cast()));
    }

    default void putToMainHand(ItemInstance instance) {
        cast().setStackInHand(Hand.MAIN_HAND, instance.cast());
    }

    default void putToOffHand(ItemInstance instance) {
        cast().setStackInHand(Hand.OFF_HAND, instance.cast());
    }

    default void putToArmorSlot(ItemInstance instance, ArmorSlot armorSlot) {
        cast().equipStack(armorSlot.cast(), instance.cast());
    }

    default boolean putToInventorySlot(int slotIndex, ItemInstance instance) {
        return cast().equip(slotIndex, instance.cast());
    }

    default void ensurePutToSlot(int slotIndex, ItemInstance instance) {
        if (!putToInventorySlot(slotIndex, instance))
            throw new UnsupportedOperationException("Failed to put item " + instance + " to slot " + slotIndex + " of " + this);
    }

    default boolean canBeEquippedWith(ItemInstance instance) {
        return cast().canEquip(instance.cast());
    }
}
