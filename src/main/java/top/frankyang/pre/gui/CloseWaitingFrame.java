package top.frankyang.pre.gui;

import top.frankyang.pre.gui.controls.MineFrame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class CloseWaitingFrame extends MineFrame {
    private final Object windowCloseLock = new Object();

    public CloseWaitingFrame() {
        addWindowListener(new MyWindowListener());
    }

    protected void closeWindow() {
        synchronized (windowCloseLock) {
            windowCloseLock.notifyAll();
        }
        dispose();
    }

    protected void waitForClose() {
        synchronized (windowCloseLock) {
            try {
                windowCloseLock.wait();
            } catch (InterruptedException e) {
                closeWindow();
            }
        }
    }

    private class MyWindowListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent windowEvent) {
        }

        @Override
        public void windowClosing(WindowEvent windowEvent) {
            synchronized (windowCloseLock) {
                windowCloseLock.notifyAll();
            }
        }

        @Override
        public void windowClosed(WindowEvent windowEvent) {
        }

        @Override
        public void windowIconified(WindowEvent windowEvent) {
        }

        @Override
        public void windowDeiconified(WindowEvent windowEvent) {
        }

        @Override
        public void windowActivated(WindowEvent windowEvent) {
        }

        @Override
        public void windowDeactivated(WindowEvent windowEvent) {
        }
    }
}
