package top.frankyang.pre.api.entity;

import net.minecraft.entity.Entity;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

public class EntityImpl extends CastableImpl<Entity> implements EntityLike<Entity> {
    public EntityImpl(Entity delegate) {
        super(delegate);
    }
}
