package top.frankyang.pre.python.providers;

import top.frankyang.pre.python.internal.IndividualPythonImpl;
import top.frankyang.pre.python.internal.Python;
import top.frankyang.pre.python.internal.PythonProvider;
import top.frankyang.pre.util.ClassLoaders;

import java.nio.file.Path;
import java.util.List;

public class PackagedProvider implements PythonProvider {
    private final String path;
    private final ClassLoader loader;

    public PackagedProvider(String path, ClassLoader loader) {
        this.path = path;
        this.loader = loader;
    }

    @Override
    public Python newPython() {
        IndividualPythonImpl python = new IndividualPythonImpl();
        python.setClassLoader(loader);
        python.pushPythonPath(path);
        return python;
    }
}
