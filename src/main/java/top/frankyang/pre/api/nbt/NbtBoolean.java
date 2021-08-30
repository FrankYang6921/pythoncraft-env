package top.frankyang.pre.api.nbt;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.ByteTag;

/**
 * NBT布尔类。NBT数组可以存储布尔型的数据。注意，NBT本身并不能储存布尔值，实际上，这个标签将{@code true}存储为{@code 1b}，将{@code false}存储为{@code 0b}，某种程度上类似于{@link NbtNumber.NbtByte}。正因为如此，我们不推荐你将它存储于方块实体等要被序列化的容器中。
 */
public class NbtBoolean extends Nbt<ByteTag> implements NbtPrimitive<Boolean> {
    private NbtBoolean(ByteTag delegate) {
        super(delegate);
    }

    /**
     * 将指定实例包装成此类的实例。
     *
     * @param delegate 要包装的ByteTag实例。
     * @return 包装后的NbtBoolean实例。
     */
    public static NbtBoolean of(ByteTag delegate) {
        return new NbtBoolean(delegate);
    }

    /**
     * 创建一个Nbt布尔值。
     *
     * @param aBoolean 布尔值。
     * @return 持有该布尔值的NbtBoolean实例。
     */
    public static NbtBoolean of(Boolean aBoolean) {
        return NbtBoolean.of(ByteTag.of((byte) (aBoolean ? 1 : 0)));
    }

    @Override
    public Boolean get() {
        return delegate.getByte() > 0;
    }

    @Override
    public void set(Boolean aBoolean) {
        delegate = ByteTag.of((byte) (aBoolean ? 1 : 0));
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }
}
