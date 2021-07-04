package top.frankyang.pre.main;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.frankyang.pre.misc.Version;
import top.frankyang.pre.packaging.unpacking.Package;
import top.frankyang.pre.packaging.unpacking.PackageHandler;
import top.frankyang.pre.packaging.unpacking.PackageLoader;
import top.frankyang.pre.python.internal.PythonThreadPool;
import top.frankyang.pre.python.providers.StandaloneProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.LongArgumentType.getLong;
import static com.mojang.brigadier.arguments.LongArgumentType.longArg;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

abstract class BasePythonCraft implements PackageHandler {
    // The default and main apache logger of PythonCraft.
    protected static final Logger logger = LogManager.getLogger(PythonCraft.class);
    // The environment root (.minecraft) of PythonCraft.
    protected static final String envRoot = System.getProperty("user.dir");
    // The version of PythonCraft loader .
    protected static final Version version = new Version("0.0.1");

    // The *ONLY* thread pool used to deploy Python in PythonCraft. Uses instance field to defer loading.
    protected final PythonThreadPool pythonThreadPool = new PythonThreadPool(
        0, 2147483647, 60, TimeUnit.SECONDS
    );
    // The *ONLY* package loader of PythonCraft. Will be created in the abstract method `loadPackages()`.
    protected final PackageLoader loader;
    // Whether command blocks are allowed to run PythonCraft scripts or not. Settable by commands.
    private boolean commandBlocks = true;

    protected BasePythonCraft() {
        registerCommands();
        loader = loadPackages();
    }

    public static Logger getLogger() {
        return logger;
    }

    public static String getEnvRoot() {
        return envRoot;
    }

    public static Version getVersion() {
        return version;
    }

    public List<Package> getPackages() {
        return loader.getPackages();
    }

    protected abstract PackageLoader loadPackages();

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register(this::registerCommands);
    }

    private void registerCommands(
        CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(
            literal("python").requires(s -> s.hasPermissionLevel(commandBlocks ? 2 : 4)).then(
                literal("config").then(
                    literal("maxPythonThreadCount").then(
                        argument("value", integer()).executes(context -> {
                            pythonThreadPool.setMaxPythonCount(getInteger(context, "value"));
                            context.getSource().sendFeedback(new LiteralText("已经改变了最大Python解释器数量。"), true);
                            return 1;
                        })
                    )
                ).then(
                    literal("minPythonThreadCount").then(
                        argument("value", integer()).executes(context -> {
                            pythonThreadPool.setMinPythonCount(getInteger(context, "value"));
                            context.getSource().sendFeedback(new LiteralText("已经改变了最小Python解释器数量。"), true);
                            return 1;
                        })
                    )
                ).then(
                    literal("pythonThreadKeepAliveTime").then(
                        argument("value", longArg()).executes(context -> {
                            pythonThreadPool.setKeepAliveTime(getLong(context, "value"), TimeUnit.SECONDS);
                            context.getSource().sendFeedback(new LiteralText("已经改变了Python解释器超时时间。"), true);
                            return 1;
                        })
                    )
                ).then(
                    literal("commandBlocksAllowed").then(
                        argument("value", bool()).executes(context -> {
                            commandBlocks = getBool(context, "value");
                            context.getSource().sendFeedback(
                                commandBlocks ? new LiteralText("现在允许命令方块执行PythonCraft命令。") : new LiteralText("现在不允许命令方块执行PythonCraft命令。"), true
                            );
                            return 1;
                        })
                    )
                )
            ).then(
                literal("script").then(
                    argument("path", string()).executes(context -> {
                        String path = getString(context, "path");
                        pythonThreadPool.submit(
                            p -> p.execfile(path),
                            new StandaloneProvider(
                                context, path
                            )
                        );
                        return 1;
                    })
                )
            )
        );
    }
}
