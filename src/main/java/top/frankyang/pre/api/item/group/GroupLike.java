package top.frankyang.pre.api.item.group;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import top.frankyang.pre.api.misc.Castable;

/**
 * 包装类接口，包装原版类{@link ItemGroup}。
 */
public interface GroupLike extends Castable<ItemGroup> {
    /**
     * 获取一个物品组的图标。
     *
     * @return 图标。。
     */
    default ItemStack getIcon() {  // TODO 用包装类替换ItemStack
        return cast().getIcon();
    }

    /**
     * 获取一个物品组的名称。
     *
     * @return 组名。
     */
    default String getName() {
        return cast().getName();
    }
}
