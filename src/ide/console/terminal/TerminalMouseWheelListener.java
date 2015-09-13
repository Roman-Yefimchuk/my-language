package ide.console.terminal;

import ide.console.terminal.Terminal;
import java.awt.event.*;
import javax.swing.*;

public class TerminalMouseWheelListener implements MouseWheelListener {

    private JScrollBar scrollBar;
    private Terminal display;

    public TerminalMouseWheelListener(JScrollBar scrollBar, Terminal display) {
        this.scrollBar = scrollBar;
        this.display = display;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        //System.out.println(e.getUnitsToScroll());
    }
}
