package top.frankyang.pre.api.nbt;

import net.minecraft.nbt.*;
import top.frankyang.pre.api.misc.*;
import top.frankyang.pre.api.misc.conversion.Castable;
import top.frankyang.pre.api.misc.conversion.CastableImpl;
import top.frankyang.pre.api.misc.conversion.JsonCastable;
import top.frankyang.pre.loader.exceptions.ImpossibleException;

/**
 * 所有NBT数据的父类。
 *
 * @param <T> 该NBT所包装的原版NBT标签。
 */
public abstract class Nbt<T extends Tag>
    extends CastableImpl<T>
    implements
    Castable<T>,
    PrettyPrintable,
    Copyable<Nbt<T>>,
    JsonCastable {
    /**
     * This static field is here to overcome compile-time type check. Without {@code (Class<?>)}, The compiler will consider the cast illegal, then refuse to compile. With {@code (Class<?>)}, we can fool the compiler :)
     */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    protected static final Class<? extends Nbt<?>> CLASS = (Class<? extends Nbt<?>>) (Class<?>) Nbt.class;

    protected Nbt(T delegate) {
        super(delegate);
    }

    /**
     * 将一个原版NBT标签转为所对应的的NBT。
     *
     * @param tag 原版NBT标签。
     * @return 该标签所对应的NBT。
     */
    public static Nbt<?> of(Tag tag) {
        if (tag == null) return null;
        if (tag instanceof StringTag) {
            return NbtString.of((StringTag) tag);
        }
        if (tag instanceof ByteTag) {
            return NbtNumber.NbtByte.of((ByteTag) tag);
        }
        if (tag instanceof ShortTag) {
            return NbtNumber.NbtShort.of((ShortTag) tag);
        }
        if (tag instanceof IntTag) {
            return NbtNumber.NbtInt.of((IntTag) tag);
        }
        if (tag instanceof LongTag) {
            return NbtNumber.NbtLong.of((LongTag) tag);
        }
        if (tag instanceof FloatTag) {
            return NbtNumber.NbtFloat.of((FloatTag) tag);
        }
        if (tag instanceof DoubleTag) {
            return NbtNumber.NbtDouble.of((DoubleTag) tag);
        }
        if (tag instanceof ByteArrayTag) {
            return NbtArray.NbtByteArray.of((ByteArrayTag) tag);
        }
        if (tag instanceof IntArrayTag) {
            return NbtArray.NbtIntArray.of((IntArrayTag) tag);
        }
        if (tag instanceof LongArrayTag) {
            return NbtArray.NbtLongArray.of((LongArrayTag) tag);
        }
        if (tag instanceof ListTag) {
            return NbtList.of((ListTag) tag);
        }
        if (tag instanceof CompoundTag) {
            return NbtObject.of((CompoundTag) tag);
        }
        throw new ImpossibleException(tag.getClass().getName() + "?!");
    }

    /**
     * 获取该NBT标签的内部类型。
     *
     * @return 内部类型ID。
     */
    public byte getType() {
        return cast().getType();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Nbt<T> shallowCopy() {
        Nbt<T> dst = Copyable.super.shallowCopy();
        dst.casted = (T) dst.casted.copy();
        return dst;
    }

    @Override
    // Disables `deepCopy()` by default;
    public Nbt<T> deepCopy() {
        return shallowCopy();
    }
}
