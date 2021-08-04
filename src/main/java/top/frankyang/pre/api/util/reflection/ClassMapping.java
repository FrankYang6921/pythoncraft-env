package top.frankyang.pre.api.util.reflection;

import net.fabricmc.mapping.tree.ClassDef;
import net.fabricmc.mapping.tree.FieldDef;
import net.fabricmc.mapping.tree.MethodDef;

import java.util.HashMap;
import java.util.Map;

/**
 * 内部类，用于记录一个运行时类与其映射表的映射关系。
 */
class ClassMapping {
    private final String srcName;
    private final String dstName;

    private final Map<String, String> methodMap = new HashMap<>();
    private final Map<String, String> fieldMap = new HashMap<>();

    public ClassMapping(String srcName, String dstName, ClassDef classDef, String srcNamespace, String dstNamespace) {
        this.srcName = srcName;
        this.dstName = dstName;

        for (MethodDef entry : classDef.getMethods()) {
            srcName = entry.getName(srcNamespace);
            dstName = entry.getName(dstNamespace);
            methodMap.put(srcName, dstName);
        }

        for (FieldDef entry : classDef.getFields()) {
            srcName = entry.getName(srcNamespace);
            dstName = entry.getName(dstNamespace);
            fieldMap.put(srcName, dstName);
        }
    }

    /**
     * @return 该类的运行时名称。
     */
    public String getSrcName() {
        return srcName;
    }

    /**
     * @return 该类的映射表名称。
     */
    public String getDstName() {
        return dstName;
    }

    /**
     * @param methodSrc 运行时方法名。
     * @return 映射表方法名。
     */
    public String methodDst(String methodSrc) {
        return methodMap.get(methodSrc);
    }

    /**
     * @param fieldSrc 运行时字段名。
     * @return 映射表字段名。
     */
    public String fieldDst(String fieldSrc) {
        return fieldMap.get(fieldSrc);
    }
}
