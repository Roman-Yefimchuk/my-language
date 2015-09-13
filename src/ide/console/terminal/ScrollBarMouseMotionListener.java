package ide.console.terminal;

import ide.console.terminal.Terminal;
import java.awt.event.*;
import javax.swing.*;

public class ScrollBarMouseMotionListener extends MouseMotionAdapter {

    private JScrollBar scrollBar;
    private Terminal display;

    public ScrollBarMouseMotionListener(JScrollBar scrollBar, Terminal display) {
        this.scrollBar = scrollBar;
        this.display = display;
    }

    public void mouseDragged(MouseEvent evt) {
        //System.out.println(scrollBar.getValue());
    }
}
