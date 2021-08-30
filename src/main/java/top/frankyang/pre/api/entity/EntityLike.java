package top.frankyang.pre.api.entity;

import net.minecraft.entity.Entity;
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.math.Vector3d;
import top.frankyang.pre.api.misc.Castable;
import top.frankyang.pre.api.world.WorldImpl;
import top.frankyang.pre.api.world.WorldLike;
import top.frankyang.pre.mixin.reflect.EntityInvoker;

import java.util.Collection;
import java.util.stream.Collectors;

public interface EntityLike<T extends Entity> extends Castable<T> {
    default WorldLike getWorld() {
        return new WorldImpl(cast().getEntityWorld());
    }

    default String getName() {
        return cast().getEntityName();
    }

    default String getFriendlyName() {
        return cast().getName().toString();
    }

    default int getOxygenLevel() {
        return cast().getAir();
    }

    default void setOxygenLevel(int value) {
        cast().setAir(value);
    }

    default int getMaxOxygenLevel() {
        return cast().getMaxAir();
    }

    default double getOxygenPercentage() {
        return (double) getOxygenLevel() / getMaxOxygenLevel();
    }

    default BlockPosition getBlockPosition() {
        return new BlockPosition(cast().getBlockPos());
    }

    default Vector3d getPosition() {
        return new Vector3d(cast().getX(), cast().getY(), cast().getZ());
    }

    default void setPosition(Vector3d value) {
        cast().setPos(value.getX(), value.getY(), value.getZ());
    }

    default boolean isAttackable() {
        return cast().isAttackable();
    }

    default boolean isRideableInWater() {
        return cast().canBeRiddenInWater();
    }

    default boolean isSneaking() {
        return cast().isSneaking();
    }

    default void setSneaking(boolean value) {
        cast().setSneaking(value);
    }

    default boolean isRunning() {
        return cast().isSprinting();
    }

    default void setRunning(boolean value) {
        cast().setSprinting(value);
    }

    default boolean isSwimming() {
        return cast().isSwimming();
    }

    default void setSwimming(boolean value) {
        cast().setSwimming(value);
    }

    default boolean isGlowing() {
        return cast().isGlowing();
    }

    default void setGlowing(boolean value) {
        cast().setGlowing(value);
    }

    default Vector3d getVelocity() {
        return new Vector3d(cast().getVelocity());
    }

    default void setVelocity(Vector3d value) {
        cast().setVelocity(value.cast());  // Technically faster than 3 separate arguments
    }

    default void addVelocity(Vector3d value) {
        cast().addVelocity(value.getX(), value.getY(), value.getZ());
    }

    default Collection<EntityLike<?>> getAllPassengers() {
        return cast().getPassengersDeep().stream().map(EntityImpl::new).collect(Collectors.toSet());
    }

    default Collection<EntityLike<?>> getPassengers() {
        return cast().getPassengerList().stream().map(EntityImpl::new).collect(Collectors.toSet());
    }

    default boolean hasPassenger(EntityLike<?> entity) {
        return cast().hasPassenger(entity.cast());
    }  // TODO add entity type!

    default float getWidth() {
        return cast().getWidth();
    }

    default float getHeight() {
        return cast().getHeight();
    }

    default int getPermissionLevel() {
        return ((EntityInvoker) cast()).invokeGetPermissionLevel();
    }

    default boolean hasPermissionLevel(int value) {
        return getPermissionLevel() >= value;
    }
}
