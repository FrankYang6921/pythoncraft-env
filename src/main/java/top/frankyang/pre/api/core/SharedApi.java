package top.frankyang.pre.api.core;

import top.frankyang.pre.api.block.BlockRegistry;
import top.frankyang.pre.api.item.ItemRegistry;
import top.frankyang.pre.loader.core.Package;

public class SharedApi extends Api {
    SharedApi() {
    }

    @Override
    public BlockRegistry getBlockRegistry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemRegistry getItemRegistry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Package getPackage() {
        throw new UnsupportedOperationException();
    }
}
