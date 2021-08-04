package top.frankyang.pre.util;

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

            Robot robot = new Robot();
            while (sequencer.isRunning()) {
                robot.mouseMove(
                    RANDOM.nextInt(width),
                    RANDOM.nextInt(height)
                );

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
