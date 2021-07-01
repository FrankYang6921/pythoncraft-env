package top.frankyang.pre.interact;

import top.frankyang.pre.python.IndividualPythonImpl;
import top.frankyang.pre.python.Python;
import top.frankyang.pre.python.PythonProvider;
import top.frankyang.pre.util.ClassLoaders;

import java.nio.file.Path;
import java.util.List;

public class PackagedProvider implements PythonProvider {
    private final String path;
    private final ClassLoader loader;

    public PackagedProvider(Path path, List<Path> classes) {
        this.path = path.getParent().toString();
        loader = ClassLoaders.get(classes);
    }

    @Override
    public Python newPython() {
        IndividualPythonImpl python = new IndividualPythonImpl();
        python.setClassLoader(loader);
        python.pushPythonPath(path);
        return python;
    }
}
