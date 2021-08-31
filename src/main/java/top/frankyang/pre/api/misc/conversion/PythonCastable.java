package top.frankyang.pre.api.misc.conversion;

import org.python.core.PyObject;

/**
 * 表明一个对象可以被转化为Python对象。
 */
@FunctionalInterface
public interface PythonCastable {
    PyObject toPython();
}
