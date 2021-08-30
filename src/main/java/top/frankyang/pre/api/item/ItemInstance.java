package top.frankyang.pre.api.item;

import net.minecraft.item.ItemStack;
import top.frankyang.pre.api.misc.DelegatedCastable;
import top.frankyang.pre.api.nbt.NbtObject;
import top.frankyang.pre.api.text.RichTextImpl;

public class ItemInstance extends DelegatedCastable<ItemStack> {
    public ItemInstance(ItemStack delegate) {
        super(delegate);
    }  // TODO wrap enchantments

    public RichTextImpl getName() {
        return new RichTextImpl(delegate.getName());
    }

    public int getCoolDownTime() {
        return delegate.getCooldown();
    }

    public void setCoolDownTime(int value) {
        delegate.setCooldown(value);
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
        delegate.increment(value);
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
        delegate.decrement(value);
    }

    public int getCount() {
        return delegate.getCount();
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
        delegate.setCount(value);
    }

    public int getMaxCount() {
        return delegate.getMaxCount();
    }

    public boolean isFood() {
        return delegate.getItem().isFood();
    }

    public String getDrinkSoundId() {
        return delegate.getItem().getDrinkSound().getId().toString();
    }

    public String getEatSoundId() {
        return delegate.getItem().getEatSound().getId().toString();
    }

    public int getRepairCost() {
        return delegate.getMaxUseTime();
    }

    public void setRepairCost(int value) {
        delegate.setRepairCost(value);
    }

    public int getDamage() {
        return delegate.getDamage();
    }

    public void setDamage(int value) {
        delegate.setDamage(value);
    }

    public int getMaxDamage() {
        return delegate.getMaxDamage();
    }

    public boolean isDamageable() {
        return getMaxDamage() > 0;
    }

    public double getDamagePercentage() {
        return (double) getDamage() / getMaxDamage();
    }

    public NbtObject getNbt() {
        return NbtObject.of(delegate.getOrCreateTag());
    }
}
