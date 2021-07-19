package top.frankyang.pre.api.core;

import top.frankyang.pre.api.block.BlockRegistry;
import top.frankyang.pre.api.item.ItemRegistry;
import top.frankyang.pre.loader.core.Package;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class API {
    private static final Map<Package, PrivateAPI> APIs = new ConcurrentHashMap<>();

    /**
     * 获取某个PythonCraft包独享的API实例。
     *
     * @param pkg 某个PythonCraft包。
     * @return 此PythonCraft包独享的API实例。
     */
    public static API getPrivateInstance(Package pkg) {
        return APIs.computeIfAbsent(Objects.requireNonNull(pkg), PrivateAPI::new);
    }

    /**
     * 获取在游戏生命周期内共享的API实例。
     *
     * @return 共享的API实例。
     */
    public static API getPublicInstance() {
        return PublicAPISingleton.INSTANCE;
    }

    public abstract BlockRegistry getBlockRegistry();

    public abstract ItemRegistry getItemRegistry();

    public abstract Package getPackage();

    private static class PublicAPISingleton {
        private static final API INSTANCE = new PublicAPI();
    }

}
