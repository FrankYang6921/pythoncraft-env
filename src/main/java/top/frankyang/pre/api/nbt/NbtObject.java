package top.frankyang.pre.api.nbt;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.nbt.Tag;
import top.frankyang.pre.api.misc.CastingMap;
import top.frankyang.pre.mixin.reflect.CompoundTagAccessor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static top.frankyang.pre.api.util.ReflectUtils.tryThrow;

/**
 * NBT表。就像{@link java.util.Map}，它定义了一个可以存储NBT数据的表。该对象可以按照键取得一个NBT对象，并且通过{@link NbtObject#getByte(String)}等方法，您可以直接获取任意一个NBT类型的对象而无需强制转型。得利于Python的动态性，对于Python，使用{@link NbtObject#get(Object)}方法通常就足够了。
 */
public class NbtObject extends Nbt<CompoundTag> implements CastingMap<String, Tag, Nbt<?>> {
    protected NbtObject(CompoundTag delegate) {
        super(delegate);
    }

    /**
     * 创建一个空的NBT表。
     *
     * @return 新的NBT表。
     */
    public static NbtObject empty() {
        return NbtObject.of(new CompoundTag());
    }

    /**
     * 创建一个指定内容的NBT表。
     *
     * @param pairs 指定的键值对。
     * @return 新的NBT表。
     */
    public static NbtObject of(Object... pairs) {
        if ((pairs.length & 1) != 0)
            throw new IllegalArgumentException("Argument `pairs` must be of a length of an even number!");

        List<Pair<String, Nbt<?>>> realPairs = new LinkedList<>();
        for (int i = 0; i < pairs.length - 1; i += 2) {
            if (!(pairs[i] instanceof CharSequence)) {
                throw new IllegalArgumentException(pairs[i] + " is not a string!");
            }
            if (!(pairs[i + 1] instanceof Nbt<?>)) {
                throw new IllegalArgumentException(pairs[i + 1] + " is not a NBT!");
            }
            realPairs.add(new Pair<>(
                ((CharSequence) pairs[i]).toString(), (Nbt<?>) pairs[i + 1]
            ));
        }

        return NbtObject.of(new CompoundTag() {{
            realPairs.forEach(p -> put(p.getFirst(), p.getSecond().cast()));
        }});
    }

    /**
     * 将NBT标签包装成NBT表。
     *
     * @param delegate 指定的标签。
     * @return 包装的NBT表。
     */
    public static NbtObject of(CompoundTag delegate) {
        return new NbtObject(delegate);
    }

    /**
     * 将NBT字符串解析成NBT表。
     *
     * @param string 指定的字符串。
     * @return 解析的NBT表。
     */
    public static NbtObject parse(String string) {
        return tryThrow(() -> NbtObject.of(new StringNbtReader(new StringReader(string)).parseCompoundTag()));
    }

    @Override
    public Map<String, Tag> getDelegateMap() {
        return ((CompoundTagAccessor) delegate).getTags();
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

    public <T> T getAsClass(String key, Class<? extends T> clazz) {
        Nbt<?> u = get(key);
        if (clazz.isInstance(u)) {
            return clazz.cast(u);
        }
        throw new ClassCastException(u + " is not a " + clazz.getName() + " object!");
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getObject(int)
     */
    public NbtObject getObject(String key) {
        return getAsClass(key, NbtObject.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getBytes(int)
     */
    public NbtArray.NbtByteArray getBytes(String key) {
        return getAsClass(key, NbtArray.NbtByteArray.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getInts(int)
     */
    public NbtArray.NbtIntArray getInts(String key) {
        return getAsClass(key, NbtArray.NbtIntArray.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getLongs(int)
     */
    public NbtArray.NbtLongArray getLongs(String key) {
        return getAsClass(key, NbtArray.NbtLongArray.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getByte(int)
     */
    public NbtNumber.NbtByte getByte(String key) {
        return getAsClass(key, NbtNumber.NbtByte.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getShort(int)
     */
    public NbtNumber.NbtShort getShort(String key) {
        return getAsClass(key, NbtNumber.NbtShort.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getInt(int)
     */
    public NbtNumber.NbtInt getInt(String key) {
        return getAsClass(key, NbtNumber.NbtInt.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getLong(int)
     */
    public NbtNumber.NbtLong getLong(String key) {
        return getAsClass(key, NbtNumber.NbtLong.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getFloat(int)
     */
    public NbtNumber.NbtFloat getFloat(String key) {
        return getAsClass(key, NbtNumber.NbtFloat.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getDouble(int)
     */
    public NbtNumber.NbtDouble getDouble(String key) {
        return getAsClass(key, NbtNumber.NbtDouble.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getList(int)
     */
    public NbtList getList(String key) {
        return getAsClass(key, NbtList.class);
    }

    /**
     * 与{@link NbtCollection}的同名方法类似，只不过它使用{@link String}作为键，而不是使用{@code int}作为索引。
     *
     * @see NbtCollection#getString(int)
     */
    public NbtString getString(String key) {
        return getAsClass(key, NbtString.class);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        forEach((k, v) ->
            builder
                .append(k)
                .append(": ")
                .append(v)
                .append(", ")
        );
        if (!isEmpty())
            builder.delete(builder.length() - 2, builder.length());
        return builder.append("}").toString();
    }

    @Override
    public String prettyString(int depth) {
        if (isEmpty())
            return "{}";
        StringBuilder builder = new StringBuilder("{\n");
        forEach((k, v) ->
            builder
                .append(Strings.repeat("\t", depth))
                .append(k)
                .append(": ")
                .append(v.prettyString(depth + 1))
                .append(", ")
                .append("\n")
        );
        builder.delete(builder.length() - 3, builder.length());
        return builder
            .append("\n")
            .append(Strings.repeat("\t", depth - 1))
            .append("}")
            .toString();
    }

    @Override
    public NbtObject deepCopy() {
        NbtObject newNbtObject = NbtObject.empty();
        forEach((k, v) ->
            newNbtObject.put(k, v.deepCopy()));
        return newNbtObject;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        forEach((k, v) -> object.add(k, v.toJson()));
        return object;
    }
}
