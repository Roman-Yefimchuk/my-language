package ide.console;

import ide.console.terminal.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import dina.compiler.*;
import dina.runtime.*;
import ide.*;

public class Console extends JFrame implements AbstractConsole {

    public static final String consoleWaitObject = "wait...";
    public static boolean firstConsoleStatr = true;
    private static Console instance;
    private static Terminal terminal;
    public static JPanel panel;
    public static JScrollBar scrollBar;
    public static Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static Dimension screenSize = toolkit.getScreenSize();
    public static int width = screenSize.width / 2;
    public static int height = screenSize.height / 2;
    public static TerminalKeyListener keyListener;
    public static TerminalMouseListener consoleMouseListener = new TerminalMouseListener();
    public static TerminalMouseWheelListener mouseWheelListener;
    public static ScrollBarMouseMotionListener mouseMotionListener;
    public static TerminalWindowListener consoleWindowListener = new TerminalWindowListener();

    private Console() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
        } catch (Exception Ex) {
        }
        if (firstConsoleStatr) {
            initComponents();
            firstConsoleStatr = !firstConsoleStatr;
        }
        setIconImage(toolkit.createImage(getClass().getResource("/core/logo.png")));
        init();
    }

    public static AbstractConsole getConsole() {
        return instance;
    }

    public static Terminal getTerminal() {
        return terminal;
    }

    private static void initComponents() {
        scrollBar = new JScrollBar();
        terminal = new Terminal(scrollBar);
        panel = new JPanel();
        keyListener = new TerminalKeyListener(terminal);
        mouseWheelListener = new TerminalMouseWheelListener(scrollBar, terminal);
        panel.addKeyListener(keyListener);
        scrollBar.addKeyListener(new ScrollBarKeyListener(scrollBar));
        scrollBar.addMouseWheelListener(mouseWheelListener);
        terminal.addKeyListener(keyListener);
        terminal.addMouseWheelListener(mouseWheelListener);
        terminal.addMouseMotionListener(consoleMouseListener);
        terminal.addMouseListener(consoleMouseListener);
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panel.setDoubleBuffered(true);
        scrollBar.setMaximum(0);
        mouseMotionListener = new ScrollBarMouseMotionListener(scrollBar, terminal);
        scrollBar.addMouseMotionListener(mouseMotionListener);
        terminal.init(width, height);
    }

    public void init() {
        addWindowListener(consoleWindowListener);
        addKeyListener(keyListener);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);
    }

    public void resetConsole(String title) {
        Terminal.screenMode = Terminal.NO_FULL_SCREEN_MODE;
        TerminalMouseListener.setScreenMode(Terminal.NO_FULL_SCREEN_MODE);
        setDefaultScreen();
        pack();
        setConsoleTitle(title);
        terminal.setTextState();
        terminal.cls();
        setLocation(new Point((screenSize.width - width) / 2, (screenSize.height - height) / 2));
        writeLn("Dina [version 1.0.0]");
        writeLn("©Roman Efimchuk, 2012");
        setVisible(true);
    }

    public static void showConsole(String title) {
        instance = new Console();
        instance.resetConsole(title);
        DinaVM.run(Output.executeFile.toByteArray(), DinaCompiler.getDebugger());
    }

    @Override
    public void paint(Graphics g) {
        synchronized (Terminal.lock) {
            super.paint(g);
        }
        synchronized (consoleWaitObject) {
            consoleWaitObject.notify();
        }
    }

    private void setFullScreen() {
        setSize(screenSize);
        setLocation(0, 0);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                addComponent(terminal, GroupLayout.DEFAULT_SIZE, screenSize.width, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                addComponent(terminal, GroupLayout.DEFAULT_SIZE, screenSize.height, Short.MAX_VALUE));
    }

    private void setDefaultScreen() {
        setSize(width, height);
        setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
        GroupLayout jPanel1Layout = new GroupLayout(panel);
        panel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                addGroup(jPanel1Layout.createSequentialGroup().
                addComponent(terminal, GroupLayout.PREFERRED_SIZE, width, GroupLayout.PREFERRED_SIZE).
                addGap(1, 1, 1).addComponent(scrollBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                addComponent(terminal, GroupLayout.DEFAULT_SIZE, height, Short.MAX_VALUE).
                addComponent(scrollBar, GroupLayout.DEFAULT_SIZE, height, Short.MAX_VALUE));
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
    }

    public void write(boolean x) {
        synchronized (Terminal.lock) {
            terminal.printLine(String.valueOf(x));
            terminal.repaint();
        }
    }

    public void write(char x) {
        synchronized (Terminal.lock) {
            terminal.printLine(String.valueOf(x));
            terminal.repaint();
        }
    }

    public void write(int x) {
        synchronized (Terminal.lock) {
            terminal.printLine(String.valueOf(x));
            terminal.repaint();
        }
    }

    public void write(float x) {
        synchronized (Terminal.lock) {
            terminal.printLine(String.valueOf(x));
            terminal.repaint();
        }
    }

    public void write(String x) {
        synchronized (Terminal.lock) {
            terminal.printLine(x);
            terminal.repaint();
        }
    }

    public void writeLn(boolean x) {
        synchronized (Terminal.lock) {
            terminal.printLine(String.valueOf(x));
            terminal.newLine();
            terminal.repaint();
        }
    }

    public void writeLn(char x) {
        synchronized (Terminal.lock) {
            terminal.printLine(String.valueOf(x));
            terminal.newLine();
            terminal.repaint();
        }
    }

    public void writeLn(int x) {
        synchronized (Terminal.lock) {
            terminal.printLine(String.valueOf(x));
            terminal.newLine();
            terminal.repaint();
        }
    }

    public void writeLn(float x) {
        synchronized (Terminal.lock) {
            terminal.printLine(String.valueOf(x));
            terminal.newLine();
            terminal.repaint();
        }
    }

    public void writeLn(String x) {
        synchronized (Terminal.lock) {
            terminal.printLine(x);
            terminal.newLine();
            terminal.repaint();
        }
    }

    public void writeLn() {
        synchronized (Terminal.lock) {
            terminal.newLine();
            terminal.repaint();
        }
    }

    public void cls() {
        synchronized (Terminal.lock) {
            terminal.cls();
            terminal.repaint();
        }
    }

    public int nextInteger() {
        return Integer.parseInt(terminal.nextLine());
    }

    public float nextFloat() {
        return Float.parseFloat(terminal.nextLine());
    }

    public String nextString() {
        return terminal.nextLine();
    }

    public int getKey() {
        return terminal.getKey();
    }

    public void setConsoleState(int state) {
        terminal.setState(state);
    }

    public int getConsoleState() {
        return terminal.getState();
    }

    public void setConsoleTitle(String title) {
        setTitle(title);
    }

    public void setFullScreenMode(boolean fullScreen) {
        if (Terminal.screenMode == (fullScreen ? Terminal.FULL_SCREEN_MODE : Terminal.NO_FULL_SCREEN_MODE)) {
            return;
        }
        repaint();
        synchronized (consoleWaitObject) {
            try {
                consoleWaitObject.wait();
            } catch (Exception ex) {
            }
        }
        synchronized (Terminal.lock) {
            removeNotify();
            getContentPane().removeAll();
            if (fullScreen) {
                Terminal.screenMode = Terminal.FULL_SCREEN_MODE;
                TerminalMouseListener.setScreenMode(Terminal.FULL_SCREEN_MODE);
                setFullScreen();
            } else {
                Terminal.screenMode = Terminal.NO_FULL_SCREEN_MODE;
                TerminalMouseListener.setScreenMode(Terminal.NO_FULL_SCREEN_MODE);
                setDefaultScreen();
            }
            setUndecorated(fullScreen);
            pack();
            terminal.show();
        }
    }

    public void halt() {
        setVisible(false);
        DinaVM.haltThread();
        IDE.installEnabled(true);
        terminal.hide();
    }

    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }
}
