package top.frankyang.pre.api.item.food;

import net.minecraft.item.FoodComponent;
import org.python.core.PyDictionary;
import top.frankyang.pre.api.misc.DelegatedCastable;
import top.frankyang.pre.api.util.TypedDictionary;

/**
 * 包装类，包装原版类{@link FoodComponent}。
 */
public class Food extends DelegatedCastable<FoodComponent> {
    protected Food(FoodComponent delegate) {
        super(delegate);
    }

    /**
     * 将一个Python字典解析为食物属性。
     *
     * @param dictionary 用于解析的Python字典。
     * @return 解析后的<code>Food</code>实例。
     */
    @SuppressWarnings("CodeBlock2Expr")
    public static Food of(PyDictionary dictionary) {
        TypedDictionary dict = new TypedDictionary(dictionary);

        FoodComponent.Builder foodComponentBuilder = new FoodComponent.Builder();
        dict.ifPresent("hunger", Integer.class, foodComponentBuilder::hunger);
        dict.ifPresent("saturationModifier", Number.class, n -> {
            foodComponentBuilder.saturationModifier(n.floatValue());
        });
        dict.ifTrue("meat", foodComponentBuilder::meat);
        dict.ifTrue("alwaysEdible", foodComponentBuilder::alwaysEdible);
        dict.ifTrue("snack", foodComponentBuilder::snack);
        // TODO 加入自定义的状态效果以兼容statusEffect()方法

        return new Food(foodComponentBuilder.build());
    }
}
