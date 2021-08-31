package top.frankyang.pre.api.nbt;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * NBT字符串。注意，一个NBT字符串的长度限制是{@code 65535}字节（一个中文字符通常占3字节）。如果要存储极长的字符数据，考虑使用{@link NbtArray}。
 */
public class NbtString extends Nbt<StringTag> implements NbtPrimitive<String> {
    private NbtString(StringTag delegate) {
        super(delegate);
    }

    /**
     * 将指定实例包装成此类的实例。
     *
     * @param delegate 要包装的StringTag实例。
     * @return 包装后的NbtString实例。
     */
    public static NbtString of(StringTag delegate) {
        return new NbtString(delegate);
    }

    /**
     * 创建一个Nbt字符串。
     *
     * @param string 字符串。
     * @return 持有该字符串的NbtString实例。
     */
    public static NbtString of(String string) {
        return NbtString.of(StringTag.of(string));
    }

    @Override
    public String get() {
        return casted.asString();
    }

    @Override
    public void set(String string) {
        casted = StringTag.of(string);
    }

    @Override
    public String toString() {
        return "\"" + StringEscapeUtils.escapeJava(get()) + "\"";
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }
}
