package top.frankyang.pre.api.entity;

import net.minecraft.entity.Entity;
import top.frankyang.pre.api.misc.DelegatedCastable;

public class EntityImpl extends DelegatedCastable<Entity> implements EntityLike<Entity> {
    public EntityImpl(Entity delegate) {
        super(delegate);
    }
}
