package top.frankyang.pre.api.block.entity;

import net.minecraft.block.entity.BlockEntity;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.block.state.MutableBlockState;
import top.frankyang.pre.api.misc.DelegatedCastable;
import top.frankyang.pre.api.nbt.NbtObject;
import top.frankyang.pre.api.world.WorldLike;

public class VanillaBlockEntityImpl extends DelegatedCastable<BlockEntity> implements BlockEntityLike {
    private final WorldLike world;

    public VanillaBlockEntityImpl(BlockEntity delegate, WorldLike world) {
        super(delegate);
        this.world = world;
    }

    @Override
    public NbtObject serialize(NbtObject object) {
        return NbtObject.of(delegate.toTag(object.cast()));
    }

    @Override
    public void deserialize(BlockStateLike state, NbtObject object) {
        delegate.fromTag(state.cast(), object.cast());
    }

    @Override
    public BlockStateLike getState() {
        return new MutableBlockState(delegate.getCachedState(), world.cast(), delegate.getPos());
    }

    @Override
    public BlockEntityFactory getFactory() {
        throw new UnsupportedOperationException();
    }
}
