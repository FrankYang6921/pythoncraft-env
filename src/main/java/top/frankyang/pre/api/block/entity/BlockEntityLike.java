package top.frankyang.pre.api.block.entity;

import net.minecraft.block.entity.BlockEntity;
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.misc.conversion.Castable;
import top.frankyang.pre.api.nbt.NbtObject;
import top.frankyang.pre.api.world.WorldLike;
import top.frankyang.pre.api.world.WorldImpl;

/**
 * 方块实体类，方块实体可以储存比方块状态更复杂的数据。包装原版类{@link BlockEntity}
 */
public interface BlockEntityLike extends Castable<BlockEntity> {
    /**
     * 序列化一个方块实体所含数据到一个NBT表。
     *
     * @param object 目标NBT表。
     * @return object
     */
    NbtObject serialize(NbtObject object);

    /**
     * 反序列化一个NBT表到一个方块实体所含数据。
     *
     * @param state  当前的方块状态。
     * @param object 目标NBT表。
     */
    void deserialize(BlockStateLike state, NbtObject object);

    /**
     * 要求持久化。由于在Minecraft中方块实体的数据是缓存的，因此如果您修改了方块实体中的数据，应当调用此方法要求Minecraft对该方块实体进行持久化。否则您的修改很有可能会丢失。
     */
    default void acquirePersistence() {
        cast().markDirty();
    }

    /**
     * 获取当前方块实体所对应的方块状态。
     *
     * @return 方块状态。
     */
    BlockStateLike getState();

    /**
     * 获取当前方块实体所对应的方块实体工厂。
     *
     * @return 方块实体工厂。
     */
    BlockEntityFactory getFactory();

    /**
     * 获取当前方块实体所在的位置。
     *
     * @return 方块位置。
     */
    default BlockPosition getPosition() {
        return new BlockPosition(cast().getPos());
    }

    /**
     * 获取当前方块实体所在的世界。
     *
     * @return 可访问的世界。
     */
    default WorldLike getWorld() {
        return new WorldImpl(cast().getWorld());
    }
}
