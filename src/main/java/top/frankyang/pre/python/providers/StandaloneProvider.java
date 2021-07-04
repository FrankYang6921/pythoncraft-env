package top.frankyang.pre.python.providers;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import top.frankyang.pre.python.internal.IndividualPythonImpl;
import top.frankyang.pre.python.internal.Python;
import top.frankyang.pre.python.internal.PythonProvider;

import java.io.PrintWriter;
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
        IndividualPythonImpl python = new IndividualPythonImpl();
        python.setErr(out);
        python.setOut(out);
        python.pushPythonPath(path);  // Adds the parent path of the file to the Python path, so that you can do some imports.
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
        context.getSource().sendFeedback(new LiteralText("运行了PythonCraft脚本，并且没有捕获到任何异常。"), true);
    }

    @Override
    public void whenRejected(Exception e) {
        sendBufferToFeedback();
        context.getSource().sendFeedback(new LiteralText("运行了PythonCraft脚本，但是捕获到了如下异常。"), true);
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        context.getSource().sendFeedback(new LiteralText(writer.toString()), true);
    }
}
