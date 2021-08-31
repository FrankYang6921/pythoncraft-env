package top.frankyang.pre.api.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import top.frankyang.pre.api.misc.collection.CastingList;
import top.frankyang.pre.api.misc.conversion.JsonCastable;

/**
 * NBT集合类。就像{@link java.util.Collection}，它定义了一个可以存储NBT数据的集合对象。该对象可以按照索引取得一个NBT对象，并且通过{@link NbtCollection#getByte(int)}等方法，您可以直接获取任意一个NBT类型的对象而无需强制转型。得利于Python的动态性，对于Python，使用{@link NbtCollection#get(int)}方法通常就足够了。
 *
 * @param <T> 该NBT集合所持有的NBT类型所包装的NBT标签类型。
 * @param <U> 该NBT集合所持有的NBT类型。
 */
public interface NbtCollection<T extends Tag, U extends Nbt<?>> extends CastingList<T, U>, JsonCastable {
    /**
     * 按指定类型在集合中获取一个对象。
     *
     * @param index 索引。
     * @param clazz 要获取的对象的类实例。
     * @param <V>   要获取的对象类。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类的对象。
     */
    default <V> V getAsClass(int index, Class<? extends V> clazz) {
        U u = get(index);
        if (clazz.isInstance(u)) {
            return clazz.cast(u);
        }
        throw new ClassCastException(u + " is not a " + clazz.getName() + " object!");
    }

    /**
     * 在集合中获取一个NBT表。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtObject getObject(int index) {
        return getAsClass(index, NbtObject.class);
    }

    /**
     * 在集合中获取一个NBT字节数组。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtArray.NbtByteArray getBytes(int index) {
        return getAsClass(index, NbtArray.NbtByteArray.class);
    }

    /**
     * 在集合中获取一个NBT整型数组。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtArray.NbtIntArray getInts(int index) {
        return getAsClass(index, NbtArray.NbtIntArray.class);
    }

    /**
     * 在集合中获取一个NBT长整型数组。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtArray.NbtLongArray getLongs(int index) {
        return getAsClass(index, NbtArray.NbtLongArray.class);
    }

    /**
     * 在集合中获取一个NBT字节。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtNumber.NbtByte getByte(int index) {
        return getAsClass(index, NbtNumber.NbtByte.class);
    }

    /**
     * 在集合中获取一个NBT短整型。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtNumber.NbtShort getShort(int index) {
        return getAsClass(index, NbtNumber.NbtShort.class);
    }

    /**
     * 在集合中获取一个NBT整型。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtNumber.NbtInt getInt(int index) {
        return getAsClass(index, NbtNumber.NbtInt.class);
    }

    /**
     * 在集合中获取一个NBT长整型。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtNumber.NbtLong getLong(int index) {
        return getAsClass(index, NbtNumber.NbtLong.class);
    }

    /**
     * 在集合中获取一个NBT单精度浮点型。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtNumber.NbtFloat getFloat(int index) {
        return getAsClass(index, NbtNumber.NbtFloat.class);
    }

    /**
     * 在集合中获取一个NBT双精度浮点型。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtNumber.NbtDouble getDouble(int index) {
        return getAsClass(index, NbtNumber.NbtDouble.class);
    }

    /**
     * 在集合中获取一个NBT列表。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtList getList(int index) {
        return getAsClass(index, NbtList.class);
    }

    /**
     * 在集合中获取一个NBT字符串。
     *
     * @param index 索引。
     * @return 获取的对象。
     * @throws ClassCastException 如果集合中存储的不是指定的类型。
     */
    default NbtString getString(int index) {
        return getAsClass(index, NbtString.class);
    }

    @Override
    default JsonElement toJson() {
        JsonArray array = new JsonArray();
        for (U u : this) {
            array.add(u.toJson());
        }
        return array;
    }
}
