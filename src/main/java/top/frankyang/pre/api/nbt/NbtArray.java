package top.frankyang.pre.api.nbt;

import com.google.common.base.Strings;
import net.minecraft.nbt.*;

import java.util.List;

/**
 * NBT数组类。NBT数组可以存储字节型，整型或是长整型的数据。
 *
 * @param <T> 该NBT数组所持有的NBT类型所包装的NBT标签类型。
 * @param <U> 该NBT数组所持有的NBT类型。
 */
public abstract class NbtArray<T extends Tag, U extends Nbt<T>>
    extends Nbt<AbstractListTag<T>>
    implements NbtCollection<T, U> {

    protected NbtArray(AbstractListTag<T> delegate) {
        super(delegate);
    }

    /**
     * 通过一个字节数组构造一个NBT数组。
     *
     * @param bytes 字节数组。
     * @return 构造的NBT字节数组。
     */
    public static NbtByteArray ofBytes(byte... bytes) {
        return new NbtByteArray(new ByteArrayTag(bytes));
    }

    /**
     * 通过一个整型数组构造一个NBT数组。
     *
     * @param ints 整型数组。
     * @return 构造的NBT整型数组。
     */
    public static NbtIntArray ofIntegers(int... ints) {
        return new NbtIntArray(new IntArrayTag(ints));
    }

    /**
     * 通过一个长整型数组构造一个NBT数组。
     *
     * @param longs 长整型数组。
     * @return 构造的NBT长整型数组。
     */
    public static NbtLongArray ofLongs(long... longs) {
        return new NbtLongArray(new LongArrayTag(longs));
    }

    /**
     * 通过一个字节列表构造一个NBT数组。
     *
     * @param bytes 字节数组。
     * @return 构造的NBT字节数组。
     */
    public static NbtByteArray ofBytes(List<Byte> bytes) {
        return new NbtByteArray(new ByteArrayTag(bytes));
    }

    /**
     * 通过一个整型列表构造一个NBT数组。
     *
     * @param ints 整型数组。
     * @return 构造的NBT整型数组。
     */
    public static NbtIntArray ofIntegers(List<Integer> ints) {
        return new NbtIntArray(new IntArrayTag(ints));
    }

    /**
     * 通过一个长整型列表构造一个NBT数组。
     *
     * @param longs 长整型数组。
     * @return 构造的NBT长整型数组。
     */
    public static NbtLongArray ofLongs(List<Long> longs) {
        return new NbtLongArray(new LongArrayTag(longs));
    }

    @Override
    public List<T> getDelegateList() {
        return casted;
    }

    @Override
    public T toSrc(U u) {
        return u.cast();
    }

    protected abstract String getTypePrefix();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        builder.append(getTypePrefix()).append(";");
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
            return "[" + getTypePrefix() + ";]";
        StringBuilder builder = new StringBuilder("[");
        builder.append(getTypePrefix()).append(";\n");
        forEach(i ->
            builder
                .append(Strings.repeat("\t", depth))
                .append(i)
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

    public static class NbtByteArray extends NbtArray<ByteTag, NbtNumber.NbtByte> {
        protected NbtByteArray(AbstractListTag<ByteTag> delegate) {
            super(delegate);
        }

        public static NbtByteArray of(AbstractListTag<ByteTag> delegate) {
            return new NbtByteArray(delegate);
        }

        @Override
        protected String getTypePrefix() {
            return "B";
        }

        @Override
        public NbtNumber.NbtByte toDst(ByteTag byteTag) {
            return NbtNumber.NbtByte.of(byteTag);
        }

        @Override
        public Class<NbtNumber.NbtByte> getDstClass() {
            return NbtNumber.NbtByte.class;
        }
    }

    public static class NbtIntArray extends NbtArray<IntTag, NbtNumber.NbtInt> {
        protected NbtIntArray(AbstractListTag<IntTag> delegate) {
            super(delegate);
        }

        public static NbtIntArray of(AbstractListTag<IntTag> delegate) {
            return new NbtIntArray(delegate);
        }

        @Override
        protected String getTypePrefix() {
            return "I";
        }

        @Override
        public NbtNumber.NbtInt toDst(IntTag intTag) {
            return NbtNumber.NbtInt.of(intTag);
        }

        @Override
        public Class<NbtNumber.NbtInt> getDstClass() {
            return NbtNumber.NbtInt.class;
        }
    }

    public static class NbtLongArray extends NbtArray<LongTag, NbtNumber.NbtLong> {
        protected NbtLongArray(AbstractListTag<LongTag> delegate) {
            super(delegate);
        }

        public static NbtLongArray of(AbstractListTag<LongTag> delegate) {
            return new NbtLongArray(delegate);
        }

        @Override
        protected String getTypePrefix() {
            return "L";
        }

        @Override
        public NbtNumber.NbtLong toDst(LongTag longTag) {
            return NbtNumber.NbtLong.of(longTag);
        }

        @Override
        public Class<NbtNumber.NbtLong> getDstClass() {
            return NbtNumber.NbtLong.class;
        }
    }
}
