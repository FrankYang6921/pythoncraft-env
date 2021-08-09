package top.frankyang.pre.api.entity;

import net.minecraft.entity.player.PlayerEntity;
import top.frankyang.pre.api.misc.DelegatedCastable;

public class PlayerImpl extends DelegatedCastable<PlayerEntity> implements PlayerLike<PlayerEntity> {
    public PlayerImpl(PlayerEntity delegate) {
        super(delegate);
    }
}
