package top.frankyang.pre.python;

public interface PythonProvider {
    Python newPython();

    default void whenResolved(Python python) {
        // Does nothing by default :)
    }

    default void whenRejected(Exception e){
        throw new RuntimeException(e);
    }

    default void whenFinished(Python python) {

    }
}
