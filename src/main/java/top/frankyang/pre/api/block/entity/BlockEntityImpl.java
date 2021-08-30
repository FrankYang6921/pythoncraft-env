package top.frankyang.pre.api.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.block.state.MutableBlockState;
import top.frankyang.pre.api.misc.DelegatedCastable;
import top.frankyang.pre.api.nbt.NbtObject;

public abstract class BlockEntityImpl extends DelegatedCastable<BlockEntity> implements BlockEntityLike {
    private final BlockEntityFactory factory;
    private BlockStateLike state;

    protected BlockEntityImpl(BlockEntityFactory factory) {
        this.factory = factory;
        delegate = new MyBlockEntity(factory);
    }

    public BlockStateLike getState() {
        return state;
    }

    @Override
    public BlockEntityFactory getFactory() {
        return factory;
    }

    public class MyBlockEntity extends BlockEntity {
        private final BlockEntityFactory factory;

        public MyBlockEntity(BlockEntityFactory factory) {
            super(factory.getType());
            this.factory = factory;
        }

        public BlockEntityImpl getParent() {
            return BlockEntityImpl.this;
        }

        public BlockEntityFactory getFactory() {
            return factory;
        }

        @Override
        public CompoundTag toTag(CompoundTag tag) {
            super.toTag(tag);
            return serialize(NbtObject.of(tag)).cast();
        }

        @Override
        public void fromTag(BlockState state0, CompoundTag tag) {
            deserialize(state = new MutableBlockState(state0, getWorld(), getPos()), NbtObject.of(tag));
            super.fromTag(state0, tag);
        }
    }
}
