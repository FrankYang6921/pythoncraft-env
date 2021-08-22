package top.frankyang.pre.api.block.entity;

import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.nbt.NbtObject;

/**
 * 简单的方块实体。它持有一个NBT表，您可以自由使用这个NBT表。
 */
class SimpleBlockEntityImpl extends BlockEntityImpl {
    private final NbtObject data;

    protected SimpleBlockEntityImpl(SimpleBlockEntityFactory factory, NbtObject data) {
        super(factory);
        this.data = data;
    }

    @Override
    public NbtObject serialize(NbtObject object) {
        object.putAll(data);
        return object;
    }

    @Override
    public void deserialize(BlockStateLike state, NbtObject object) {
        data.putAll(object);
    }

    /**
     * 获取该方块实体所持有的NBT表。
     *
     * @return 所持有的NBT表。
     */
    public NbtObject getData() {
        return data;
    }
}
