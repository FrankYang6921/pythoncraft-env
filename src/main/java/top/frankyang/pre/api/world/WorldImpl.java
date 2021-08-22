package top.frankyang.pre.api.world;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.entity.BlockEntityFactory;
import top.frankyang.pre.api.block.entity.BlockEntityImpl;
import top.frankyang.pre.api.block.entity.BlockEntityLike;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.block.state.MutableBlockState;
import top.frankyang.pre.api.misc.DelegatedCastable;
import top.frankyang.pre.api.nbt.NbtObject;

public class WorldImpl extends DelegatedCastable<WorldAccess> implements WorldLike {
    public WorldImpl(WorldAccess delegate) {
        super(delegate);
    }

    @Override
    public int lightLevelAt(BlockPosition position) {
        return delegate.getLuminance(position.cast());
    }

    @Override
    public int getDifficulty() {
        return delegate.getDifficulty().getId();
    }

    @Override
    public float difficultyAt(BlockPosition position) {
        return delegate.getLocalDifficulty(position.cast()).getClampedLocalDifficulty();
    }

    @Override
    public BlockStateLike blockStateAt(BlockPosition position) {
        return blockStateAt0(position.cast());
    }

    private BlockStateLike blockStateAt0(BlockPos position) {
        return new MutableBlockState(delegate.getBlockState(position), delegate, position);
    }

    @Override
    public BlockEntityLike blockEntityAt(BlockPosition position) {
        BlockEntity entity = delegate.getBlockEntity(position.cast());
        if (entity == null) {
            throw new IllegalStateException("No block entity present for such position (yet): " + position);
        }
        if (entity.isRemoved()) {
            throw new IllegalStateException("Block entity already removed for such position: " + position);

        }
        if (entity instanceof BlockEntityImpl.MyBlockEntity) {  // Created in PythonCraft!
            return ((BlockEntityImpl.MyBlockEntity) entity).getParent();
        }

        return new VanillaBlockEntityImpl(entity);
    }

    private class VanillaBlockEntityImpl implements BlockEntityLike {
        private final BlockEntity entity;

        public VanillaBlockEntityImpl(BlockEntity entity) {
            this.entity = entity;
        }

        @Override
        public NbtObject serialize(NbtObject object) {
            return NbtObject.of(entity.toTag(object.cast()));
        }

        @Override
        public void deserialize(BlockStateLike state, NbtObject object) {
            entity.fromTag(state.cast(), object.cast());
        }

        @Override
        public void acquirePersistence() {
            entity.markDirty();
        }

        @Override
        public BlockStateLike getState() {
            return new MutableBlockState(entity.getCachedState(), delegate, entity.getPos());
        }

        @Override
        public BlockEntityFactory getFactory() {
            throw new UnsupportedOperationException();
        }

        @Override
        public BlockEntity cast() {
            return entity;
        }
    }
}
