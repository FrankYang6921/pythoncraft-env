package top.frankyang.pre.api.nbt;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

/**
 * NBT数字。
 *
 * @param <T> 该NBT数字所持有的NBT类型所包装的NBT标签类型。
 * @param <U> 该NBT数字所持有的NBT类型。
 */
public abstract class NbtNumber<T extends AbstractNumberTag, U extends Number> extends Nbt<T> implements NbtPrimitive<U> {
    protected NbtNumber(T delegate) {
        super(delegate);
    }

    /**
     * 构建一个NBT字节。
     */
    public static NbtByte ofByte(Byte aByte) {
        return NbtByte.of(ByteTag.of(aByte));
    }

    /**
     * 构建一个NBT短整型。
     */
    public static NbtShort ofShort(Short aShort) {
        return NbtShort.of(ShortTag.of(aShort));
    }

    /**
     * 构建一个NBT整型。
     */
    public static NbtInt ofInteger(Integer aInteger) {
        return NbtInt.of(IntTag.of(aInteger));
    }

    /**
     * 构建一个NBT长整型。
     */
    public static NbtLong ofLong(Long aLong) {
        return NbtLong.of(LongTag.of(aLong));
    }

    /**
     * 构建一个NBT单精度浮点型。
     */
    public static NbtFloat ofFloat(Float aFloat) {
        return NbtFloat.of(FloatTag.of(aFloat));
    }

    /**
     * 构建一个NBT双精度浮点型。
     */
    public static NbtDouble ofDouble(Double aDouble) {
        return NbtDouble.of(DoubleTag.of(aDouble));
    }

    public static NbtNumber<?, ?> of(Number number) {
        if (number instanceof Byte) {
            return ofByte(number.byteValue());
        }
        if (number instanceof Short) {
            return ofShort(number.shortValue());
        }
        if (number instanceof Integer) {
            return ofInteger(number.intValue());
        }
        if (number instanceof Long) {
            return ofLong(number.longValue());
        }
        if (number instanceof Float) {
            return ofFloat(number.floatValue());
        }
        return ofDouble(number.doubleValue());
    }

    protected abstract String getSuffix();

    @Override
    public String toString() {
        return get() + getSuffix();
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    public static class NbtByte extends NbtNumber<ByteTag, Byte> {
        private NbtByte(ByteTag delegate) {
            super(delegate);
        }

        public static NbtByte of(ByteTag delegate) {
            return new NbtByte(delegate);
        }

        @Override
        protected String getSuffix() {
            return "b";
        }

        @Override
        public Byte get() {
            return delegate.getByte();
        }

        @Override
        public void set(Byte aByte) {
            delegate = ByteTag.of(aByte);
        }
    }

    public static class NbtShort extends NbtNumber<ShortTag, Short> {
        private NbtShort(ShortTag delegate) {
            super(delegate);
        }

        public static NbtShort of(ShortTag delegate) {
            return new NbtShort(delegate);
        }

        @Override
        protected String getSuffix() {
            return "s";
        }

        @Override
        public Short get() {
            return delegate.getShort();
        }

        @Override
        public void set(Short aShort) {
            delegate = ShortTag.of(aShort);
        }
    }

    public static class NbtInt extends NbtNumber<IntTag, Integer> {
        private NbtInt(IntTag delegate) {
            super(delegate);
        }

        public static NbtInt of(IntTag delegate) {
            return new NbtInt(delegate);
        }

        @Override
        protected String getSuffix() {
            return "";
        }

        @Override
        public Integer get() {
            return delegate.getInt();
        }

        @Override
        public void set(Integer aInt) {
            delegate = IntTag.of(aInt);
        }
    }

    public static class NbtLong extends NbtNumber<LongTag, Long> {
        private NbtLong(LongTag delegate) {
            super(delegate);
        }

        public static NbtLong of(LongTag delegate) {
            return new NbtLong(delegate);
        }

        @Override
        protected String getSuffix() {
            return "l";
        }

        @Override
        public Long get() {
            return delegate.getLong();
        }

        @Override
        public void set(Long aLong) {
            delegate = LongTag.of(aLong);
        }
    }

    public static class NbtFloat extends NbtNumber<FloatTag, Float> {
        private NbtFloat(FloatTag delegate) {
            super(delegate);
        }

        public static NbtFloat of(FloatTag delegate) {
            return new NbtFloat(delegate);
        }

        @Override
        protected String getSuffix() {
            return "f";
        }

        @Override
        public Float get() {
            return delegate.getFloat();
        }

        @Override
        public void set(Float aFloat) {
            delegate = FloatTag.of(aFloat);
        }
    }

    public static class NbtDouble extends NbtNumber<DoubleTag, Double> {
        private NbtDouble(DoubleTag delegate) {
            super(delegate);
        }

        public static NbtDouble of(DoubleTag delegate) {
            return new NbtDouble(delegate);
        }

        @Override
        protected String getSuffix() {
            return "d";
        }

        @Override
        public Double get() {
            return delegate.getDouble();
        }

        @Override
        public void set(Double aDouble) {
            delegate = DoubleTag.of(aDouble);
        }
    }
}
