package top.frankyang.pre.api.item;

import net.minecraft.entity.EquipmentSlot;
import top.frankyang.pre.api.misc.conversion.Castable;

public enum ArmorSlot implements Castable<EquipmentSlot> {
    FEET(EquipmentSlot.FEET),
    LEGS(EquipmentSlot.LEGS),
    CHEST(EquipmentSlot.CHEST),
    HEAD(EquipmentSlot.HEAD);

    private final EquipmentSlot delegate;

    ArmorSlot(EquipmentSlot delegate) {
        this.delegate = delegate;
    }

    @Override
    public EquipmentSlot cast() {
        return delegate;
    }
}
