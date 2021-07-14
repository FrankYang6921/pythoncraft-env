package top.frankyang.pre.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

public final class MinecraftInfo {
    @SuppressWarnings("deprecation")
    private static FabricLoader getFabricLoader() {
        return FabricLoader.INSTANCE;
    }

    /**
     * 获取当前运行中的游戏的环境类型。
     * @return 环境类型。
     */
    public static EnvType getEnvironment() {
        return getFabricLoader().getEnvironmentType();
    }

    /**
     * 获取当前运行中的游戏是否是客户端。
     * @return 是否是客户端。
     */
    public static boolean isClient() {
        return getFabricLoader().getEnvironmentType() == EnvType.CLIENT;
    }

    /**
     * 获取当前运行中的游戏是否是服务端。
     * @return 是否是服务端。
     */
    public static boolean isServer() {
        return getFabricLoader().getEnvironmentType() == EnvType.SERVER;
    }

    /**
     * 获取当前游戏的客户端实例。
     * @throws UnsupportedOperationException 如果当前的游戏是服务端而非客户端。
     * @return 获取到的客户端实例。
     */
    public static MinecraftClient getClient() {
        if (isServer())
            throw new UnsupportedOperationException("Cannot get a `MinecraftClient` instance in a Minecraft server.");
        return (MinecraftClient) getFabricLoader().getGameInstance();
    }

    /**
     * 获取当前游戏的客户端实例。
     * @throws UnsupportedOperationException 如果当前的游戏是服务端而非客户端。
     * @return 获取到的客户端实例。
     */
    public static MinecraftServer getServer() {
        if (isClient())
            throw new UnsupportedOperationException("Cannot get a `MinecraftServer` instance in a Minecraft client.");
        return (MinecraftServer) getFabricLoader().getGameInstance();
    }
}
