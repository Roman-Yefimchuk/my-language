package ide.console.terminal;

import ide.console.Console;
import java.awt.event.*;

public class TerminalWindowListener implements WindowListener {

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        windowClosed(e);
    }

    public void windowClosed(WindowEvent e) {
        Console.getConsole().halt();
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
        Console.getTerminal().show();
    }

    public void windowDeactivated(WindowEvent e) {
        Console.getTerminal().hide();
    }
}
