package top.frankyang.pre.main;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import top.frankyang.pre.loader.PackageManager;
import top.frankyang.pre.loader.loader.Package;
import top.frankyang.pre.loader.loader.PackageLoaderImpl;
import top.frankyang.pre.python.providers.PackagedProvider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

public class PythonCraft extends AbstractPythonCraft {
    public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    private final PackageManager pkgMgr;

    private PythonCraft() {
        super();
        pkgMgr = new PackageManager(new PackageLoaderImpl(Paths.get(envRoot, "scripts"), this));
        pkgMgr.construct();

        Registry.register(Registry.BLOCK, new Identifier("pre", "example_block"), EXAMPLE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("pre", "example_block"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
    }

    @SuppressWarnings("UnusedReturnValue")
    public static PythonCraft getInstance() {
        return PythonCraftSingleton.INSTANCE;
    }

    public PackageManager getPackageManager() {
        return pkgMgr;
    }

    @Override
    public Future<?> onConstruction(Package pkg) {
        Path path = pkg.getMetaData().getEntrypointPath();

        return pythonThreadPool.submit(
            p -> p.execfile(path),
            new PackagedProvider(
                path.getParent().toString(),
                pkgMgr.getUserClassLoader()
            )
        );
    }

    @Override
    public Future<?> onDestruction(Package pkg) {
        return null;
    }

    private static class PythonCraftSingleton {
        private static final PythonCraft INSTANCE = new PythonCraft();
    }
}
