package top.frankyang.pre.api.item.type;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.block.state.ImmutableBlockState;
import top.frankyang.pre.api.entity.PlayerImpl;
import top.frankyang.pre.api.entity.PlayerLike;
import top.frankyang.pre.api.event.EventBus;
import top.frankyang.pre.api.event.EventBusImpl;
import top.frankyang.pre.api.event.ExposedEventBus;
import top.frankyang.pre.api.item.ItemInstance;
import top.frankyang.pre.api.item.ItemSettings;
import top.frankyang.pre.api.item.event.ItemEvent;
import top.frankyang.pre.api.item.event.ItemUsageEvent;
import top.frankyang.pre.api.item.event.ItemUsageOnBlockEvent;
import top.frankyang.pre.api.math.Facing;
import top.frankyang.pre.api.reflect.DynamicOverride;
import top.frankyang.pre.api.reflect.DynamicOverrider;
import top.frankyang.pre.api.reflect.MethodContainer;
import top.frankyang.pre.api.world.WorldImpl;
import top.frankyang.pre.api.world.WorldLike;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemType extends DynamicOverrider<Item> implements ExposedEventBus<ItemEvent> {
    protected static final Map<Item, ItemType> map = new HashMap<>();
    private final EventBus<ItemEvent> eventBus = new EventBusImpl<>(
        "onUsageOnBlock",
        "onUsage"
    );

    public ItemType(ItemSettings settings) {
        this(settings, Item.class);
    }

    public ItemType(ItemSettings settings, Class<? extends Item> targetClass) {
        this(settings, targetClass, new Class<?>[0]);
    }

    public ItemType(ItemSettings settings, Class<? extends Item> targetClass, Class<?>... interfaces) {
        super(targetClass, interfaces);
        superConstructor(new Class<?>[]{Item.Settings.class}, settings.cast());
        map.put(cast(), this);
    }

    protected ItemType(Class<? extends Item> targetClass, Class<?>... interfaces) {
        super(targetClass, interfaces);
    }

    public static ItemType ofVanilla(Item item) {
        return Objects.requireNonNull(map.get(item));
    }

    public static boolean isPythonCraft(Item item) {
        return map.containsKey(item);
    }

    @DynamicOverride
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return canMine(
            new ImmutableBlockState(state),
            new WorldImpl(world),
            new PlayerImpl(miner),
            new BlockPosition(pos)
        );
    }

    public boolean canMine(BlockStateLike state, WorldLike world, PlayerLike<?> player, BlockPosition position) {
        return true;
    }

    @DynamicOverride
    public ActionResult useOnBlock(ItemUsageContext context, MethodContainer superMethod) {
        ActionResult superResult = superMethod.invoke(context);
        if (!context.getWorld().isClient()) {
            trigger("onUsageOnBlock", new ItemUsageOnBlockEvent(
                this,
                new WorldImpl(context.getWorld()),
                new PlayerImpl(context.getPlayer()),
                new BlockPosition(context.getBlockPos()),
                context.getHand() == Hand.MAIN_HAND,
                new ItemInstance(context.getStack()),
                Facing.of(context.getSide())));
        }

        if (superResult != ActionResult.PASS) {
            return superResult;
        }
        return getSource("onUsageOnBlock").getListenerCount() == 0 ? ActionResult.PASS : ActionResult.SUCCESS;
    }

    @DynamicOverride
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, MethodContainer superMethod) {
        TypedActionResult<ItemStack> superResult = superMethod.invoke(world, user, hand);

        ItemInstance instance;
        if (!world.isClient()) {
            instance = new ItemInstance(user.getStackInHand(hand));  // Lazy load
            trigger("onUsage", new ItemUsageEvent(
                this,
                new WorldImpl(world),
                new PlayerImpl(user),
                hand == Hand.MAIN_HAND,
                instance));
        } else {
            return superResult;
        }

        if (superResult.getResult() != ActionResult.PASS) {
            return superResult;
        }
        return getSource("onUsage").getListenerCount() == 0 ?
            TypedActionResult.pass(instance.cast()) :
            TypedActionResult.success(instance.cast());
    }

    @Override
    public EventBus<ItemEvent> getDelegateEventSources() {
        return eventBus;
    }
}
