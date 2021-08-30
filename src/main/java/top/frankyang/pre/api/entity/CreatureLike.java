package top.frankyang.pre.api.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

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


}
