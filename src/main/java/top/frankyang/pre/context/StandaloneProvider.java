package top.frankyang.pre.context;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import org.python.core.PySystemState;
import top.frankyang.pre.python.Python;
import top.frankyang.pre.python.PythonProvider;
import top.frankyang.pre.python.SimplePythonImpl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Paths;

import static org.python.core.Py.newStringOrUnicode;

public class StandaloneProvider implements PythonProvider {
    private final CommandContext<ServerCommandSource> context;
    private final Writer out = new StringWriter();;
    private final String path;

    public StandaloneProvider(CommandContext<ServerCommandSource> context, String path) {
        this.context = context;
        this.path = Paths.get(path).getParent().toString();
    }

    @Override
    public Python newPython() {
        SimplePythonImpl python = new SimplePythonImpl();
        python.setErr(out);
        python.setOut(out);
        python.getSystemState().path.append(newStringOrUnicode(path));  // Adds the parent path of the file to the Python path, so that you can do some imports.
        return python;
    }

    private void sendBuffer() {
        new PySystemState();
        String string = out.toString().trim();
        if (!string.isEmpty()) {
            context.getSource().sendFeedback(new LiteralText(string), true);
        }  // Ensures nothing will be printed if the stdout is empty.
    }

    @Override
    public void onSuccessful(Python python) {
        sendBuffer();
        context.getSource().sendFeedback(new LiteralText("运行了PythonCraft脚本，并且没有捕获到任何异常。"), true);
    }

    @Override
    public void onException(Exception e) {
        sendBuffer();
        context.getSource().sendFeedback(new LiteralText("运行了PythonCraft脚本，但是捕获到了如下异常。"), true);
        // Prints the thrown exception.
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        context.getSource().sendFeedback(new LiteralText(writer.toString()), true);
    }
}
