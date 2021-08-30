package top.frankyang.pre.api.entity;

import net.minecraft.entity.mob.MobEntity;

public interface MobLike<T extends MobEntity> extends CreatureLike<T> {
    default CreatureLike<?> getTarget() {
        return new CreatureImpl(cast().getTarget());
    }

    default void setTarget(CreatureLike<?> value) {
        cast().setTarget(value.cast());
    }

    default boolean canTargetTo(CreatureLike<?> value) {
        return cast().canTarget(value.cast());
    }  // TODO add entity type!!
}
