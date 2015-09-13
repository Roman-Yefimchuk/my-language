package ide.console.terminal;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import dina.*;
import dina.library.libs.*;

public class Terminal extends JComponent {

    public static final int FULL_SCREEN_MODE = 2;
    public static final int NO_FULL_SCREEN_MODE = 1;
    public static final Object lock = new JButton().getTreeLock();
    public static final Font DEFAULT_FONT = new Font("Consolas", 0, 14);
    public static final TerminalContext TEXT_CONTEXT = new TerminalContext(DEFAULT_FONT);
    public static final TerminalContext VGA_CONTEXT = new TerminalContext(DEFAULT_FONT);
    public static final int MOUSE_UP = GlobalConstantsValue.MOUSE_UP;
    public static final int MOUSE_DOWN = GlobalConstantsValue.MOUSE_DOWN;
    public static final int MOUSE_MOVE = GlobalConstantsValue.MOUSE_MOVE;
    public static final int MOUSE_DRAG = GlobalConstantsValue.MOUSE_DRAG;
    //
    public static final int STATE_TEXT = GlobalConstantsValue.STATE_TEXT;
    public static final int STATE_VGA = GlobalConstantsValue.STATE_VGA;
    //
    public static final int READ = 0;
    public static final int WRITE = 1;
    public static final int GET_KEY = 2;
    //
    public static TerminalContext currentContext = TEXT_CONTEXT;
    private int mode = WRITE;
    public static int state = STATE_TEXT;
    private static JScrollBar scrollBar;
    private static BufferedImage image;
    private static Graphics2D graphics;
    private static Color backgroundColor;
    private static Color textColor;
    private static Font consoleFont;
    private static int left;
    private static int top;
    private static int width;
    private static int height;
    private static int offset;
    private static int fontHeight;
    private static int maxLines;
    private static int currentLine;
    private static int caretPosition;
    private static int caretIndex;
    private static ArrayList<StringBuffer> lines = new ArrayList<StringBuffer>();
    private static StringBuffer line;
    public static CaretManagement caretManagement;
    private static int imageWidth;
    private static int imageHeight;
    //
    public static int CURRENT_X;
    public static int CURRENT_Y;
    public static int CURRENT_MOUSE_ACTION;
    //
    public static int ACTION_KEY;
    public static int screenMode = NO_FULL_SCREEN_MODE;

    public Terminal(JScrollBar scrollBar) {
        Terminal.scrollBar = scrollBar;
        setDoubleBuffered(true);
        RepaintManager.currentManager(this).setDoubleBufferingEnabled(true);
    }

    public Graphics2D getTerminalGraphics() {
        return graphics;
    }

    @Override
    public void show() {
        synchronized (this) {
            setBackground(backgroundColor);
            caretManagement = new CaretManagement();
            caretManagement.start();
            resetBlink();
            updateCaret();
        }
    }

    @Override
    public void hide() {
        synchronized (this) {
            if (caretManagement != null) {
                caretManagement.destroy();
                caretManagement = null;
                updateCaret();
            }
        }
    }

    public void init(int displayWidth, int displayHeight) {
        backgroundColor = Color.black;
        textColor = Color.white;
        width = displayWidth;
        height = displayHeight;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        consoleFont = DEFAULT_FONT;
        graphics.setFont(currentContext.font);
        fontHeight = graphics.getFontMetrics().getHeight();
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();
        //System.out.println(graphics.getFontMetrics().getAscent());
    }

    public void setTextState() {
        state = STATE_TEXT;
        currentContext = TEXT_CONTEXT;
        consoleFont = currentContext.font;
        graphics.setFont(consoleFont);
        fontHeight = graphics.getFontMetrics().getHeight();
    }

    public void setVGAState() {
        state = STATE_VGA;
        currentContext = VGA_CONTEXT;
        graphics.setFont(currentContext.font);
        GraphicsLibrary.updateLibrary();
    }

    @Override
    protected void paintChildren(Graphics g) {
        //empty
    }

    @Override
    public void paint(Graphics g) {
        paintComponent(g);
    }

    @Override
    public void paintComponent(Graphics g) {
        synchronized (lock) {
            if (state == STATE_TEXT) {
                clearBackGround();
                updateConsole();
            }
            g.drawImage(image, 0, 0, imageWidth * screenMode, imageHeight * screenMode, null);
        }
    }

    private void updateConsole() {
        graphics.setColor(textColor);
        for (int index = offset; index < offset + maxLines; index++) {
            graphics.drawString(lines.get(index).toString(), 0, ((top + (index * fontHeight)) - (offset * fontHeight)) + graphics.getFontMetrics().getAscent());
        }
        drawCaret();
    }

    public void printLine(String text) {
        char[] line = text.toCharArray();
        for (int charIndex = 0; charIndex < line.length; charIndex++) {
            writeChar(line[charIndex]);
        }
        resetBlink();
    }

    private void writeChar(char c) {
        if (c == '\n') {
            newLine();
        } else {
            int charWidth = graphics.getFontMetrics().charWidth(c);
            if (caretPosition + charWidth > width - 1) {
                newLine();
            }
            caretPosition += charWidth;
            line = lines.get(currentLine);
            line.insert(caretIndex, c);
            caretIndex++;
        }
    }

    public void newLine() {
        StringBuffer sBuf = lines.get(currentLine);
        char[] afterCaret = new char[sBuf.length() - caretIndex];
        incLineIndex();
        if (afterCaret.length != 0) {
            sBuf.getChars(caretIndex, sBuf.length(), afterCaret, 0);
            sBuf.delete(caretIndex, sBuf.length());
            lines.add(currentLine, new StringBuffer(new String(afterCaret)));
        } else {
            lines.add(currentLine, new StringBuffer());
        }
        afterCaret = null;
        caretIndex = 0;
        caretPosition = 0;
        updateMaxLinesCount();
        scrollBar.setMaximum(lines.size() - 1);
        scrollBar.setValue(currentLine);
    }

    private void moveUp() {
        if (currentLine != 0 && currentLine != lineLimit) {
            decLineIndex();
            shiftCaret(lines.get(currentLine).toString());
            resetBlink();
        }
    }

    private void moveDown() {
        if (currentLine != lines.size() - 1) {
            incLineIndex();
            shiftCaret(lines.get(currentLine).toString());
            resetBlink();
        }
    }

    private void moveCaret(int arrow) {
        StringBuffer sBuf = lines.get(currentLine);
        if (arrow == 1) {
            if (caretIndex == sBuf.length()) {
                if (currentLine != lines.size() - 1) {
                    incLineIndex();
                    caretIndex = 0;
                    caretPosition = 0;
                    resetBlink();
                }
            } else {
                int charWidth = graphics.getFontMetrics().charWidth(sBuf.charAt(caretIndex));
                caretPosition += charWidth;
                caretIndex++;
                resetBlink();
            }
        } else {
            if (caretIndex > 0) {
                if (caretIndex == caretLimit && currentLine == lineLimit) {
                    return;
                }
                int charWidth = graphics.getFontMetrics().charWidth(sBuf.charAt(caretIndex - 1));
                caretPosition -= charWidth;
                caretIndex--;
                resetBlink();
            } else {
                if (currentLine != 0 && currentLine != lineLimit && caretIndex != caretLimit) {
                    decLineIndex();
                    sBuf = lines.get(currentLine);
                    caretPosition = graphics.getFontMetrics().stringWidth(sBuf.toString());
                    caretIndex = sBuf.length();
                    resetBlink();
                }
            }
        }
    }

    private void deleteChar() {
        if (caretIndex == caretLimit && currentLine == lineLimit) {
            return;
        }
        if (currentLine != 0) {
            if (caretIndex != 0) {
                removeChar();
            } else {
                if (currentLine != lineLimit) {
                    int lastLineIndex = currentLine;
                    moveCaret(-1);
                    if (currentLine != lastLineIndex) {
                        StringBuffer sBuf = lines.get(currentLine);
                        sBuf.append(lines.get(lastLineIndex).toString());
                        lines.remove(lastLineIndex);
                        updateMaxLinesCount();
                        scrollBar.setMaximum(lines.size() - 1);
                        scrollBar.setValue(currentLine);
                        if (offset > 0) {
                            offset--;
                        }
                    } else {
                        lines.get(currentLine).deleteCharAt(caretIndex);
                    }
                    resetBlink();
                }
            }
        } else {
            if (caretIndex != 0) {
                removeChar();
            }
        }
    }

    private void removeChar() {
        moveCaret(-1);
        lines.get(currentLine).deleteCharAt(caretIndex);
        resetBlink();
    }

    private void incLineIndex() {
        if (((top + (currentLine + 1) * fontHeight) - (offset * fontHeight)) + fontHeight > top + height) {
            offset++;
            currentLine++;
        } else {
            currentLine++;
        }
        scrollBar.setValue(currentLine);
    }

    private void decLineIndex() {
        if (((top + currentLine * fontHeight) - (offset * fontHeight)) - fontHeight < top) {
            offset--;
            currentLine--;
        } else {
            currentLine--;
        }
        scrollBar.setValue(currentLine);
    }

    private void shiftCaret(String s) {
        if (s.length() <= caretIndex) {
            caretIndex = s.length();
            caretPosition = graphics.getFontMetrics().stringWidth(s);
        } else {
            caretPosition = graphics.getFontMetrics().charsWidth(s.toCharArray(), 0, caretIndex);
        }
    }

    private void updateMaxLinesCount() {
        int mod = height / fontHeight;
        if (lines.size() <= mod) {
            maxLines = lines.size();
        } else {
            maxLines = mod;
        }
    }

    private void drawCaret() {
        if (isShowCaret()) {
            graphics.setColor(Color.red);
            graphics.fillRect((left + caretPosition), top + ((currentLine - offset) * fontHeight), 1, fontHeight);
        }
    }

    public void cls() {
        synchronized (lock) {
            lines.clear();
            lines.add(new StringBuffer());
            updateMaxLinesCount();
            currentLine = 0;
            caretIndex = 0;
            offset = 0;
            scrollBar.setMaximum(0);
            scrollBar.setValue(0);
            resetBlink();
        }
    }
    public static final String waitObject = ":)";
    private StringBuffer inputLine;
    private int lineLimit;
    private int caretLimit;
    private int inputKey;

    public void setMoveLimit() {
        lineLimit = currentLine;
        caretLimit = caretIndex;
    }

    private void getInputLine() {
        inputLine = new StringBuffer();
        int fromLine = lineLimit;
        int toLine = lines.size() - 1;
        if (fromLine == toLine) {
            String line = lines.get(fromLine).toString();
            inputLine.append(line.substring(caretLimit, line.length()));
        } else {
            String line = lines.get(fromLine).toString();
            inputLine.append(line.substring(caretLimit, line.length()));
            for (int lineIndex = fromLine + 1; lineIndex <= toLine; lineIndex++) {
                inputLine.append(lines.get(lineIndex));
            }
        }
    }

    public void readKey(KeyEvent e) {
        synchronized (lock) {
            inputKey = e.getKeyCode();
            synchronized (waitObject) {
                waitObject.notify();
            }
            try {
                wait(10);
            } catch (Exception ex) {
            }
        }
    }

    public void readLine(KeyEvent e) {
        synchronized (lock) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_LEFT: {
                    moveCaret(-1);
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    moveCaret(1);
                    break;
                }
                case KeyEvent.VK_BACK_SPACE: {
                    deleteChar();
                    break;
                }
                case KeyEvent.VK_ENTER: {
                    getInputLine();
                    currentLine = lines.size() - 1;
                    caretIndex = lines.get(lines.size() - 1).length();
                    newLine();
                    synchronized (waitObject) {
                        waitObject.notify();
                    }
                    try {
                        wait(10);
                    } catch (Exception ex) {
                    }
                    resetBlink();
                    break;
                }
                default: {
                    int inputSymbol = Keyboard.getChar(e);
                    if (inputSymbol != -1) {
                        writeChar((char) inputSymbol);
                        resetBlink();
                    }
                    break;
                }
            }
        }
    }

    public String nextLine() {
        setMode(READ);
        setMoveLimit();
        synchronized (waitObject) {
            try {
                waitObject.wait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        setMode(WRITE);
        return inputLine.toString();
    }

    public void setMode(int mode) {
        synchronized (lock) {
            this.mode = mode;
        }
    }

    public int getMode() {
        return mode;
    }

    public int getKey() {
        setMode(GET_KEY);
        synchronized (waitObject) {
            try {
                waitObject.wait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        setMode(WRITE);
        return inputKey;
    }

    public void setState(int state) {
        synchronized (lock) {
            if (state != STATE_TEXT && state != STATE_VGA) {
                throw new IllegalArgumentException("Console state must be one of: STATE_TEXT or STATE_VGA");
            }
            if (Terminal.state != state) {
                switch (state) {
                    case STATE_TEXT: {
                        setTextState();
                        break;
                    }
                    case STATE_VGA: {
                        setVGAState();
                        clearBackGround();
                        break;
                    }
                }
                repaint();
            }
        }
    }

    private void clearBackGround() {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
    }

    public synchronized int getState() {
        return state;
    }

    public void updateCaret() {
        synchronized (lock) {
            repaint((left + caretPosition) * screenMode, (top + ((currentLine - offset) * fontHeight)) * screenMode, 1 * screenMode, fontHeight * screenMode);
        }
    }

    public boolean isShowCaret() {
        if (caretManagement != null) {
            return caretManagement.isShowCaret();
        }
        return false;
    }

    public void resetBlink() {
        if (caretManagement != null) {
            caretManagement.resetBlink();
        }
    }
}
