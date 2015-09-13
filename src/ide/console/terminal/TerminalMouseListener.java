package ide.console.terminal;

import ide.console.terminal.Terminal;
import java.awt.event.*;

public class TerminalMouseListener implements MouseListener, MouseMotionListener {

    private static int screenMode = Terminal.NO_FULL_SCREEN_MODE;

    public static void setScreenMode(int screenMode) {
        TerminalMouseListener.screenMode = screenMode;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        Terminal.CURRENT_X = e.getX() / screenMode;
        Terminal.CURRENT_Y = e.getY() / screenMode;
        Terminal.CURRENT_MOUSE_ACTION = Terminal.MOUSE_DOWN;
    }

    public void mouseReleased(MouseEvent e) {
        Terminal.CURRENT_X = e.getX() / screenMode;
        Terminal.CURRENT_Y = e.getY() / screenMode;
        Terminal.CURRENT_MOUSE_ACTION = Terminal.MOUSE_UP;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        Terminal.CURRENT_X = e.getX() / screenMode;
        Terminal.CURRENT_Y = e.getY() / screenMode;
        Terminal.CURRENT_MOUSE_ACTION = Terminal.MOUSE_DRAG;
    }

    public void mouseMoved(MouseEvent e) {
        Terminal.CURRENT_X = e.getX() / screenMode;
        Terminal.CURRENT_Y = e.getY() / screenMode;
        Terminal.CURRENT_MOUSE_ACTION = Terminal.MOUSE_MOVE;
    }
}
