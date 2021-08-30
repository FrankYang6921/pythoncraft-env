package top.frankyang.pre.api.world;

import net.minecraft.world.WorldAccess;
import top.frankyang.pre.api.misc.DelegatedCastable;

public class WorldImpl extends DelegatedCastable<WorldAccess> implements WorldLike {
    public WorldImpl(WorldAccess delegate) {
        super(delegate);
    }
}
