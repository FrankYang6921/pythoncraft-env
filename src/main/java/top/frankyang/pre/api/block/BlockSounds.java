package top.frankyang.pre.api.block;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.python.core.PyDictionary;
import top.frankyang.pre.api.misc.DelegatedConvertable;
import top.frankyang.pre.api.util.TypedDictionary;

/**
 * 包装类，包装原版类<code>BlockSoundGroup</code>。
 */
public class BlockSounds extends DelegatedConvertable<BlockSoundGroup> {
    protected BlockSounds(BlockSoundGroup delegate) {
        super(delegate);
    }

    /**
     * 将一个Python字典解析为方块音效。
     *
     * @param dictionary 用于解析的Python字典。
     * @return 解析后的<code>BlockSoundGroup</code>实例。
     */
    public static BlockSounds of(@Nullable PyDictionary dictionary) {
        TypedDictionary dict = new TypedDictionary(dictionary);

        float volume = dict.getOrDefault("volume", Float.class, 1f);
        float pitch = dict.getOrDefault("pitch", Float.class, 1f);
        SoundEvent breakSound = dict.computeIfPresent("breakSound", String.class, s ->
            Registry.SOUND_EVENT.get(new Identifier(s)), SoundEvents.BLOCK_STONE_BREAK);
        SoundEvent placeSound = dict.computeIfPresent("placeSound", String.class, s ->
            Registry.SOUND_EVENT.get(new Identifier(s)), SoundEvents.BLOCK_STONE_PLACE);
        SoundEvent stepSound = dict.computeIfPresent("stepSound", String.class, s ->
            Registry.SOUND_EVENT.get(new Identifier(s)), SoundEvents.BLOCK_STONE_STEP);
        SoundEvent hitSound = dict.computeIfPresent("hitSound", String.class, s ->
            Registry.SOUND_EVENT.get(new Identifier(s)), SoundEvents.BLOCK_STONE_HIT);
        SoundEvent fallSound = dict.computeIfPresent("fallSound", String.class, s ->
            Registry.SOUND_EVENT.get(new Identifier(s)), SoundEvents.BLOCK_STONE_FALL);

        return new BlockSounds(
            new BlockSoundGroup(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound)
        );
    }

    public float getVolume() {
        return delegate.getVolume();
    }

    public float getPitch() {
        return delegate.getPitch();
    }

    public String getBreakSoundId() {
        return delegate.getBreakSound().getId().toString();
    }

    public String getStepSoundId() {
        return delegate.getStepSound().getId().toString();
    }

    public String getPlaceSoundId() {
        return delegate.getPlaceSound().getId().toString();
    }

    public String getHitSoundId() {
        return delegate.getHitSound().getId().toString();
    }

    public String getFallSound() {
        return delegate.getFallSound().getId().toString();
    }
}
