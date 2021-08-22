package top.frankyang.pre.api.reflection.mapping;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.TinyMappingFactory;
import net.fabricmc.mapping.tree.TinyTree;
import top.frankyang.pre.api.Minecraft;
import top.frankyang.pre.loader.exceptions.RuntimeIOException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 符号解析器。用于正、反向解析运行时和Yarn映射表中的类名，方法名，字段名。
 */
public class SymbolResolver {
    static final String RUNTIME_NAMESPACE =
        Minecraft.getFabricLoader().getMappingResolver().getCurrentRuntimeNamespace();
    static final String MAPPING_NAMESPACE = "named";

    private final Map<String, ClassMapping> runtimeToMapping = new HashMap<>();
    private final Map<String, ClassMapping> mappingToRuntime = new HashMap<>();
    private final Map<Pair<Class<?>, String>, Pair<Class<?>, String>> runtimeFields = new ConcurrentHashMap<>();
    private final Map<Pair<Class<?>, String>, Pair<Class<?>, String>> runtimeMethods = new ConcurrentHashMap<>();

    private SymbolResolver() throws IOException {
        TinyTree tree = TinyMappingFactory.loadWithDetection(new BufferedReader(new InputStreamReader(
            Objects.requireNonNull(SymbolResolver.class.getResourceAsStream("/assets/pre/mappings.tiny")))
        ));

        for (ClassDef classEntry : tree.getClasses()) {
            String runtimeClassName = classEntry.getName(RUNTIME_NAMESPACE);
            String mappingClassName = classEntry.getName(MAPPING_NAMESPACE);

            runtimeToMapping.put(runtimeClassName, new ClassMapping(
                runtimeClassName, mappingClassName, classEntry, RUNTIME_NAMESPACE, MAPPING_NAMESPACE
            ));
            mappingToRuntime.put(mappingClassName, new ClassMapping(
                mappingClassName, runtimeClassName, classEntry, MAPPING_NAMESPACE, RUNTIME_NAMESPACE
            ));
        }
    }

    private static String dotsToSlashes(String s) {
        return s.replace('.', '/');
    }

    private static String slashesToDots(String s) {
        return s.replace('/', '.');
    }

    private static <T> T nonNullOrElse(T t1, T t2) {
        return t1 != null ? t1 : t2;
    }

    public static SymbolResolver getInstance() {
        return NameResolverSingleton.INSTANCE;
    }

    /**
     * @param mappingName 该类的映射表名称。
     * @return 该类的运行时名称。
     */
    public String classNameToRuntime(String mappingName) {
        mappingName = dotsToSlashes(mappingName);
        if (!mappingToRuntime.containsKey(mappingName)) {
            return slashesToDots(mappingName);
        }
        return slashesToDots(mappingToRuntime.get(mappingName).getDstName());
    }

    /**
     * @param runtimeName 该类的运行时名称。
     * @return 该类的映射表名称。
     */
    public String classNameToMapping(String runtimeName) {
        runtimeName = dotsToSlashes(runtimeName);
        if (!runtimeToMapping.containsKey(runtimeName)) {
            return slashesToDots(runtimeName);
        }
        return slashesToDots(runtimeToMapping.get(runtimeName).getDstName());
    }

    /**
     * @param clazz  该方法所属的类的映射表名称。
     * @param method 该方法的映射表名称。
     * @return 该方法的运行时名称。
     * @apiNote 即便该方法有被重写和实现，也要提供最初声明该方法的类或接口。
     */
    public String methodNameToRuntime(String clazz, String method) {
        clazz = dotsToSlashes(clazz);
        if (!mappingToRuntime.containsKey(clazz)) {
            return method;
        }
        return nonNullOrElse(mappingToRuntime.get(clazz).methodDst(method), method);
    }

    /**
     * @param clazz  该方法所属的类的运行时名称。
     * @param method 该方法的运行时名称。
     * @return 该方法的映射表名称。
     * @apiNote 即便该方法有被重写和实现，也要提供最初声明该方法的类或接口。
     */
    public String methodNameToMapping(String clazz, String method) {
        clazz = dotsToSlashes(clazz);
        if (!runtimeToMapping.containsKey(clazz)) {
            return method;
        }
        return nonNullOrElse(runtimeToMapping.get(clazz).methodDst(method), method);
    }

    /**
     * @param clazz 该字段所属的类的映射表名称。
     * @param field 该字段的映射表名称。
     * @return 该字段的运行时名称。
     * @apiNote 即便该字段有被重写，也要提供最初声明该字段的类。
     */
    public String fieldNameToRuntime(String clazz, String field) {
        clazz = dotsToSlashes(clazz);
        if (!mappingToRuntime.containsKey(clazz)) {
            return field;
        }
        return nonNullOrElse(mappingToRuntime.get(clazz).fieldDst(field), field);
    }

    /**
     * @param clazz 该字段所属的类的运行时名称。
     * @param field 该字段的运行时名称。
     * @return 该字段的映射表名称。
     * @apiNote 即便该字段有被重写，也要提供最初声明该字段的类。
     */
    public String fieldNameToMapping(String clazz, String field) {
        clazz = dotsToSlashes(clazz);
        if (!runtimeToMapping.containsKey(clazz)) {
            return field;
        }
        return nonNullOrElse(runtimeToMapping.get(clazz).fieldDst(field), field);
    }

    /**
     * @param runtimeMethod 运行时获取到的方法对象。
     * @return 该方法的映射表名称。
     * @apiNote 无需从最初声明它的类获取该方法。我们会自动进行递归查找。该方法的是部分缓存的，因此性能较好。
     */
    public String mappingNameOf(Method runtimeMethod) {
        String clazz = DeclarationFinder.getOriginalMethodDeclaration(
            runtimeMethod.getDeclaringClass(), runtimeMethod.getName(), runtimeMethod.getParameterTypes()
        ).getName();
        return methodNameToMapping(clazz, runtimeMethod.getName());
    }

    /**
     * @param runtimeField 运行时获取到的字段对象。
     * @return 该字段的映射表名称。
     * @apiNote 无需从最初声明它的类获取该字段。我们会自动进行递归查找。该方法的是部分缓存的，因此性能较好。
     */
    public String mappingNameOf(Field runtimeField) {
        String clazz = DeclarationFinder.getOriginalFieldDeclaration(
            runtimeField.getDeclaringClass(), runtimeField.getName(), new Class[]{runtimeField.getType()}
        ).getName();
        return methodNameToMapping(clazz, runtimeField.getName());
    }

    private Field findField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (ReflectiveOperationException e) {
            // Search in parents
            List<Field> results = new ArrayList<>();
            for (Class<?> cls : clazz.getInterfaces()) {
                results.add(findField(cls, name));
            }
            Class<?> cls = clazz.getSuperclass();
            if (cls != null)
                results.add(findField(cls, name));
            return results.stream().filter(Objects::nonNull).findFirst().orElse(null);
        }
    }

    /**
     * @param clazz 指定的运行时类。
     * @param name  要查找的字段映射表名。
     * @return 查找到的运行时字段。
     * @apiNote 无需从最初声明它的类获取该字段。我们会自动进行递归查找。该方法的结果是缓存的，因此性能较好。该方法返回的字段一定是可访问的。
     */
    public Field runtimeField(Class<?> clazz, String name) {
        try {
            Field field = runtimeField0(clazz, name);
            if (field != null) field.setAccessible(true);
            return field;
        } catch (Throwable throwable) {
            return null;
        }
    }

    private Field runtimeField0(Class<?> clazz, String name) throws Throwable {
        // Development mode
        if (RUNTIME_NAMESPACE.equals("named"))
            return findField(clazz, name);

        Pair<Class<?>, String> pair = new Pair<>(clazz, name);
        if (runtimeFields.containsKey(pair)) {
            pair = runtimeFields.get(pair);
            return pair.getFirst().getDeclaredField(pair.getSecond());
        }

        String className = dotsToSlashes(classNameToMapping(clazz.getName()));
        // Not mapped at all!
        if (!mappingToRuntime.containsKey(className))
            return null;
        // Is mapped in the current class
        String dst = mappingToRuntime.get(className).fieldDst(name);
        if (dst != null) {
            runtimeFields.put(pair, new Pair<>(clazz, dst));
            return clazz.getDeclaredField(dst);
        }

        // Search in parents
        List<Field> results = new ArrayList<>();
        for (Class<?> cls : clazz.getInterfaces()) {
            results.add(runtimeField0(cls, name));
        }
        Class<?> cls = clazz.getSuperclass();
        if (cls != null)
            results.add(runtimeField0(cls, name));

        return results.stream().filter(Objects::nonNull).findFirst().orElse(null);
    }

    /**
     * @param clazz      指定的运行时类。
     * @param name       要查找的方法映射表名。
     * @param paramTypes 方法的形参类型。
     * @return 查找到的运行时方法。
     * @apiNote 无需从最初声明它的类获取该方法。我们会自动进行递归查找。该方法的结果是缓存的，因此性能较好。该方法返回的方法一定是可访问的。
     */
    public Method runtimeMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        try {
            Method method = runtimeMethod0(clazz, name, paramTypes);
            if (method != null) method.setAccessible(true);
            return method;
        } catch (Throwable throwable) {
            return null;
        }
    }

    private Method runtimeMethod0(Class<?> clazz, String name, Class<?>... paramTypes) throws Throwable {
        // Development mode
        if (RUNTIME_NAMESPACE.equals("named"))
            return clazz.getMethod(name, paramTypes);

        Pair<Class<?>, String> pair = new Pair<>(clazz, name);
        if (runtimeMethods.containsKey(pair)) {
            pair = runtimeMethods.get(pair);
            return pair.getFirst().getMethod(pair.getSecond(), paramTypes);
        }

        String className = dotsToSlashes(classNameToMapping(clazz.getName()));
        // Not mapped at all!
        if (!mappingToRuntime.containsKey(className))
            return null;
        // Is mapped in the current class
        String dst = mappingToRuntime.get(className).methodDst(name);
        if (dst != null) {
            runtimeMethods.put(pair, new Pair<>(clazz, dst));
            return clazz.getMethod(dst, paramTypes);
        }

        // Search in parents
        List<Method> results = new ArrayList<>();
        for (Class<?> cls : clazz.getInterfaces()) {
            results.add(runtimeMethod0(cls, name, paramTypes));
        }
        Class<?> cls = clazz.getSuperclass();
        if (cls != null)
            results.add(runtimeMethod0(cls, name, paramTypes));

        return results.stream().filter(Objects::nonNull).findFirst().orElse(null);
    }

    private static class NameResolverSingleton {
        private static final SymbolResolver INSTANCE;

        static {
            try {
                INSTANCE = new SymbolResolver();
            } catch (IOException e) {
                throw new RuntimeIOException(e);
            }
        }
    }
}
