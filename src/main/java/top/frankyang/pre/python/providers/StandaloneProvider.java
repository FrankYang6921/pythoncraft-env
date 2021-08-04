package top.frankyang.pre.python.providers;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import org.python.core.Py;
import top.frankyang.pre.api.core.Api;
import top.frankyang.pre.python.internal.Python;
import top.frankyang.pre.python.internal.PythonImpl;
import top.frankyang.pre.python.internal.PythonProvider;
import top.frankyang.pre.util.StackTraces;

import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Paths;

public class StandaloneProvider implements PythonProvider {
    private final CommandContext<ServerCommandSource> context;
    private final Writer out = new StringWriter();
    private final String path;

    public StandaloneProvider(CommandContext<ServerCommandSource> context, String path) {
        this.context = context;
        this.path = Paths.get(path).getParent().toString();
    }

    @Override
    public Python newPython() {
        PythonImpl python = new PythonImpl();
        python.setErr(out);
        python.setOut(out);
        python.pushPythonPath(path);  // Adds the parent path of the file to the Python path, so that you can do some imports.
        python.getSystemState().builtins.__setitem__("API", Py.java2py(Api.getSharedInstance()));
        return python;
    }

    private void sendBufferToFeedback() {
        String string = out.toString().trim();
        if (!string.isEmpty()) {
            context.getSource().sendFeedback(new LiteralText(string), true);
        }  // Ensures nothing will be printed if the stdout is empty.
    }

    @Override
    public void whenResolved(Python python) {
        sendBufferToFeedback();
        context.getSource().sendFeedback(new LiteralText("执行了一个Python脚本，没有捕获到异常。"), true);
    }

    @Override
    public void whenRejected(Exception e) {
        sendBufferToFeedback();
        context.getSource().sendFeedback(new LiteralText("执行了一个Python脚本，捕获了如下异常。"), true);
        context.getSource().sendError(
            new LiteralText(StackTraces.translate(e))
        );
    }
}
