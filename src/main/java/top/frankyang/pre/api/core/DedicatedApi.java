package top.frankyang.pre.api.core;

import org.jetbrains.annotations.Nullable;
import top.frankyang.pre.api.block.BlockRegistry;
import top.frankyang.pre.api.item.ItemRegistry;
import top.frankyang.pre.loader.core.Package;

import java.util.Objects;

public class DedicatedApi extends Api {
    private final Package pkg;
    private final BlockRegistry blockRegistry;
    private final ItemRegistry itemRegistry;

    DedicatedApi(@Nullable Package pkg) {
        this.pkg = Objects.requireNonNull(pkg);
        String namespace = pkg.getMetaData().getIdentifier();
        blockRegistry = new BlockRegistry(namespace);
        itemRegistry = new ItemRegistry(namespace);
    }

    public BlockRegistry getBlockRegistry() {
        return blockRegistry;
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }

    public Package getPackage() {
        return pkg;
    }
}
