package top.frankyang.pre.python.internal;

@FunctionalInterface
public interface PythonProvider {
    Python newPython();

    default void whenResolved(Python python) {  // Try
    }

    default void whenRejected(Exception e) {  // Catch
        throw new RuntimeException(e);
    }

    default void whenFinished(Python python) {  // Finally
    }
}
