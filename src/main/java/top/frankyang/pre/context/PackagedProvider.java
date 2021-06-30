package top.frankyang.pre.context;

import top.frankyang.pre.python.Python;
import top.frankyang.pre.python.SimplePythonImpl;
import top.frankyang.pre.python.PythonProvider;

public class PackagedProvider implements PythonProvider {
    @Override
    public Python newPython() {
        return new SimplePythonImpl();
    }
}
