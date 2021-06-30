package top.frankyang.pre.python;

public interface PythonProvider {
    Python newPython();

    default void onSuccessful(Python python) {
        // Does nothing by default :)
    }

    default void onException(Exception e){
        throw new RuntimeException(e);
    }
}
