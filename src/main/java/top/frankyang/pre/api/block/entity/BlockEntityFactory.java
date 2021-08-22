package top.frankyang.pre.api.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import top.frankyang.pre.api.block.type.BlockType;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * 方块实体工厂。该类负责生成新的方块实体。
 */
public abstract class BlockEntityFactory implements Supplier<BlockEntity> {
    private BlockEntityType<?> type;

    /**
     * 创造属于该方块实体的类型。
     *
     * @param blockType 该方块实体所属的方块类。
     * @return 创建的方块实体类型。
     */
    public BlockEntityType<?> newType(BlockType blockType) {
        if (type != null) {
            throw new IllegalStateException("Type already created.");
        }
        return type = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            "pythoncraft:" + UUID.randomUUID().toString().replace('-', '_'),
            BlockEntityType.Builder.create(this, blockType.cast()).build(null));
    }

    /**
     * 获取属于该方块实体的类型。
     *
     * @return 方块实体类型。
     */
    public BlockEntityType<?> getType() {
        if (type == null) {
            throw new IllegalStateException("Type not created yet.");
        }
        return type;
    }

    @Override
    public BlockEntity get() {
        return newBlockEntity(this).cast();
    }

    /**
     * 创建一个方块实体。
     *
     * @param factory 这个实例。
     * @return 创建的方块实体。
     */
    public abstract BlockEntityLike newBlockEntity(BlockEntityFactory factory);
}
