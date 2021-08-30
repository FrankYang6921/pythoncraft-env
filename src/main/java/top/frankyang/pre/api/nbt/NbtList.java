package top.frankyang.pre.api.nbt;

import com.google.common.base.Strings;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.List;

/**
 * 使用{@link ListTag}实现的{@link NbtCollection}。不同于{@link NbtArray}，它存储的数据并没有类型限制。
 */
public class NbtList extends Nbt<ListTag> implements NbtCollection<Tag, Nbt<?>> {
    protected NbtList(ListTag delegate) {
        super(delegate);
    }

    /**
     * 创建一个空的NBT列表。
     *
     * @return 新的NBT列表。
     */
    public static NbtList empty() {
        return NbtList.of(new ListTag());
    }

    /**
     * 创建一个指定内容的NBT列表。
     *
     * @param items 指定的内容。
     * @return 新的NBT列表。
     */
    public static NbtList of(Nbt<?>... items) {
        return NbtList.of(new ListTag() {{
            for (Nbt<?> item : items) add(item.cast());
        }});
    }

    /**
     * 将NBT标签包装成NBT列表。
     *
     * @param delegate 指定的标签。
     * @return 包装的NBT列表。
     */
    public static NbtList of(ListTag delegate) {
        return new NbtList(delegate);
    }

    @Override
    public List<Tag> getDelegateList() {
        return delegate;
    }

    @Override
    public Nbt<?> toDst(Tag tag) {
        return Nbt.of(tag);
    }

    @Override
    public Tag toSrc(Nbt<?> nbt) {
        return nbt.cast();
    }

    @Override
    public Class<? extends Nbt<?>> getDstClass() {
        return Nbt.CLASS;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        forEach(i ->
            builder
                .append(i)
                .append(", ")
        );
        if (!isEmpty())
            builder.delete(builder.length() - 2, builder.length());
        return builder.append("]").toString();
    }

    @Override
    public String prettyString(int depth) {
        if (isEmpty())
            return "[]";
        StringBuilder builder = new StringBuilder("[\n");
        forEach(i ->
            builder
                .append(Strings.repeat("\t", depth))
                .append(i.prettyString(depth + 1))
                .append(", ")
                .append("\n")
        );
        builder.delete(builder.length() - 3, builder.length());
        return builder
            .append("\n")
            .append(Strings.repeat("\t", depth - 1))
            .append("]")
            .toString();
    }

    @Override
    public NbtList deepCopy() {
        NbtList newNbtList = NbtList.empty();
        forEach(i ->
            newNbtList.add(i.deepCopy()));
        return newNbtList;
    }
}
