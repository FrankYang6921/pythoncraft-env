package top.frankyang.pre.api.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import top.frankyang.pre.api.misc.DelegatedCastable;

/**
 * 包装类，包装原版类{@link AbstractBlock.Settings}。
 */
public class BlockSettings extends DelegatedCastable<FabricBlockSettings> {
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
        delegate.noCollision();
        return this;
    }

    public BlockSettings nonOpaque() {
        delegate.nonOpaque();
        return this;
    }

    public BlockSettings slipperiness(float value) {
        delegate.slipperiness(value);
        return this;
    }

    public BlockSettings velocityMultiplier(float velocityMultiplier) {
        delegate.velocityMultiplier(velocityMultiplier);
        return this;
    }

    public BlockSettings jumpVelocityMultiplier(float jumpVelocityMultiplier) {
        delegate.jumpVelocityMultiplier(jumpVelocityMultiplier);
        return this;
    }

    public BlockSettings sounds(BlockSounds sounds) {
        delegate.sounds(sounds.cast());
        return this;
    }

    public BlockSettings sounds(BlockSounds.Builder builder) {
        delegate.sounds(builder.build().cast());
        return this;
    }

    public BlockSettings strength(float hardness, float resistance) {
        delegate.strength(hardness, resistance);
        return this;
    }

    public BlockSettings breakInstantly() {
        delegate.breakInstantly();
        return this;
    }

    public BlockSettings strength(float strength) {
        delegate.strength(strength);
        return this;
    }

    public BlockSettings ticksRandomly() {
        delegate.ticksRandomly();
        return this;
    }

    public BlockSettings dynamicBounds() {
        delegate.dynamicBounds();
        return this;
    }

    public BlockSettings dropsNothing() {
        delegate.dropsNothing();
        return this;
    }

    public BlockSettings dropsLike(Block block) {
        delegate.dropsLike(block);
        return this;
    }

    public BlockSettings air() {
        delegate.air();
        return this;
    }

    public BlockSettings lightLevel(int lightLevel) {
        delegate.lightLevel(lightLevel);
        return this;
    }

    public BlockSettings luminance(int luminance) {
        delegate.luminance(luminance);
        return this;
    }

    public BlockSettings hardness(float hardness) {
        delegate.hardness(hardness);
        return this;
    }

    public BlockSettings resistance(float resistance) {
        delegate.resistance(resistance);
        return this;
    }

    public BlockSettings drops(Identifier dropTableId) {
        delegate.drops(dropTableId);
        return this;
    }

    public BlockSettings requiresTool() {
        delegate.requiresTool();
        return this;
    }

    public BlockSettings materialColor(MaterialColor color) {
        delegate.materialColor(color);
        return this;
    }

    public BlockSettings materialColor(DyeColor color) {
        delegate.materialColor(color);
        return this;
    }

    public BlockSettings collidable(boolean collidable) {
        delegate.collidable(collidable);
        return this;
    }

    public BlockSettings breakByHand(boolean breakByHand) {
        delegate.breakByHand(breakByHand);
        return this;
    }
}
