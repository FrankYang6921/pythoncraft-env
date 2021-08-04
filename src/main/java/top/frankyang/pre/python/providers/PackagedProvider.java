package top.frankyang.pre.python.providers;

import org.python.core.Py;
import top.frankyang.pre.api.core.Api;
import top.frankyang.pre.loader.core.PackageImpl;
import top.frankyang.pre.python.internal.Python;
import top.frankyang.pre.python.internal.PythonImpl;
import top.frankyang.pre.python.internal.PythonProvider;

public class PackagedProvider implements PythonProvider {
    private final String path;
    private final ClassLoader loader;
    private final PackageImpl pkg;

    public PackagedProvider(String path, ClassLoader loader, PackageImpl pkg) {
        this.path = path;
        this.loader = loader;
        this.pkg = pkg;
    }

    @Override
    public Python newPython() {
        PythonImpl python = new PythonImpl();
        Thread.currentThread().setContextClassLoader(loader);
        python.setClassLoader(loader);
        python.pushPythonPath(path);
        python.set("_PACKAGE", pkg);
        python.getSystemState().builtins.__setitem__("API", Py.java2py(Api.getDedicatedInstance(pkg)));
        return python;
    }
}
