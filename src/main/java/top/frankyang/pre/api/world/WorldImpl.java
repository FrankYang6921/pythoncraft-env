package top.frankyang.pre.api.world;

import net.minecraft.world.WorldAccess;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

public class WorldImpl extends CastableImpl<WorldAccess> implements WorldLike {
    public WorldImpl(WorldAccess delegate) {
        super(delegate);
    }
}
