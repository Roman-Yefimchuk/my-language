package ide.console.terminal;

import ide.console.terminal.Terminal;
import java.awt.event.*;

public class TerminalKeyListener implements KeyListener {

    private static Terminal display;

    public TerminalKeyListener(Terminal display) {
        TerminalKeyListener.display = display;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (Terminal.state == Terminal.STATE_TEXT) {
            switch (display.getMode()) {
                case Terminal.READ: {
                    display.readLine(e);
                    display.repaint();
                    break;
                }
                case Terminal.GET_KEY: {
                    display.readKey(e);
                    display.repaint();
                    break;
                }
            }
        } else {
            Terminal.ACTION_KEY = e.getKeyCode();
        }
    }

    public void keyReleased(KeyEvent e) {
        Terminal.ACTION_KEY = 0;
    }
}
