package top.frankyang.pre;

import net.fabricmc.api.ModInitializer;
import top.frankyang.pre.main.PythonCraft;

public class ModEntrance implements ModInitializer {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onInitialize() {
        System.setProperty("java.awt.headless", "false");
        PythonCraft.getInstance();
    }
}
