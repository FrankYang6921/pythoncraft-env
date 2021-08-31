package top.frankyang.pre.python.internal;

import org.python.core.PyObject;

import java.io.Closeable;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public interface Python extends AutoCloseable, Closeable {
    Python SHARED_INSTANCE = new PythonImpl();

    PyObject eval(String code);

    void exec(String code);

    void execfile(String filename);

    default void execfile(Path path) {
        execfile(path.toString());
    }

    void setIn(Reader inStream);

    void setOut(Writer outStream);

    void setErr(Writer outStream);

    void close();  // IOException not allowed
}
