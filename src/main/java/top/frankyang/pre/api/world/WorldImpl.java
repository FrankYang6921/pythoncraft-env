package top.frankyang.pre.api.world;

import net.minecraft.world.World;
import top.frankyang.pre.api.misc.DelegatedCastable;

public class WorldImpl extends DelegatedCastable<World> implements WorldLike {
    public WorldImpl(World delegate) {
        super(delegate);
    }
}
