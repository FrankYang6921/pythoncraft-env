package top.frankyang.pre.api.block.entity;

import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.nbt.NbtObject;
import top.frankyang.pre.api.util.NbtDeserializer;
import top.frankyang.pre.api.util.NbtSerializer;

/**
 * 持有一个对象的方块实体。您可以像操作任意一个对象那样操作这对象所持有的对象。它所持有的对象会被{@link NbtSerializer}和{@link NbtDeserializer}处理，以转化为NBT。注意，当您修改了对象，您仍然需要调用{@link BlockEntityLike#acquirePersistence()}以确保您的修改被保存。
 *
 * @param <T> 所持有的对象类型。
 */
class ObjectBlockEntityImpl<T> extends BlockEntityImpl {
    private final T data;

    protected ObjectBlockEntityImpl(ObjectBlockEntityFactory<?> factory, T data) {
        super(factory);
        this.data = data;
    }

    @Override
    public NbtObject serialize(NbtObject object) {
        NbtSerializer.serializeTo(data, object);
        return object;
    }

    @Override
    public void deserialize(BlockStateLike state, NbtObject object) {
        NbtDeserializer.deserializeTo(object, data);
    }

    /**
     * 获取该方块实体所持有的对象。
     *
     * @return 所持有的对象。
     */
    public T getData() {
        return data;
    }
}
