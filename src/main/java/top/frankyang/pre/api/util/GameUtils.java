package top.frankyang.pre.api.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import top.frankyang.pre.main.PythonCraft;
import top.frankyang.pre.util.Versions;

import java.nio.file.Path;
import java.util.Objects;

/**
 * 当前游戏运行环境的相关属性。
 */
public final class GameUtils {
    private GameUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @SuppressWarnings("deprecation")
    public static FabricLoader getFabricLoader() {
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
     */
    public static boolean isClient() {
        return getFabricLoader().getEnvironmentType() == EnvType.CLIENT;
    }

    /**
     * 获取当前运行中的游戏是否是服务端。
     *
     * @return 是否是服务端。
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
            throw new UnsupportedOperationException("Cannot get a `MinecraftClient` instance in a Minecraft *server*.");
        return (MinecraftClient) getFabricLoader().getGameInstance();
    }

    public static MinecraftServer getIntegratedServer() {
        if (isServer())
            throw new UnsupportedOperationException("Cannot get a integrated server instance in a Minecraft *server*.");
        return Objects.requireNonNull(
            getClient().getServer(),
            "Cannot get the integrated server of a Minecraft client when nobody's playing a local world."
        );
    }

    public static MinecraftServer getDedicatedServer() {
        if (isClient())
            throw new UnsupportedOperationException("Cannot get a dedicated server instance in a Minecraft *client*.");
        return (MinecraftServer) getFabricLoader().getGameInstance();
    }

    /**
     * 获取当前游戏的服务端实例。
     *
     * @return 获取到的服务端实例。
     */
    public static MinecraftServer getServer() {
        if (isClient())
            return (MinecraftServer) ReflectUtils.invokeStatic("getIntegratedServer");
        return (MinecraftServer) ReflectUtils.invokeStatic("getDedicatedServer");
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
    public static SemanticVersion getGameVersion() {
        return Versions.ofNonNull(
            getFabricLoader().getGameProvider().getNormalizedGameVersion().split("-")[0]
        );
    }

    /**
     * 获取Fabric的版本号。
     *
     * @return Fabric的版本号。
     */
    public static SemanticVersion getFabricVersion() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取PythonCraft的版本号。
     *
     * @return PythonCraft的版本号。
     */
    public static SemanticVersion getLoaderVersion() {
        return PythonCraft.getInstance().getVersion();
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
