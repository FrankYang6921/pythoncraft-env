package top.frankyang.pre.python.internal;

import java.io.Closeable;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;

public interface Python extends AutoCloseable, Closeable {
    void execfile(String filename);

    default void execfile(Path path) {
        execfile(path.toString());
    }

    void setIn(Reader inStream);

    void setOut(Writer outStream);

    void setErr(Writer outStream);

    void close();  // IOException not allowed
}
