package top.frankyang.pre.main;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import top.frankyang.pre.gui.PackageExceptionFrame;
import top.frankyang.pre.loader.PackageManager;
import top.frankyang.pre.loader.core.Pack;
import top.frankyang.pre.loader.core.PackageLoader;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PythonCraft extends AbstractPythonCraft {
    public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    private PackageManager pkgMgr;

    private PythonCraft() {
        super();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static PythonCraft getInstance() {
        return PythonCraftSingleton.INSTANCE;
    }

    private void initialize() {
        pkgMgr = new PackageManager(() ->
            new PackageLoader(Paths.get(envRoot, "scripts"))
        );
        pkgMgr.tryToConstruct(PackageExceptionFrame::open);

        Registry.register(Registry.BLOCK, new Identifier("pre", "example_block"), EXAMPLE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("pre", "example_block"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
    }

    public PackageManager getPackageManager() {
        return pkgMgr;
    }

    public URLClassLoader getUserClassLoader() {
        return pkgMgr.getUserClassLoader();
    }

    public List<Path> getUserClassPaths() {
        return pkgMgr.getUserClassPaths();
    }

    public List<Pack> getUserResourcePacks() {
        return pkgMgr.getUserResourcePacks();
    }

    private static class PythonCraftSingleton {
        private static final PythonCraft INSTANCE = new PythonCraft();

        static {
            INSTANCE.initialize();
        }
    }
}
