package ide.console.terminal;

import ide.console.Console;

public class CaretManagement extends Thread {

    public static final int BLINK_TIME = 500;
    private boolean work;
    private static Terminal terminal;
    private boolean showCaret;
    private long lastBlinkTime;

    public CaretManagement() {
        if (terminal == null) {
            terminal = Console.getTerminal();
        }
    }

    public void showCaret(boolean value) {
        synchronized (this) {
            showCaret = value;
        }
    }

    public boolean isShowCaret() {
        synchronized (this) {
            return showCaret;
        }
    }

    public void resetBlink() {
        synchronized (this) {
            showCaret = true;
            lastBlinkTime = System.currentTimeMillis();
        }
    }

    @Override
    public void start() {
        synchronized (this) {
            work = true;
            showCaret = true;
            setName("caret_management");
            super.start();
        }
    }

    @Override
    public void destroy() {
        synchronized (this) {
            showCaret = false;
            work = false;
        }
    }

    public void run() {
        long currentTime;
        while (work) {
            try {
                synchronized (this) {
                    wait(50);
                    if (terminal.getState() == Terminal.STATE_TEXT) {
                        currentTime = System.currentTimeMillis();
                        if (currentTime > lastBlinkTime + BLINK_TIME) {
                            showCaret = !showCaret;
                            terminal.updateCaret();
                            lastBlinkTime = currentTime;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Thread.yield();
        }
    }
}
