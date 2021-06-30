package top.frankyang.pre.python;

import java.io.Closeable;
import java.io.Reader;
import java.io.Writer;

public interface Python extends AutoCloseable, Closeable {
    void execfile(String filename);
    void setIn(Reader inStream);
    void setOut(Writer outStream);
    void setErr(Writer outStream);
}
