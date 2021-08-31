package top.frankyang.pre.api.item;

import net.minecraft.item.ItemStack;
import top.frankyang.pre.api.item.type.ItemType;
import top.frankyang.pre.api.misc.conversion.CastableImpl;
import top.frankyang.pre.api.nbt.NbtObject;
import top.frankyang.pre.api.text.RichTextImpl;

public class ItemInstance extends CastableImpl<ItemStack> {
    public ItemInstance(ItemStack delegate) {
        super(delegate);
    }  // TODO wrap enchantments

    public RichTextImpl getName() {
        return new RichTextImpl(casted.getName());
    }

    public ItemType getItemType() {
        if (ItemType.isPythonCraft(casted.getItem()))
            return ItemType.ofVanilla(casted.getItem());
        throw new UnsupportedOperationException();  // TODO make a simple wrapper
    }

    public int getCoolDownTime() {
        return casted.getCooldown();
    }

    public void setCoolDownTime(int value) {
        casted.setCooldown(value);
    }

    public void supply(int value) {
        supply(value, false);
    }

    public void supply() {
        supply(1, false);
    }

    public void supply(int value, boolean force) {
        if (!force && getCount() + 1 > getMaxCount()) {
            throw new IllegalStateException("Too many items: " + (getCount() + 1) + " > " + getMaxCount() + ".");
        }
        casted.increment(value);
    }

    public void consume(int value) {
        consume(value, false);
    }

    public void consume() {
        consume(1, false);
    }

    public void consume(int value, boolean force) {
        if (!force && getCount() - 1 <= 0) {
            throw new IllegalStateException("Too few items: " + (getCount() + 1) + " (must be positive) <= 0.");
        }
        casted.decrement(value);
    }

    public int getCount() {
        return casted.getCount();
    }

    public void setCount(int value) {
        setCount(value, false);
    }

    public void setCount(int value, boolean force) {
        if (!force && value > getMaxCount()) {
            throw new IllegalStateException("Too many items: " + value + " > " + getMaxCount() + ".");
        }
        if (!force && value <= 0) {
            throw new IllegalStateException("Too few items: " + value + " (must be positive) <= 0.");
        }
        casted.setCount(value);
    }

    public int getMaxCount() {
        return casted.getMaxCount();
    }

    public boolean isFood() {
        return casted.getItem().isFood();
    }

    public String getDrinkSoundId() {
        return casted.getItem().getDrinkSound().getId().toString();
    }

    public String getEatSoundId() {
        return casted.getItem().getEatSound().getId().toString();
    }

    public int getRepairCost() {
        return casted.getMaxUseTime();
    }

    public void setRepairCost(int value) {
        casted.setRepairCost(value);
    }

    public int getDamage() {
        return casted.getDamage();
    }

    public void setDamage(int value) {
        casted.setDamage(value);
    }

    public int getMaxDamage() {
        return casted.getMaxDamage();
    }

    public boolean isDamageable() {
        return getMaxDamage() > 0;
    }

    public double getDamagePercentage() {
        return (double) getDamage() / getMaxDamage();
    }

    public NbtObject getNbt() {
        return NbtObject.of(casted.getOrCreateTag());
    }
}
