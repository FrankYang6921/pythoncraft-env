package top.frankyang.pre.api.util;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;
import top.frankyang.pre.api.nbt.*;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

/**
 * NBT序列化器。该类可以将绝大多数对象序列化为NBT表，并且通过{@link NbtDeserializer}来反序列化它。
 *
 * <p>序列化器这样运作：它会将一个对象序列化成一个NBT表，表中有以下四种可能的键：{@code class}，{@code uuid}，{@code type}，
 * {@code payload}。一共有六种可能的{@code type}。{@code null}表示一个空对象；{@code ref}表示一个引用，我们会稍后提及它；{@code obj}
 * 表示一个普通对象；{@code val}表示一个原始数据类型；{@code valArr}表示原始数据类型数组；{@code refArr}表示引用类型数组。对于
 * {@code null}，键{@code class}、{@code payload}、{@code uuid}不存在。对于{@code ref}，键{@code class}、{@code payload}不存在
 * 。{@code payload}存储该对象的实际数据，对于{@code obj}类型，它是一个NBT表，对于{@code refArr}或{@code valArr}，它是一个NBT列表，对于
 * {@code val}，它是一个NBT基础类型。{@code class}存储该对象的类名，对于原始数据类型，我们存储它们的包装类名。{@code uuid}存储该对象的引用
 * UUID。</p>
 *
 * <p>为了能够正确地解析循环引用的结构，我们采用引用UUID。当一个对象被第一次序列化，它会被分配一个UUID。如果序列化后续对象时发现该对象已经序列化过，
 * 我们会添加一个{@code ref}类型的表，以此来指向那个对象。有些对象拒绝{@link Object#hashCode()}，我们无法为它分配UUID，因此该对象会被重复序
 * 列化，如果该对象还包含循环引用，则序列化器会产生{@link StackOverflowError}并崩溃。</p>
 *
 * <p>一些对象不可以被序列化，比如一些持有文件、网络资源的对象（类似于{@link FileInputStream}等）。序列化它们会造成不可预料的结果。</p>
 */
public class NbtSerializer {
    private final BiMap<String, Object> refMap = HashBiMap.create();

    private NbtSerializer() {
    }

    /**
     * 序列化一个对象到指定的表。
     *
     * @param object 要序列化的对象。
     * @param dst    要序列化到的NBT表。
     * @return dst
     */
    public static NbtObject serializeTo(Object object, NbtObject dst) {
        dst.putAll(serialize(object));
        return dst;
    }

    /**
     * 序列化一个对象到表。
     *
     * @param object 要序列化的对象。
     * @return 序列化到的NBT表。
     */
    public static NbtObject serialize(Object object) {
        try {
            return new NbtSerializer().serialize0(object);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private NbtObject serialize0(Object object) throws ReflectiveOperationException {
        if (object == null)
            return wrapNull();
        try {
            if (refMap.inverse().containsKey(object))
                return wrapRef(object);
        } catch (Exception ignored) {
            // Some objects refuse to hash. We ignore them.
        }
        if (ReflectUtils.isPrimitiveWrapper(object.getClass()))
            return serializePrimitive(object);
        if (ReflectUtils.isPrimitiveArray(object.getClass()))
            return serializePrimitiveArray(object);
        if (ReflectUtils.isReferenceArray(object.getClass()))
            return serializeReferenceArray(object);
        return serializeObject(object);
    }

    private NbtObject serializeObject(Object object) throws ReflectiveOperationException {
        List<Field> fields = ReflectUtils.getInstanceFields(object.getClass());
        NbtObject root = NbtObject.empty();
        NbtObject wrapper = wrap(object, root, "obj");
        for (Field field : fields) {
            String key = field.getName();
            Object value = field.get(object);
            root.put(key, serialize0(value));
        }
        return wrapper;
    }

    private NbtObject serializePrimitive(@NotNull Object object) {
        return wrap(object, serializePrimitive0(object), "val");
    }

    private Nbt<?> serializePrimitive0(@NotNull Object object) {
        if (object instanceof Boolean) {
            return NbtBoolean.of((Boolean) object);
        } else if (object instanceof Byte) {
            return NbtNumber.ofByte((Byte) object);
        } else if (object instanceof Character) {
            return NbtString.of(String.valueOf(object));
        } else if (object instanceof Short) {
            return NbtNumber.ofShort((Short) object);
        } else if (object instanceof Integer) {
            return NbtNumber.ofInteger((Integer) object);
        } else if (object instanceof Long) {
            return NbtNumber.ofLong((Long) object);
        } else if (object instanceof Float) {
            return NbtNumber.ofFloat((Float) object);
        } else if (object instanceof Double) {
            return NbtNumber.ofDouble((Double) object);
        } else {
            throw new IllegalArgumentException(String.valueOf(object));
        }
    }

    private NbtObject serializeReferenceArray(@NotNull Object object) throws ReflectiveOperationException {
        NbtList root = NbtList.empty();
        NbtObject wrapper = wrap(object, root, "refArr");
        for (Object o : (Object[]) object) {
            root.add(serialize0(o));
        }
        return wrapper;
    }

    private NbtObject serializePrimitiveArray(@NotNull Object object) {
        NbtList payload = NbtList.empty();
        if (object instanceof boolean[]) {
            for (boolean i : (boolean[]) object) {
                payload.add(NbtBoolean.of(i));
            }
        } else if (object instanceof byte[]) {
            for (byte i : (byte[]) object) {
                payload.add(NbtNumber.ofByte(i));
            }
        } else if (object instanceof char[]) {
            for (char i : (char[]) object) {
                payload.add(NbtString.of(String.valueOf(i)));
            }
        } else if (object instanceof short[]) {
            for (short i : (short[]) object) {
                payload.add(NbtNumber.ofShort(i));
            }
        } else if (object instanceof int[]) {
            for (int i : (int[]) object) {
                payload.add(NbtNumber.ofInteger(i));
            }
        } else if (object instanceof long[]) {
            for (long i : (long[]) object) {
                payload.add(NbtNumber.ofLong(i));
            }
        } else if (object instanceof float[]) {
            for (float i : (float[]) object) {
                payload.add(NbtNumber.ofFloat(i));
            }
        } else if (object instanceof double[]) {
            for (double i : (double[]) object) {
                payload.add(NbtNumber.ofDouble(i));
            }
        } else {
            throw new IllegalArgumentException(String.valueOf(object));
        }
        return wrap(object, payload, "valArr");
    }

    private NbtObject wrap(@NotNull Object object, Nbt<?> ret, String type) {
        NbtObject meta = NbtObject.empty();
        String uuid = UUID.randomUUID().toString();
        meta.put("class", NbtString.of(object.getClass().getName()));
        meta.put("uuid", NbtString.of(uuid));
        meta.put("type", NbtString.of(type));
        meta.put("payload", ret);
        try {
            refMap.put(uuid, object);
        } catch (Exception ignored) {
            // Some objects refuse to hash. We ignore them.
        }
        return meta;
    }

    private NbtObject wrapRef(@NotNull Object object) {
        NbtObject meta = NbtObject.empty();
        String uuid = refMap.inverse().get(object);
        meta.put("uuid", NbtString.of(uuid));
        meta.put("type", NbtString.of("ref"));
        return meta;
    }

    private NbtObject wrapNull() {
        NbtObject meta = NbtObject.empty();
        meta.put("type", NbtString.of("null"));
        return meta;
    }
}
