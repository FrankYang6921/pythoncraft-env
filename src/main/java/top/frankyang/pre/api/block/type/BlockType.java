package top.frankyang.pre.api.block.type;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
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
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.BlockSettings;
import top.frankyang.pre.api.block.entity.BlockEntityFactory;
import top.frankyang.pre.api.block.event.BlockEvent;
import top.frankyang.pre.api.block.event.BlockInteractionEvent;
import top.frankyang.pre.api.block.event.BlockUsageEvent;
import top.frankyang.pre.api.block.event.EntityPhysicsEvent;
import top.frankyang.pre.api.block.state.BlockStateFactory;
import top.frankyang.pre.api.block.state.MutableBlockState;
import top.frankyang.pre.api.entity.EntityImpl;
import top.frankyang.pre.api.entity.PlayerImpl;
import top.frankyang.pre.api.event.EventSources;
import top.frankyang.pre.api.event.EventSourcesImpl;
import top.frankyang.pre.api.event.EventType;
import top.frankyang.pre.api.event.ExposedEventSources;
import top.frankyang.pre.api.math.Facing;
import top.frankyang.pre.api.reflect.DynamicOverride;
import top.frankyang.pre.api.reflect.DynamicOverrider;
import top.frankyang.pre.api.reflect.MethodContainer;
import top.frankyang.pre.api.util.ArrayUtils;
import top.frankyang.pre.api.world.WorldImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BlockType extends DynamicOverrider<Block> implements ExposedEventSources<BlockEvent> {
    private static final Map<Block, BlockType> map = new HashMap<>();
    private final EventSources<BlockEvent> eventSources = new EventSourcesImpl<>(
        "onBreakageStart",          // onBreak
        "onBreakageFinish",         // onBroken
        "afterBreakage",            // afterBreak
        "onDestroyedByExplosion",   // onDestroyedByExplosion
        "onEntitySteppedOn",        // onSteppedOn
        "onEntityLandedOn",         // onLandedUpon
        "onPlacement",              // onPlaced
        "onUsage"                   // onUse
    );
    private final BlockStateFactory blockStateFactory;
    private final BlockEntityFactory blockEntityFactory;

    public BlockType(BlockSettings settings) {
        this(settings, null, null);
    }

    public BlockType(BlockSettings settings, BlockStateFactory blockStateFactory) {
        this(settings, blockStateFactory, null);
    }

    public BlockType(BlockSettings settings,
                     BlockStateFactory blockStateFactory,
                     BlockEntityFactory blockEntityFactory) {
        this(settings, blockStateFactory, blockEntityFactory, new Class<?>[0]);
    }

    public BlockType(BlockSettings settings,
                     BlockStateFactory blockStateFactory,
                     BlockEntityFactory blockEntityFactory,
                     Class<?>... interfaces) {
        super(Block.class,
            blockEntityFactory != null ?
                ArrayUtils.mergeArrays(Class.class, interfaces, BlockEntityProvider.class) : interfaces
        );
        this.blockStateFactory = blockStateFactory;
        this.blockEntityFactory = blockEntityFactory;

        superConstructor(new Class[]{AbstractBlock.Settings.class}, settings.cast());

        if (blockEntityFactory != null) {
            blockEntityFactory.newType(this);
        }

        map.put(cast(), this);
    }

    public static BlockType ofVanilla(Block block) {
        return Objects.requireNonNull(map.get(block));
    }

    public static boolean isPythonCraft(Block block) {
        return map.containsKey(block);
    }

    @Override
    public Block superConstructor(Class<?>[] classes, Object... args) {
        super.superConstructor(classes, args);
        if (blockStateFactory != null) {
            invoke(
                "setDefaultState",
                BlockState.class,
                blockStateFactory.putInitial(getTarget().getStateManager().getDefaultState())
            );
        }
        return getTarget();
    }

    @DynamicOverride
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state, MethodContainer superMethod) {
        superMethod.invoke(world, pos, state);
        if (!world.isClient()) {
            trigger("onBreakageFinish", new BlockInteractionEvent(
                EventType.BLOCK_BREAK_FINISH,
                this,
                new BlockPosition(pos),
                new WorldImpl(world),
                null,
                new MutableBlockState(state, world, pos)
            ));
        }
    }

    @DynamicOverride
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion, MethodContainer superMethod) {
        superMethod.invoke(world, pos, explosion);
        if (!world.isClient()) {
            trigger("onDestroyedByExplosion", new BlockInteractionEvent(
                EventType.BLOCK_BREAK_EXPLOSION,
                this,
                new BlockPosition(pos),
                new WorldImpl(world),
                null,
                new MutableBlockState(world, pos)
            ));
        }
    }

    @DynamicOverride
    public void onSteppedOn(World world, BlockPos pos, Entity entity, MethodContainer superMethod) {
        superMethod.invoke(world, pos, entity);
        if (!world.isClient()) {
            trigger("onEntitySteppedOn", new EntityPhysicsEvent(
                EventType.BLOCK_LAND,
                this,
                new BlockPosition(pos),
                new WorldImpl(world),
                new EntityImpl(entity)
            ));
        }
    }

    @DynamicOverride
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, MethodContainer superMethod) {
        superMethod.invoke(world, pos, state, placer, itemStack);
    }

    @DynamicOverride
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance, MethodContainer superMethod) {
        superMethod.invoke(world, pos, entity, distance);
        if (!world.isClient()) {
            trigger("onEntityLandedOn", new EntityPhysicsEvent(
                EventType.BLOCK_LAND,
                this,
                new BlockPosition(pos),
                new WorldImpl(world),
                new EntityImpl(entity)
            ));
        }
    }

    @DynamicOverride
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, MethodContainer superMethod) {
        superMethod.invoke(world, pos, state, player);
        if (!world.isClient()) {
            trigger("onBreakageStart", new BlockInteractionEvent(
                EventType.BLOCK_BREAK_START,
                this,
                new BlockPosition(pos),
                new WorldImpl(world),
                new PlayerImpl(player),
                new MutableBlockState(state, world, pos)
            ));
        }
    }

    @DynamicOverride
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, MethodContainer superMethod) {
        superMethod.invoke(world, player, pos, state, blockEntity, stack);
        if (!world.isClient()) {
            trigger("afterBreakage", new BlockInteractionEvent(
                EventType.BLOCK_BREAK_AFTER,
                this,
                new BlockPosition(pos),
                new WorldImpl(world),
                new PlayerImpl(player),
                new MutableBlockState(state, world, pos)
            ));
        }
    }

    @DynamicOverride
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, MethodContainer superMethod) {
        ActionResult superResult = superMethod.invoke(state, world, pos, player, hand, hit);
        if (superResult != ActionResult.PASS) {
            return superResult;
        }
        if (!world.isClient()) {
            trigger("onUsage", new BlockUsageEvent(
                this,
                new BlockPosition(pos),
                new WorldImpl(world),
                new PlayerImpl(player),
                new MutableBlockState(state, world, pos),
                hand == Hand.MAIN_HAND,
                Facing.of(hit.getSide())
            ));
        }

        return getBackingMap().get("onUsage").getEventListeners().size() != 0 ? ActionResult.SUCCESS : ActionResult.PASS;
    }

    @DynamicOverride
    public void appendProperties(StateManager.Builder<Block, BlockState> stateManager, MethodContainer superMethod) {
        if (blockStateFactory != null) {
            blockStateFactory.addProperties(stateManager::add);
        } else {
            superMethod.invoke(stateManager);
        }
    }

    @DynamicOverride(required = false)
    public BlockEntity createBlockEntity(BlockView world, MethodContainer superMethod) {
        return blockEntityFactory.get();
    }

    @Override
    public EventSources<BlockEvent> getDelegateEventSources() {
        return eventSources;
    }
}
