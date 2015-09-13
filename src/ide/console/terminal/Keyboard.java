package ide.console.terminal;

import java.awt.event.*;
import java.util.*;

public class Keyboard {

    private static Hashtable keyTable = new Hashtable();
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    private static boolean lowerCase = true;
    public static final int UPPER = 0;
    public static final int LOWER = 1;
    public static final int CAPS_LOCK = 2;

    static {
        addKey("1", '1', '!', FALSE);
        addKey("2", '2', '@', FALSE);
        addKey("3", '3', '#', FALSE);
        addKey("4", '4', '$', FALSE);
        addKey("5", '5', '%', FALSE);
        addKey("6", '6', '^', FALSE);
        addKey("7", '7', '&', FALSE);
        addKey("8", '8', '*', FALSE);
        addKey("9", '9', '(', FALSE);
        addKey("0", '0', ')', FALSE);
        addKey("Minus", '-', '_', FALSE);
        addKey("Equals", '=', '+', FALSE);
        addKey("Back Quote", '`', '~', FALSE);
        addKey("Space", ' ', ' ', FALSE);
        addKey("Q", 'Q', 'q', TRUE);
        addKey("W", 'W', 'w', TRUE);
        addKey("E", 'E', 'e', TRUE);
        addKey("R", 'R', 'r', TRUE);
        addKey("T", 'T', 't', TRUE);
        addKey("Y", 'Y', 'y', TRUE);
        addKey("U", 'U', 'u', TRUE);
        addKey("I", 'I', 'i', TRUE);
        addKey("O", 'O', 'o', TRUE);
        addKey("P", 'P', 'p', TRUE);
        addKey("A", 'A', 'a', TRUE);
        addKey("S", 'S', 's', TRUE);
        addKey("D", 'D', 'd', TRUE);
        addKey("F", 'F', 'f', TRUE);
        addKey("G", 'G', 'g', TRUE);
        addKey("H", 'H', 'h', TRUE);
        addKey("J", 'J', 'j', TRUE);
        addKey("K", 'K', 'k', TRUE);
        addKey("L", 'L', 'l', TRUE);
        addKey("Z", 'Z', 'z', TRUE);
        addKey("X", 'X', 'x', TRUE);
        addKey("C", 'C', 'c', TRUE);
        addKey("V", 'V', 'v', TRUE);
        addKey("B", 'B', 'b', TRUE);
        addKey("N", 'N', 'n', TRUE);
        addKey("M", 'M', 'm', TRUE);
        addKey("Open Bracket", '[', '{', FALSE);
        addKey("Close Bracket", ']', '}', FALSE);
        addKey("Back Slash", '\\', '|', FALSE);
        addKey("Semicolon", ';', ':', FALSE);
        addKey("Quote", '\'', '"', FALSE);
        addKey("Comma", ',', '<', FALSE);
        addKey("Period", '.', '>', FALSE);
        addKey("Slash", '/', '?', FALSE);
    }

    private static void addKey(String keyName, int upper, int lower, int capsLock) {
        keyTable.put(keyName, new int[]{upper, lower, capsLock});
    }

    private static int[] getKey(String keyName) {
        return (int[]) keyTable.get(keyName);
    }

    public static int getChar(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int symbol = -1;
        if (keyCode == KeyEvent.VK_CAPS_LOCK) {
            lowerCase = !lowerCase;
            return symbol;
        }
        int[] keyMap = getKey(KeyEvent.getKeyText(keyCode));
        if (keyMap != null) {
            boolean isShiftDown = e.isShiftDown();
            if (lowerCase && keyMap[CAPS_LOCK] == TRUE) {
                symbol = keyMap[LOWER];
            } else {
                symbol = keyMap[UPPER];
            }
            if (isShiftDown) {
                if (symbol == keyMap[LOWER]) {
                    symbol = keyMap[UPPER];
                } else {
                    symbol = keyMap[LOWER];
                }
            }
        }
        return symbol;
    }
}
