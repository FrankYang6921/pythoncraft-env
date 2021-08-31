package top.frankyang.pre.api.misc.conversion;

import com.google.gson.*;
import org.python.core.*;
import top.frankyang.pre.api.util.JsonUtils;

/**
 * 表明一个对象可以被转化为JSON格式。
 */
@FunctionalInterface
public interface JsonCastable extends PythonCastable {

    JsonElement toJson();

    default String toJsonString() {
        return toJson().toString();
    }

    @Override
    default PyObject toPython() {
        return JsonUtils.jsonToPython(toJson());
    }
}
