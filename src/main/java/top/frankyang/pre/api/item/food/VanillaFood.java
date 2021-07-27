package top.frankyang.pre.api.item.food;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;

/**
 * 枚举类，表示原版的食物。
 */
public enum VanillaFood implements FoodLike {
    APPLE(FoodComponents.APPLE),
    BAKED_POTATO(FoodComponents.BAKED_POTATO),
    BEEF(FoodComponents.BEEF),
    BEETROOT(FoodComponents.BEETROOT),
    BEETROOT_SOUP(FoodComponents.BEETROOT_SOUP),
    BREAD(FoodComponents.BREAD),
    CARROT(FoodComponents.CARROT),
    CHICKEN(FoodComponents.CHICKEN),
    CHORUS_FRUIT(FoodComponents.CHORUS_FRUIT),
    COD(FoodComponents.COD),
    COOKED_BEEF(FoodComponents.COOKED_BEEF),
    COOKED_CHICKEN(FoodComponents.COOKED_CHICKEN),
    COOKED_COD(FoodComponents.COOKED_COD),
    COOKED_MUTTON(FoodComponents.COOKED_MUTTON),
    COOKED_PORKCHOP(FoodComponents.COOKED_PORKCHOP),
    COOKED_RABBIT(FoodComponents.COOKED_RABBIT),
    COOKED_SALMON(FoodComponents.COOKED_SALMON),
    COOKIE(FoodComponents.COOKIE),
    DRIED_KELP(FoodComponents.DRIED_KELP),
    ENCHANTED_GOLDEN_APPLE(FoodComponents.ENCHANTED_GOLDEN_APPLE),
    GOLDEN_APPLE(FoodComponents.GOLDEN_APPLE),
    GOLDEN_CARROT(FoodComponents.GOLDEN_CARROT),
    HONEY_BOTTLE(FoodComponents.HONEY_BOTTLE),
    MELON_SLICE(FoodComponents.MELON_SLICE),
    MUSHROOM_STEW(FoodComponents.MUSHROOM_STEW),
    MUTTON(FoodComponents.MUTTON),
    POISONOUS_POTATO(FoodComponents.POISONOUS_POTATO),
    PORKCHOP(FoodComponents.PORKCHOP),
    POTATO(FoodComponents.POTATO),
    PUFFERFISH(FoodComponents.PUFFERFISH),
    PUMPKIN_PIE(FoodComponents.PUMPKIN_PIE),
    RABBIT(FoodComponents.RABBIT),
    RABBIT_STEW(FoodComponents.RABBIT_STEW),
    ROTTEN_FLESH(FoodComponents.ROTTEN_FLESH),
    SALMON(FoodComponents.SALMON),
    SPIDER_EYE(FoodComponents.SPIDER_EYE),
    SUSPICIOUS_STEW(FoodComponents.SUSPICIOUS_STEW),
    SWEET_BERRIES(FoodComponents.SWEET_BERRIES),
    TROPICAL_FISH(FoodComponents.TROPICAL_FISH);

    private final FoodComponent delegate;

    VanillaFood(FoodComponent delegate) {
        this.delegate = delegate;
    }

    @Override
    public FoodComponent convert() {
        return delegate;
    }
}
