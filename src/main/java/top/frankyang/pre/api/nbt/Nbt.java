package top.frankyang.pre.api.nbt;

import net.minecraft.nbt.*;
import top.frankyang.pre.api.misc.Castable;
import top.frankyang.pre.api.misc.Copyable;
import top.frankyang.pre.api.misc.DelegatedCastable;
import top.frankyang.pre.api.misc.PrettyPrintable;
import top.frankyang.pre.loader.exceptions.ImpossibleException;

/**
 * 所有NBT数据的父类。
 *
 * @param <T> 该NBT所包装的原版NBT标签。
 */
public abstract class Nbt<T extends Tag> extends DelegatedCastable<T> implements Castable<T>, PrettyPrintable, Copyable<Nbt<T>>, Cloneable {
    /**
     * This static field is here to overcome compile-time type check. Without {@code (Class<?>)}, The compiler will consider the cast illegal, then refuse to compile. With {@code (Class<?>)}, we can fool the compiler :)
     */
    @SuppressWarnings({"unchecked", "RedundantCast"})
    public static final Class<? extends Nbt<?>> CLASS = (Class<? extends Nbt<?>>) (Class<?>) Nbt.class;

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
        } else if (tag instanceof ByteTag) {
            return NbtNumber.NbtByte.of((ByteTag) tag);
        } else if (tag instanceof ShortTag) {
            return NbtNumber.NbtShort.of((ShortTag) tag);
        } else if (tag instanceof IntTag) {
            return NbtNumber.NbtInt.of((IntTag) tag);
        } else if (tag instanceof LongTag) {
            return NbtNumber.NbtLong.of((LongTag) tag);
        } else if (tag instanceof FloatTag) {
            return NbtNumber.NbtFloat.of((FloatTag) tag);
        } else if (tag instanceof DoubleTag) {
            return NbtNumber.NbtDouble.of((DoubleTag) tag);
        } else if (tag instanceof ByteArrayTag) {
            return NbtArray.NbtByteArray.of((ByteArrayTag) tag);
        } else if (tag instanceof IntArrayTag) {
            return NbtArray.NbtIntArray.of((IntArrayTag) tag);
        } else if (tag instanceof LongArrayTag) {
            return NbtArray.NbtLongArray.of((LongArrayTag) tag);
        } else if (tag instanceof ListTag) {
            return NbtList.of((ListTag) tag);
        } else if (tag instanceof CompoundTag) {
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @SuppressWarnings("unchecked")
    public <U extends Nbt<T>> U copy() {
        U u;
        try {
            u = (U) clone();
        } catch (CloneNotSupportedException e) {
            throw new ImpossibleException(e);
        }
        u.delegate = (T) u.delegate.copy();
        return u;
    }
}
