package top.frankyang.pre.util;

import net.minecraft.client.util.Window;
import top.frankyang.pre.api.Minecraft;
import top.frankyang.pre.mixin.ClientWindowAccessor;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.Callable;

public final class EasterEggs {
    private static final Random RANDOM = new Random();
    private static Sequencer sequencer;

    private EasterEggs() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void playRedZone() {
        if (RANDOM.nextDouble() < 0.9 || sequencer != null && sequencer.isRunning())
            return;
        try {
            playRedZone0();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private static void playRedZone0() throws Throwable {
        Sequence sequence = MidiSystem.getSequence(
            EasterEggs.class.getResource("/assets/pre/easter_egg.mid")
        );
        sequencer = MidiSystem.getSequencer();
        sequencer.setSequence(sequence);
        sequencer.open();
        sequencer.start();

        Callable<?> task = () -> {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) size.getWidth();
            int height = (int) size.getHeight();

            Thread.sleep(4500);

            ClientWindowAccessor windowAccessor = ClientWindowAccessor.class.cast(Minecraft.getClient().getWindow());
            Window window = Minecraft.getClient().getWindow();

            int vx = 20, vy = 20;
            int boundX = width - window.getWidth();
            int boundY = height - window.getHeight();
            int cx = RANDOM.nextInt(boundX), cy = RANDOM.nextInt(boundY);

            while (sequencer.isRunning()) {
                Robot robot = new Robot();
                robot.mouseMove(
                    RANDOM.nextInt(width),
                    RANDOM.nextInt(height)
                );
                if (cx + vx < 0 || cx + vx > boundX) {
                    vx *= -1;
                }
                if (cy + vy < 0 || cy + vy > boundY) {
                    vy *= -1;
                }
                cx += vx;
                cy += vy;
                if (window.isFullscreen()) {
                    window.toggleFullscreen();
                }
                //noinspection ConstantConditions
                windowAccessor.setWindowedX(cx);
                windowAccessor.setWindowedY(cy);
                windowAccessor.callUpdateWindowRegion();
                //noinspection BusyWait
                Thread.sleep(17);
            }

            JOptionPane.showMessageDialog(
                null,
                "请勿禁用API。",
                "PythonCraft Runtime Environment",
                JOptionPane.WARNING_MESSAGE
            );

            return null;
        };

        Thread thread = new Thread(() -> {
            try {
                task.call();
            } catch (Exception e) {
                // Stop the thread at once!
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
