package top.frankyang.pre.api.entity;

import net.minecraft.entity.player.PlayerEntity;
import top.frankyang.pre.api.item.ItemInstance;
import top.frankyang.pre.mixin.reflect.HungerManagerAccessor;

public interface PlayerLike<T extends PlayerEntity> extends CreatureLike<T> {
    default int getExperienceLevel() {
        return cast().experienceLevel;
    }

    default void setExperienceLevel(int value) {
        addExperienceLevel(value - getExperienceLevel());
    }

    default void addExperienceLevel(int value) {
        cast().addExperienceLevels(value);
    }

    default int getExperiencePoint() {
        return cast().totalExperience;
    }

    default void setExperiencePoint(int value) {
        addExperiencePoint(value - getExperienceLevel());
    }

    default void addExperiencePoint(int value) {
        cast().addExperience(value);
    }

    default boolean isSleeping() {
        return cast().isSleeping();
    }

    default boolean isSpectator() {
        return cast().isSpectator();
    }

    default boolean isCreative() {
        return cast().isCreative();
    }

    default boolean isAdministrator() {
        return hasPermissionLevel(2);
    }

    default int getFoodLevel() {
        return cast().getHungerManager().getFoodLevel();
    }

    default void setFoodLevel(int value) {
        cast().getHungerManager().setFoodLevel(value);
    }

    default float getSaturationLevel() {
        return cast().getHungerManager().getSaturationLevel();
    }

    default void setSaturationLevel(float value) {
        ((HungerManagerAccessor) cast().getHungerManager()).setSaturationLevel(value);
    }
}
