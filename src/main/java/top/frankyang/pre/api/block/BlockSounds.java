package top.frankyang.pre.api.block;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import top.frankyang.pre.api.misc.conversion.CastableImpl;

/**
 * 包装类，包装原版类{@link BlockSoundGroup}。
 */
public class BlockSounds extends CastableImpl<BlockSoundGroup> {
    protected BlockSounds(BlockSoundGroup delegate) {
        super(delegate);
    }

    public static Builder of() {
        return new Builder();
    }

    private static BlockSounds of(Builder builder) {
        return new BlockSounds(
            new BlockSoundGroup(
                builder.volume,
                builder.pitch,
                builder.breakSound,
                builder.placeSound,
                builder.stepSound,
                builder.hitSound,
                builder.fallSound
            )
        );
    }

    public float getVolume() {
        return casted.getVolume();
    }

    public float getPitch() {
        return casted.getPitch();
    }

    public String getBreakSoundId() {
        return casted.getBreakSound().getId().toString();
    }

    public String getStepSoundId() {
        return casted.getStepSound().getId().toString();
    }

    public String getPlaceSoundId() {
        return casted.getPlaceSound().getId().toString();
    }

    public String getHitSoundId() {
        return casted.getHitSound().getId().toString();
    }

    public String getFallSound() {
        return casted.getFallSound().getId().toString();
    }

    public static class Builder {
        private float volume = 1f;
        private float pitch = 1f;
        private SoundEvent breakSound = SoundEvents.BLOCK_STONE_BREAK;
        private SoundEvent placeSound = SoundEvents.BLOCK_STONE_PLACE;
        private SoundEvent stepSound = SoundEvents.BLOCK_STONE_STEP;
        private SoundEvent hitSound = SoundEvents.BLOCK_STONE_HIT;
        private SoundEvent fallSound = SoundEvents.BLOCK_STONE_FALL;

        public void volume(float volume) {
            this.volume = volume;
        }

        public void pitch(float pitch) {
            this.pitch = MathHelper.clamp(pitch, .5f, 2);
        }

        public void breakSoundId(String s) {
            breakSound = Registry.SOUND_EVENT.get(new Identifier(s));
        }

        public void placeSoundId(String s) {
            placeSound = Registry.SOUND_EVENT.get(new Identifier(s));
        }

        public void stepSoundId(String s) {
            stepSound = Registry.SOUND_EVENT.get(new Identifier(s));
        }

        public void hitSoundId(String s) {
            hitSound = Registry.SOUND_EVENT.get(new Identifier(s));
        }

        public void fallSoundId(String s) {
            fallSound = Registry.SOUND_EVENT.get(new Identifier(s));
        }

        public BlockSounds build() {
            return BlockSounds.of(this);
        }
    }
}
