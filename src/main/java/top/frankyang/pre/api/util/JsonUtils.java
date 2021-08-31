package top.frankyang.pre.api.util;

import com.google.gson.*;
import org.python.core.*;
import top.frankyang.pre.api.nbt.*;
import top.frankyang.pre.loader.exceptions.ImpossibleException;

public final class JsonUtils {
    private JsonUtils() {
    }

    public static Nbt<?> jsonToNbt(JsonElement element) {
        if (element == null || element instanceof JsonNull) {
            return null;
        }
        if (element instanceof JsonPrimitive) {
            JsonPrimitive elem = (JsonPrimitive) element;
            if (elem.isString()) {
                return NbtString.of(elem.getAsString());
            }
            if (elem.isNumber()) {
                return NbtNumber.of(elem.getAsNumber());
            }
            if (elem.isBoolean()) {
                return NbtBoolean.of(elem.getAsBoolean());
            }
            throw new ImpossibleException();
        }
        if (element instanceof JsonArray) {
            NbtList target = NbtList.empty();
            ((JsonArray) element).forEach(e ->
                target.add(jsonToNbt(e))
            );
            return target;
        }
        if (element instanceof JsonObject) {
            NbtObject target = NbtObject.empty();
            ((JsonObject) element).entrySet().forEach(e ->
                target.put(e.getKey(), jsonToNbt(e.getValue()))
            );
            return target;
        }
        throw new ImpossibleException(element.toString());
    }

    public static PyObject jsonToPython(JsonElement element) {
        if (element == null || element instanceof JsonNull) {
            return Py.None;
        }
        if (element instanceof JsonPrimitive) {
            JsonPrimitive elem = (JsonPrimitive) element;
            if (elem.isString()) {
                return new PyString(elem.getAsString());
            }
            if (elem.isBoolean()) {
                return new PyBoolean(elem.getAsBoolean());
            }
            if (elem.isNumber()) {
                return new PyFloat(elem.getAsDouble());
            }
            throw new ImpossibleException();
        }
        if (element instanceof JsonArray) {
            PyList target = new PyList();
            ((JsonArray) element).forEach(o ->
                target.append(jsonToPython(o))
            );
            return target;
        }
        if (element instanceof JsonObject) {
            PyDictionary target = new PyDictionary();
            ((JsonObject) element).entrySet().forEach(e ->
                target.__setitem__(
                    new PyString(e.getKey()),
                    jsonToPython(e.getValue())
                )
            );
            return target;
        }
        throw new ImpossibleException();
    }
}
