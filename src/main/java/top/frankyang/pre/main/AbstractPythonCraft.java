package top.frankyang.pre.main;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.Version;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import top.frankyang.pre.python.internal.PythonThreadPool;
import top.frankyang.pre.python.providers.StandaloneProvider;
import top.frankyang.pre.util.Versions;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.LongArgumentType.getLong;
import static com.mojang.brigadier.arguments.LongArgumentType.longArg;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

abstract class AbstractPythonCraft {
    protected final Version version = Versions.of("0.1.1");
    protected final PythonThreadPool pythonThreadPool = new PythonThreadPool();
    private boolean looseMode = true;

    protected AbstractPythonCraft() {
        registerCommands();
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    private void registerCommands(
        CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(
            literal("python").requires(s -> s.hasPermissionLevel(looseMode ? 2 : 4)).then(
                literal("config")
                    .then(literal("maxPythonThreadCount").then(argument("value", integer()).executes(this::maxPyThreadCountCmd)))
                    .then(literal("minPythonThreadCount").then(argument("value", integer()).executes(this::minPyThreadCountCmd)))
                    .then(literal("pythonThreadTimeout").then(argument("value", longArg()).executes(this::pyThreadTimeoutCmd)))
                    .then(literal("looseMode").then(argument("value", bool()).executes(this::looseModeCmd)))
            ).then(
                literal("script")
                    .then(argument("path", string()).executes(this::runScript))
            )
        );
    }

    private int maxPyThreadCountCmd(CommandContext<ServerCommandSource> context) {
        pythonThreadPool.setMaxPythonCount(getInteger(context, "value"));
        context.getSource().sendFeedback(new LiteralText("已经改变了最大Python解释器数量。"), true);
        return pythonThreadPool.getMaxPythonCount();
    }

    private int minPyThreadCountCmd(CommandContext<ServerCommandSource> context) {
        pythonThreadPool.setMinPythonCount(getInteger(context, "value"));
        context.getSource().sendFeedback(new LiteralText("已经改变了最小Python解释器数量。"), true);
        return pythonThreadPool.getMinPythonCount();
    }

    private int pyThreadTimeoutCmd(CommandContext<ServerCommandSource> context) {
        pythonThreadPool.setKeepAliveTime(getLong(context, "value"), SECONDS);
        context.getSource().sendFeedback(new LiteralText("已经改变了Python解释器超时时间。"), true);
        return 1;
    }

    private int looseModeCmd(CommandContext<ServerCommandSource> context) {
        looseMode = getBool(context, "value");
        context.getSource().sendFeedback(
            looseMode ?
                new LiteralText("现在允许命令方块执行PythonCraft命令。") :
                new LiteralText("现在不允许命令方块执行PythonCraft命令。"),
            true
        );
        return looseMode ? 1 : 0;
    }

    private int runScript(CommandContext<ServerCommandSource> context) {
        String path = getString(context, "path");
        pythonThreadPool.submit(
            p -> p.execfile(path), new StandaloneProvider(context, path)
        );
        return 1;
    }

    public Version getVersion() {
        return this.version;
    }

    public PythonThreadPool getPythonThreadPool() {
        return this.pythonThreadPool;
    }
}
