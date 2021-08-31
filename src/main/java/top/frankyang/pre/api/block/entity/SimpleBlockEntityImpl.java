package top.frankyang.pre.api.block.entity;

import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.nbt.NbtObject;

/**
 * 简单的方块实体。它持有一个NBT表，您可以自由使用这个NBT表。
 */
class SimpleBlockEntityImpl extends BlockEntityImpl {
    private final NbtObject nbt;

    protected SimpleBlockEntityImpl(SimpleBlockEntityFactory factory, NbtObject nbt) {
        super(factory);
        this.nbt = nbt;
    }

    @Override
    public NbtObject serialize(NbtObject object) {
        object.putAll(nbt);
        return object;
    }

    @Override
    public void deserialize(BlockStateLike state, NbtObject object) {
        nbt.putAll(object);
    }

    /**
     * 获取该方块实体所持有的NBT表。
     *
     * @return 所持有的NBT表。
     */
    public NbtObject getNbt() {
        return nbt;
    }
}
