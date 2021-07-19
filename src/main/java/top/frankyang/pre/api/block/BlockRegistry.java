package top.frankyang.pre.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.python.core.PyDictionary;
import top.frankyang.pre.api.AbstractRegistry;
import top.frankyang.pre.api.item.ItemSettings;


/**
 * 方块注册表。在某一个PythonCraft包的命名空间内注册方块。
 */
public class BlockRegistry extends AbstractRegistry {
    public BlockRegistry(String namespace) {
        super(namespace);
    }

    /**
     * 查询一个方块，不限于当前模组的命名空间。
     *
     * @param id 所查询方块的完整命名空间ID。
     * @return 查询到的方块。
     */
    public static Block lookup(Identifier id) {
        Block block = Registry.BLOCK.get(id);
        if (block == Blocks.AIR)
            throw new IllegalArgumentException("Cannot find the block for the identifier: " + id);
        return block;
    }

    /**
     * 注册一个方块，但是<strong>不注册</strong>其物品。这意味着这个方块将不会在创造模式物品栏中被找到，也不可能被合成。
     *
     * @param id    方块的命名空间ID。
     * @param block 所要注册的方块实例。
     */
    public void registerBlockOnly(String id, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(id), block);
    }

    /**
     * 注册一个方块，但是<strong>不注册</strong>其物品。这意味着这个方块将不会在创造模式物品栏中被找到，也不可能被合成。该方法仅适用于创建一个简单的方块。
     *
     * @param id            方块的命名空间ID。
     * @param blockSettings 所要注册的方块设置（Python字典，会自动解析设置并创建方块实例）。
     */
    public void registerBlockOnly(String id, PyDictionary blockSettings) {
        registerBlockOnly(id, new Block(BlockSettings.parse(blockSettings)));
    }

    /**
     * 注册一个方块，并同时注册其物品。
     *
     * @param id    方块及其物品的命名空间ID。
     * @param block 所要注册的方块实例。
     * @param item  所要注册的方块物品实例。
     */
    public void registerBlock(String id, Block block, BlockItem item) {
        Registry.register(Registry.BLOCK, getIdentifier(id), block);
        Registry.register(Registry.ITEM, getIdentifier(id), item);
    }

    /**
     * 注册一个方块，并同时注册其物品。该方法仅适用于创建一个简单的方块。
     *
     * @param id            方块及其物品的命名空间ID。
     * @param blockSettings 所要注册的方块设置（Python字典，会自动解析设置并创建方块实例）。
     * @param itemSettings  所要注册的方块物品设置（Python字典，会自动解析设置并创建方块物品实例）。
     */
    public void registerBlock(String id, PyDictionary blockSettings, PyDictionary itemSettings) {
        Block block;
        registerBlock(
            id,
            block = new Block(BlockSettings.parse(blockSettings)),
            new BlockItem(block, ItemSettings.parse(itemSettings))
        );
    }

    /**
     * 查询一个方块，不限于当前模组的命名空间。
     *
     * @param id 所查询方块的命名空间ID。
     * @return 查询到的物品。
     */
    public Block lookup(String id) {
        if (id.contains(":"))
            return lookup(new Identifier(id));
        return lookup(getIdentifier(id));
    }
}
