package top.frankyang.pre.api.entity;

import net.minecraft.entity.LivingEntity;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

class CreatureImpl extends CastableImpl<LivingEntity> implements CreatureLike<LivingEntity> {
    public CreatureImpl(LivingEntity delegate) {
        super(delegate);
    }
}
