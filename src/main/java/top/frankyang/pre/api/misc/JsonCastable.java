package top.frankyang.pre.api.misc;

import com.google.gson.JsonElement;

/**
 * 表明一个对象可以被转化为JSON格式。
 */
public interface JsonCastable {
    JsonElement toJson();

    default String toJsonString() {
        return toJson().toString();
    }
}
