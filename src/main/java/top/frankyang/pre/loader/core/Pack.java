package top.frankyang.pre.loader.core;

import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import top.frankyang.pre.misc.FakeMetadata;

import java.nio.file.Path;

public class Pack extends ModNioResourcePack {
    public Pack(String identifier, Path path) {
        super(new FakeMetadata(identifier), path, null, null);
    }
}
