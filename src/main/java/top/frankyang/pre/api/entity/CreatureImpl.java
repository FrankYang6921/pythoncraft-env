package top.frankyang.pre.api.entity;

import net.minecraft.entity.LivingEntity;
import top.frankyang.pre.api.misc.DelegatedCastable;

class CreatureImpl extends DelegatedCastable<LivingEntity> implements CreatureLike<LivingEntity> {
    public CreatureImpl(LivingEntity delegate) {
        super(delegate);
    }
}
