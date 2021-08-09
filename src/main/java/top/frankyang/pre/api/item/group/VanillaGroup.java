package top.frankyang.pre.api.item.group;

import net.minecraft.item.ItemGroup;

/**
 * 枚举类，表示原版的9种物品组。分别是：
 * <ol>
 *     <li>建筑方块：<code>BUILDING_BLOCKS</code></li>
 *     <li>装饰性方块：<code>DECORATIONS</code></li>
 *     <li>红石：<code>REDSTONE</code></li>
 *     <li>交通运输：<code>TRANSPORTATION</code></li>
 *     <li>杂项：<code>MISC</code></li>
 *     <li>食物：<code>FOOD</code></li>
 *     <li>工具：<code>TOOLS</code></li>
 *     <li>武器：<code>COMBAT</code></li>
 *     <li>酿造：<code>BREWING</code></li>
 * </ol>
 */
public enum VanillaGroup implements GroupLike {
    BUILDING_BLOCKS(ItemGroup.BUILDING_BLOCKS),
    DECORATIONS(ItemGroup.DECORATIONS),
    REDSTONE(ItemGroup.REDSTONE),
    TRANSPORTATION(ItemGroup.TRANSPORTATION),
    MISC(ItemGroup.MISC),
    FOOD(ItemGroup.FOOD),
    TOOLS(ItemGroup.TOOLS),
    COMBAT(ItemGroup.COMBAT),
    BREWING(ItemGroup.BREWING);

    private final ItemGroup delegate;

    VanillaGroup(ItemGroup delegate) {
        this.delegate = delegate;
    }

    @Override
    public ItemGroup cast() {
        return delegate;
    }
}
