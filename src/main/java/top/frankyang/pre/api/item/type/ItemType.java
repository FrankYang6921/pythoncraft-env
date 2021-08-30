package top.frankyang.pre.api.item.type;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.frankyang.pre.api.block.BlockPosition;
import top.frankyang.pre.api.block.state.BlockStateLike;
import top.frankyang.pre.api.block.state.ImmutableBlockState;
import top.frankyang.pre.api.entity.PlayerImpl;
import top.frankyang.pre.api.entity.PlayerLike;
import top.frankyang.pre.api.event.EventSources;
import top.frankyang.pre.api.event.EventSourcesImpl;
import top.frankyang.pre.api.event.ExposedEventSources;
import top.frankyang.pre.api.item.ItemSettings;
import top.frankyang.pre.api.item.event.ItemEvent;
import top.frankyang.pre.api.item.event.ItemUsageEvent;
import top.frankyang.pre.api.reflect.DynamicOverride;
import top.frankyang.pre.api.reflect.DynamicOverrider;
import top.frankyang.pre.api.reflect.MethodContainer;
import top.frankyang.pre.api.world.WorldImpl;
import top.frankyang.pre.api.world.WorldLike;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemType extends DynamicOverrider<Item> implements ExposedEventSources<ItemEvent> {
    private static final Map<Item, ItemType> map = new HashMap<>();
    private final EventSources<ItemEvent> eventSources = new EventSourcesImpl<>("onUsageOnBlock");

    public ItemType(ItemSettings settings, Class<?>... interfaces) {
        super(Item.class, interfaces);
        superConstructor(new Class[]{Item.Settings.class}, settings.cast());
        map.put(cast(), this);
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
        superMethod.invoke(context);
        if (!context.getWorld().isClient())
            trigger("onUsageOnBlock", new ItemUsageEvent(
                this,
                new WorldImpl(context.getWorld()),
                new PlayerImpl(context.getPlayer()),
                new BlockPosition(context.getBlockPos()),
                context.getHand() == Hand.MAIN_HAND
            ));
        return getBackingMap().get("onUsageOnBlock").getEventListeners().size() != 0 ? ActionResult.SUCCESS : ActionResult.PASS;
    }

    @Override
    public EventSources<ItemEvent> getDelegateEventSources() {
        return eventSources;
    }
}
