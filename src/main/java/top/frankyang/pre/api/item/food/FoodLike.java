package top.frankyang.pre.api.item.food;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import top.frankyang.pre.api.misc.Convertable;

import java.util.List;

/**
 * 包装类接口，包装原版类<code>FoodComponent</code>。
 */
public interface FoodLike extends Convertable<FoodComponent> {
    /**
     * 获取该食物可以恢复的饱食度，以半格为单位。
     *
     * @return 该食物可以恢复的饱食度。
     */
    default int getHunger() {
        return convert().getHunger();
    }

    /**
     * 获取该食物的饱和指数。饱和指数越大，玩家食用此食物后可以维持饱食度不变的时间就越长。
     *
     * @return 该食物的饱和指数。
     */
    default float getSaturationModifier() {
        return convert().getSaturationModifier();
    }

    /**
     * 获取该食物是否是肉食。如果是，则意味着该食物也可以给狗食用。
     *
     * @return 是否是肉食。
     */
    default boolean isMeat() {
        return convert().isMeat();
    }

    /**
     * 获取该食物是否永远可食用。如果是，玩家即使饱食度已满也可食用该食物。
     *
     * @return 是否永远可食用。
     */
    default boolean isAlwaysEdible() {
        return convert().isAlwaysEdible();
    }

    /**
     * 获取该食物是否是零食。如果是，玩家食用该食物的速度比一般食物要快。
     *
     * @return 是否是零食。
     */
    default boolean isSnack() {
        return convert().isSnack();
    }

    /**
     * 获取食用该食物后可能获得的状态效果。
     *
     * @return &lt;尚未完工&gt;
     */
    default List<Pair<StatusEffectInstance, Float>> getStatusEffects() {  // TODO 加入自定义的状态效果以兼容此方法
        return convert().getStatusEffects();
    }
}
