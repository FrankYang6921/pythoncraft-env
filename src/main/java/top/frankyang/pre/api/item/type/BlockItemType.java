package top.frankyang.pre.api.item.type;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import top.frankyang.pre.api.block.type.BlockType;
import top.frankyang.pre.api.item.ItemSettings;

public class BlockItemType extends ItemType {
    private BlockType blockType;

    public BlockItemType(BlockType blockType, ItemSettings settings) {
        this(blockType, settings, BlockItem.class);
    }

    public BlockItemType(BlockType blockType, ItemSettings settings, Class<?>... interfaces) {
        super(BlockItem.class, interfaces);
        superConstructor(new Class<?>[]{Block.class, Item.Settings.class}, blockType.cast(), settings.cast());
        map.put(cast(), this);
        this.blockType = blockType;
    }

    protected BlockItemType(Class<? extends Item> targetClass, Class<?>... interfaces) {
        super(targetClass, interfaces);
    }

    public BlockType getBlockType() {
        return blockType;
    }
}
