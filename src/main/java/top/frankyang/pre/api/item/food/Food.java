package top.frankyang.pre.api.item.food;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import top.frankyang.pre.api.misc.DelegatedCastable;

/**
 * 包装类，包装原版类{@link FoodComponent}。
 */
public class Food extends DelegatedCastable<FoodComponent> implements FoodLike {
    protected Food(FoodComponent delegate) {
        super(delegate);
    }

    public static Builder of() {
        return new Builder();
    }

    public static class Builder {
        private final FoodComponent.Builder foodComponentBuilder = new FoodComponent.Builder();

        public Food.Builder hunger(int hunger) {
            foodComponentBuilder.hunger(hunger);
            return this;
        }

        public Food.Builder saturationModifier(float saturationModifier) {
            foodComponentBuilder.saturationModifier(saturationModifier);
            return this;
        }

        public Food.Builder meat() {
            foodComponentBuilder.meat();
            return this;
        }

        public Food.Builder alwaysEdible() {
            foodComponentBuilder.alwaysEdible();
            return this;
        }

        public Food.Builder snack() {
            foodComponentBuilder.snack();
            return this;
        }

        public Food.Builder statusEffect(StatusEffectInstance effect, float chance) {  // TODO add status effects
            foodComponentBuilder.statusEffect(effect, chance);
            return this;
        }

        public Food build() {
            return new Food(foodComponentBuilder.build());
        }
    }
}
