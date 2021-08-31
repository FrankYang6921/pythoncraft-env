package top.frankyang.pre.api.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

/**
 * 包装类，包装原版类{@link AbstractBlock.Settings}。
 */
public class BlockSettings extends CastableImpl<FabricBlockSettings> {
    protected BlockSettings(FabricBlockSettings delegate) {
        super(delegate);
    }

    /**
     * 获取一个方块设置。
     *
     * @param material 方块的材质。
     */
    public static BlockSettings of(BlockMaterial material) {
        return new BlockSettings(FabricBlockSettings.of(material.cast()));
    }

    /**
     * 获取一个方块设置。
     *
     * @param builder 方块的材质生成器。
     */
    public static BlockSettings of(BlockMaterial.Builder builder) {
        return new BlockSettings(FabricBlockSettings.of(builder.build().cast()));
    }

    public BlockSettings noCollision() {
        casted.noCollision();
        return this;
    }

    public BlockSettings nonOpaque() {
        casted.nonOpaque();
        return this;
    }

    public BlockSettings slipperiness(float value) {
        casted.slipperiness(value);
        return this;
    }

    public BlockSettings velocityMultiplier(float velocityMultiplier) {
        casted.velocityMultiplier(velocityMultiplier);
        return this;
    }

    public BlockSettings jumpVelocityMultiplier(float jumpVelocityMultiplier) {
        casted.jumpVelocityMultiplier(jumpVelocityMultiplier);
        return this;
    }

    public BlockSettings sounds(BlockSounds sounds) {
        casted.sounds(sounds.cast());
        return this;
    }

    public BlockSettings sounds(BlockSounds.Builder builder) {
        casted.sounds(builder.build().cast());
        return this;
    }

    public BlockSettings strength(float hardness, float resistance) {
        casted.strength(hardness, resistance);
        return this;
    }

    public BlockSettings breakInstantly() {
        casted.breakInstantly();
        return this;
    }

    public BlockSettings strength(float strength) {
        casted.strength(strength);
        return this;
    }

    public BlockSettings ticksRandomly() {
        casted.ticksRandomly();
        return this;
    }

    public BlockSettings dynamicBounds() {
        casted.dynamicBounds();
        return this;
    }

    public BlockSettings dropsNothing() {
        casted.dropsNothing();
        return this;
    }

    public BlockSettings dropsLike(Block block) {
        casted.dropsLike(block);
        return this;
    }

    public BlockSettings air() {
        casted.air();
        return this;
    }

    public BlockSettings lightLevel(int lightLevel) {
        casted.lightLevel(lightLevel);
        return this;
    }

    public BlockSettings luminance(int luminance) {
        casted.luminance(luminance);
        return this;
    }

    public BlockSettings hardness(float hardness) {
        casted.hardness(hardness);
        return this;
    }

    public BlockSettings resistance(float resistance) {
        casted.resistance(resistance);
        return this;
    }

    public BlockSettings drops(Identifier dropTableId) {
        casted.drops(dropTableId);
        return this;
    }

    public BlockSettings requiresTool() {
        casted.requiresTool();
        return this;
    }

    public BlockSettings materialColor(MaterialColor color) {
        casted.materialColor(color);
        return this;
    }

    public BlockSettings materialColor(DyeColor color) {
        casted.materialColor(color);
        return this;
    }

    public BlockSettings collidable(boolean collidable) {
        casted.collidable(collidable);
        return this;
    }

    public BlockSettings breakByHand(boolean breakByHand) {
        casted.breakByHand(breakByHand);
        return this;
    }
}
