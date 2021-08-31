package top.frankyang.pre.api.entity;

import net.minecraft.entity.player.PlayerEntity;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

public class PlayerImpl extends CastableImpl<PlayerEntity> implements PlayerLike<PlayerEntity> {
    public PlayerImpl(PlayerEntity delegate) {
        super(delegate);
    }
}
