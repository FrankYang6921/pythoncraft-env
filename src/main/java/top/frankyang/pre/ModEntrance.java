package top.frankyang.pre;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import top.frankyang.pre.main.PythonCraft;

public class ModEntrance implements ModInitializer {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onInitialize() {
        PythonCraft.getInstance();
    }
}
