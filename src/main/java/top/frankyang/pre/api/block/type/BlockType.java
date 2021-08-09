package top.frankyang.pre.api.block.type;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import top.frankyang.pre.api.block.BlockSettings;
import top.frankyang.pre.api.block.entity.BlockEntityData;
import top.frankyang.pre.api.block.event.BlockEvent;
import top.frankyang.pre.api.block.state.BlockStateData;
import top.frankyang.pre.api.event.DelegatedEventSources;
import top.frankyang.pre.api.event.EventSources;
import top.frankyang.pre.api.event.EventSourcesImpl;
import top.frankyang.pre.api.util.reflection.DynamicOverride;
import top.frankyang.pre.api.util.reflection.DynamicOverrider;
import top.frankyang.pre.api.util.reflection.MethodContainer;

public class BlockType extends DynamicOverrider<Block> implements DelegatedEventSources<BlockEvent<?>> {
    private final EventSources<BlockEvent<?>> eventSources = new EventSourcesImpl<>();
    private BlockStateData blockStateData;
    private BlockEntityData blockEntityData;

    public BlockType(BlockSettings settings) {
        this(settings, null, null);
    }

    public BlockType(BlockSettings settings, BlockStateData blockStateData) {
        this(settings, blockStateData, null);
    }

    public BlockType(BlockSettings settings, BlockStateData blockStateData, BlockEntityData blockEntityData) {
        this(Block.class);
        this.blockStateData = blockStateData;
        this.blockEntityData = blockEntityData;
        createTarget(new Class[]{AbstractBlock.Settings.class}, settings.cast());
    }

    private BlockType(Class<? extends Block> targetClass) {
        super(targetClass);
    }

    @Override
    public void createTarget(Class<?>[] classes, Object... args) {
        super.createTarget(classes, args);
        if (blockStateData != null) {
            invoke(
                "setDefaultState", BlockState.class,
                blockStateData.putAll(
                    getTarget().getStateManager().getDefaultState()
                )
            );
        }
    }

    @DynamicOverride
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state, MethodContainer superMethod) {
        superMethod.invokeLiteral(world, pos, state);
    }

    @DynamicOverride
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion, MethodContainer superMethod) {
        superMethod.invokeLiteral(world, pos, explosion);
    }

    @DynamicOverride
    public void onSteppedOn(World world, BlockPos pos, Entity entity, MethodContainer superMethod) {
        superMethod.invokeLiteral(world, pos, entity);
    }

    @DynamicOverride
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, MethodContainer superMethod) {
        superMethod.invokeLiteral(world, pos, state, placer, itemStack);
    }

    @DynamicOverride
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance, MethodContainer superMethod) {
        superMethod.invokeLiteral(world, pos, entity, distance);
    }

    @DynamicOverride
    public void onEntityLand(BlockView world, Entity entity, MethodContainer superMethod) {
        superMethod.invokeLiteral(world, entity);
    }

    @DynamicOverride
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, MethodContainer superMethod) {
        superMethod.invokeLiteral(world, pos, state, player);
    }

    @DynamicOverride
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, MethodContainer superMethod) {
        superMethod.invokeLiteral(world, player, pos, state, blockEntity, stack);
    }

    @DynamicOverride
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, MethodContainer superMethod) {
        return superMethod.invokeLiteral(state, world, player, hand, hit);
    }

    @DynamicOverride
    public void appendProperties(StateManager.Builder<Block, BlockState> stateManager, MethodContainer superMethod) {
        if (blockStateData != null) {
            blockStateData.addAll(stateManager::add);
        }
    }

    @Override
    public EventSources<BlockEvent<?>> getEventSources() {
        return eventSources;
    }
}
