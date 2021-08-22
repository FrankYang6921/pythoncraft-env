package top.frankyang.pre.api.util;

import top.frankyang.pre.api.nbt.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * NBT反序列化器。
 *
 * @see NbtSerializer
 */
public class NbtDeserializer {
    private final Map<String, Object> refMap = new HashMap<>();

    private NbtDeserializer() {
    }

    /**
     * 反序列化一个表到指定的对象。该对象的所有字段会被强制赋值为反序列化的结果。
     *
     * @param nbt 要反序列化的NBT表。
     * @param dst 要反序列化到的对象。
     * @return dst
     */
    public static Object deserializeTo(NbtObject nbt, Object dst) {
        Classes.assignTo(deserialize(nbt), dst);
        return dst;
    }

    /**
     * 反序列化一个表到对象。
     *
     * @param nbt 要反序列化的NBT表。
     * @return 反序列化到的对象。
     */
    public static Object deserialize(NbtObject nbt) {
        try {
            return new NbtDeserializer().deserialize0(nbt);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private Object deserialize0(NbtObject nbt) throws ReflectiveOperationException {
        String type = ((NbtString) nbt.get("type")).get();
        switch (type) {
            case "null":
                return null;
            case "val":
                return deserializePrimitive(nbt);
            case "valArr":
                return deserializePrimitiveArray(nbt);
            case "refArr":
                return deserializeReferenceArray(nbt);
            case "obj":
                return deserializeObject(nbt);
            case "ref":
                return deserializeReference(nbt);
        }
        return null;
    }

    private Object deserializeReference(NbtObject nbt) {
        return Objects.requireNonNull(refMap.get(((NbtString) nbt.get("uuid")).get()));
    }

    private Object deserializeObject(NbtObject nbt) throws ReflectiveOperationException {
        String cls = ((NbtString) nbt.get("class")).get();
        String uuid = ((NbtString) nbt.get("uuid")).get();
        NbtObject payload = ((NbtObject) nbt.get("payload"));
        Class<?> clazz = Class.forName(cls);
        Object instance = Classes.forceNew(clazz);
        refMap.put(uuid, instance);
        List<Field> fields = Classes.getInstanceFields(clazz);
        for (Field field : fields) {
            String key = field.getName();
            Object value = deserialize0((NbtObject) payload.get(key));
            field.set(instance, value);
        }
        return instance;
    }

    private Object deserializePrimitiveArray(NbtObject nbt) throws ReflectiveOperationException {
        String cls = ((NbtString) nbt.get("class")).get();
        String uuid = ((NbtString) nbt.get("uuid")).get();
        NbtList payload = ((NbtList) nbt.get("payload"));
        Class<?> clazz = Class.forName(cls);
        Object array = Array.newInstance(clazz.getComponentType(), payload.size());
        refMap.put(uuid, array);
        int i = 0;
        if (clazz.getComponentType() == boolean.class) {
            for (Nbt<?> item : payload) {
                Object value = ((NbtPrimitive<?>) item).get();
                Array.set(array, i++, (Byte) value > 0);
            }
        } else if (clazz.getComponentType() == char.class) {
            for (Nbt<?> item : payload) {
                Object value = ((NbtPrimitive<?>) item).get();
                Array.set(array, i++, ((String) value).charAt(0));
            }
        } else {
            for (Nbt<?> item : payload) {
                Object value = ((NbtPrimitive<?>) item).get();
                Array.set(array, i++, value);
            }
        }
        return array;
    }

    private Object deserializeReferenceArray(NbtObject nbt) throws ReflectiveOperationException {
        String cls = ((NbtString) nbt.get("class")).get();
        String uuid = ((NbtString) nbt.get("uuid")).get();
        NbtList payload = ((NbtList) nbt.get("payload"));
        Class<?> clazz = Class.forName(cls);
        Object array = Array.newInstance(clazz.getComponentType(), payload.size());
        refMap.put(uuid, array);
        int i = 0;
        for (Nbt<?> item : payload) {
            Object value = deserialize0((NbtObject) item);
            Array.set(array, i++, value);
        }
        return array;
    }

    private Object deserializePrimitive(NbtObject nbt) throws ReflectiveOperationException {
        String cls = ((NbtString) nbt.get("class")).get();
        String uuid = ((NbtString) nbt.get("uuid")).get();
        Object value = ((NbtPrimitive<?>) nbt.get("payload")).get();
        Class<?> clazz = Class.forName(cls);
        if (value instanceof Byte) {  // Real byte or boolean?
            if (clazz == Byte.class) {
                return value;
            }
            return (Byte) value > 0;
        } else if (value instanceof String) {  // Must be char!!
            return ((String) value).charAt(0);
        }
        return value;
    }
}
