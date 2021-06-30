package top.frankyang.pre;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.frankyang.pre.context.StandaloneProvider;
import top.frankyang.pre.loader.Package;
import top.frankyang.pre.loader.Packages;
import top.frankyang.pre.misc.FileOnlyVisitor;
import top.frankyang.pre.python.PythonThreadPool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
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

public class PythonCraft implements ModInitializer {
    private static final String HOME_DIR = System.getProperty("user.dir");
    private static final PythonThreadPool PYTHON_THREAD_POOL = new PythonThreadPool(
        0, 2147483647, 60, TimeUnit.SECONDS
    );
    private static final ArrayList<Package> LOADED_MOD_PACKAGES = new ArrayList<>();
    private static final Logger LOGGER =
        LogManager.getLogger(PythonCraft.class);  // The logger isn't necessary here, but comes handy for debugging.

    private static boolean commandBlocksAllowed = true;

    public static PythonThreadPool getPythonThreadPool() {
        return PYTHON_THREAD_POOL;
    }

    public static ArrayList<Package> getLoadedModPackages() {
        return LOADED_MOD_PACKAGES;
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(
            literal("python").requires(s -> s.hasPermissionLevel(commandBlocksAllowed ? 2 : 4)).then(
                literal("config").then(
                    literal("maxPythonThreadCount").then(
                        argument("value", integer()).executes(context -> {
                            PYTHON_THREAD_POOL.setMaxPythonCount(getInteger(context, "value"));
                            context.getSource().sendFeedback(new LiteralText("已经改变了最大Python解释器数量。"), true);
                            return 1;
                        })
                    )
                ).then(
                    literal("minPythonThreadCount").then(
                        argument("value", integer()).executes(context -> {
                            PYTHON_THREAD_POOL.setMinPythonCount(getInteger(context, "value"));
                            context.getSource().sendFeedback(new LiteralText("已经改变了最小Python解释器数量。"), true);
                            return 1;
                        })
                    )
                ).then(
                    literal("pythonThreadKeepAliveTime").then(
                        argument("value", longArg()).executes(context -> {
                            PYTHON_THREAD_POOL.setKeepAliveTime(getLong(context, "value"), TimeUnit.SECONDS);
                            context.getSource().sendFeedback(new LiteralText("已经改变了Python解释器超时时间。"), true);
                            return 1;
                        })
                    )
                ).then(
                    literal("permitCommandBlocks").then(
                        argument("value", bool()).executes(context -> {
                            commandBlocksAllowed = getBool(context, "value");
                            context.getSource().sendFeedback(
                                commandBlocksAllowed ? new LiteralText("现在允许命令方块执行PythonCraft命令。") : new LiteralText("现在不允许命令方块执行PythonCraft命令。"), true
                            );
                            return 1;
                        })
                    )
                )
            ).then(
                literal("script").then(
                    argument("path", string()).executes(context -> {
                        String path = getString(context, "path");
                        PYTHON_THREAD_POOL.submit(
                            python -> python.execfile(path), new StandaloneProvider(context, path)
                        );
                        return 1;
                    })
                )
            )
        );
    }

    private static void loadPackages() throws IOException {
        List<Path> packages = new LinkedList<>();

        Files.walkFileTree(
            Paths.get(HOME_DIR, "scripts"),
            (FileOnlyVisitor<Path>) packages::add
        );

        for (Path packagePath : packages) {
            LOGGER.info("正在加载这个PythonCraft包：" + packagePath);

            Package pkg;
            try {
                pkg = Packages.get(packagePath.toAbsolutePath().toString());
            } catch (Exception e) {
                LOGGER.error("无法加载这个PythonCraft包：" + packagePath, e);
                continue;
            }

            LOGGER.info(String.format(
                "成功加载这个PythonCraft包：%s（%s）",
                pkg.getMetaData().getPackageIdentifier(),
                pkg.getMetaData().getPackageFriendlyName()
            ));
        }
    }

    @Override
    public void onInitialize() {
        if (HOME_DIR == null) {
            throw new RuntimeException("无法找到游戏的根目录。请检查您的文件系统权限。");
        }
        try {
            Files.createDirectories(Paths.get(HOME_DIR, "scripts"));
        } catch (IOException e) {
            throw new RuntimeException("无法创建scripts目录。请检查您的文件系统权限。");
        }
        CommandRegistrationCallback.EVENT.register(PythonCraft::registerCommands);

        try {
            loadPackages();
        } catch (IOException e) {
            LOGGER.error("无法加载PythonCraft包。", e);
        }
    }
}
