package top.frankyang.pre.api.block.entity;

import top.frankyang.pre.api.nbt.Nbt;
import top.frankyang.pre.api.nbt.NbtObject;

/**
 * 简单方块实体的工厂。
 */
public class SimpleBlockEntityFactory extends BlockEntityFactory {
    private final NbtObject defaultData = NbtObject.empty();

    /**
     * 为该工厂生成的简单方块实体增加一个属性。
     *
     * @param key   该属性的键。
     * @param value 该属性的默认值。
     * @return 该实例本身。
     */
    public SimpleBlockEntityFactory property(String key, Nbt<?> value) {
        if (defaultData.containsKey(key)) {
            throw new IllegalStateException("Property already defined: " + key);
        }
        defaultData.put(key, value);
        return this;
    }

    @Override
    public BlockEntityLike newBlockEntity(BlockEntityFactory factory) {
        return new SimpleBlockEntityImpl(this, defaultData.deepCopy());
    }

}
