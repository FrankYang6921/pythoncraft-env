package top.frankyang.pre.python.internal;

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import java.util.concurrent.atomic.AtomicInteger;

import static org.python.core.Py.newStringOrUnicode;

public class PythonImpl extends PythonInterpreter implements Python {
    private final AtomicInteger pythonPathCount = new AtomicInteger();

    public PythonImpl() {
        super(null, new PySystemState());
    }

    /**
     * Changes the class loader of this python interpreter to the specified one.
     *
     * @param classLoader the specified class loader
     */
    public void setClassLoader(ClassLoader classLoader) {
        getSystemState().setClassLoader(classLoader);
    }

    /**
     * Adds an additional Python path to this python interpreter.
     *
     * @param path the additional Python path
     */
    public void pushPythonPath(String path) {
        pythonPathCount.getAndIncrement();
        getSystemState().path.append(newStringOrUnicode(path));
    }

    /**
     * Removes an additional Python path from this python interpreter.
     */
    public void popPythonPath() {
        if (pythonPathCount.get() <= 0)
            return;

        pythonPathCount.getAndDecrement();
        getSystemState().path.pop();
    }

    /**
     * Removes all additional Python paths from this python interpreter.
     */
    public void popPythonPaths() {
        if (pythonPathCount.get() <= 0)
            return;

        int pathCount = pythonPathCount.get();
        for (int i = 0; i < pathCount; i++) {
            popPythonPath();
        }
    }

    @Override
    public void close() {
    }
}
