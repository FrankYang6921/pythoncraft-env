package top.frankyang.pre.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import top.frankyang.pre.misc.Version;

import java.nio.file.Path;

/**
 * 当前游戏运行环境的相关属性。
 */
public final class Minecraft {
    private Minecraft() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @SuppressWarnings("deprecation")
    private static FabricLoader getFabricLoader() {
        return FabricLoader.INSTANCE;
    }

    /**
     * 获取当前运行中的游戏的环境类型。
     *
     * @return 环境类型。
     */
    public static EnvType getEnvironment() {
        return getFabricLoader().getEnvironmentType();
    }

    /**
     * 获取当前运行中的游戏是否是客户端。
     *
     * @return 是否是客户端。
     * @apiNote 请勿用这个方法检查是否可以获取一个客户端实例，因为可能出现类加载异常（在服务端环境中没有对应的类）。作为替代，您应该创建一个客户端专用的入口文件。
     */
    public static boolean isClient() {
        return getFabricLoader().getEnvironmentType() == EnvType.CLIENT;
    }

    /**
     * 获取当前运行中的游戏是否是服务端。
     *
     * @return 是否是服务端。
     * @apiNote 请勿用这个方法检查是否可以获取一个服务端实例，因为可能出现类加载异常（在客户端环境中没有对应的类）。作为替代，您应该创建一个服务端专用的入口文件。
     */
    public static boolean isServer() {
        return getFabricLoader().getEnvironmentType() == EnvType.SERVER;
    }

    /**
     * 获取当前游戏的客户端实例。
     *
     * @return 获取到的客户端实例。
     * @throws UnsupportedOperationException 如果当前的游戏是服务端而非客户端。
     */
    public static MinecraftClient getClient() {
        if (isServer())
            throw new UnsupportedOperationException("Cannot get a `MinecraftClient` instance in a Minecraft server.");
        return (MinecraftClient) getFabricLoader().getGameInstance();
    }

    /**
     * 获取当前游戏的服务端实例。
     *
     * @return 获取到的服务端实例。
     * @throws UnsupportedOperationException 如果当前的游戏是客户端而非服务端。
     * @apiNote 为了避免类加载异常（在服务端环境中没有对应的类），这个方法只会返回单独的服务端实例，而非一个客户端的内置服务端实例。想要获取一个客户端的内置服务端，请使用调用它的<code>getServer()</code>方法。
     */
    public static MinecraftServer getServer() {
        if (isClient())
            throw new UnsupportedOperationException("Cannot get a `MinecraftServer` instance in a Minecraft client.");
        return (MinecraftServer) getFabricLoader().getGameInstance();
    }

    /**
     * 获取当前游戏的配置文件目录。
     *
     * @return 配置文件目录。
     */
    public static Path getConfigPath() {
        return getFabricLoader().getConfigDir();
    }

    /**
     * 获取当前游戏的根目录。
     *
     * @return 根目录。
     */
    public static Path getGamePath() {
        return getFabricLoader().getGameDir();
    }

    /**
     * 获取当前游戏的模组目录。
     *
     * @return 模组目录。
     */
    public static Path getModsPath() {
        return getFabricLoader().getModsDir();
    }

    /**
     * 获取当前游戏的PythonCraft包目录。
     *
     * @return PythonCraft包目录。
     */
    public static Path getPackagesPath() {
        return getFabricLoader().getGameDir().resolve("packages");
    }

    /**
     * 获取当前游戏的版本号。
     *
     * @return 当前游戏的版本号。
     */
    public static Version getGameVersion() {
        return new Version(
            getFabricLoader().getGameProvider().getNormalizedGameVersion().split("-")[0]
        );
    }


    /**
     * 获取当前游戏启动时收到的所有参数。
     *
     * @return 当前游戏启动时收到的所有参数。
     */
    public static String[] getArguments() {
        return getFabricLoader().getLaunchArguments(false);
    }
}
