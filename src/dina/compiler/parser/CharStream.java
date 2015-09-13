package dina.compiler.parser;

import dina.*;
import java.util.*;

public class CharStream {

    public static final int LAST_COLUMN_INDEX = 0;
    public static final int LAST_LINE_INDEX = 1;
    public static final int LAST_POSITION_INDEX = 2;
    private int position = -1;
    private StringBuffer buffer;
    private int column = 0;
    private int line = 1;
    private Stack<int[]> returnBuffer;

    public CharStream(String text) {
        if (text.length() == 0) {
            throw new DinaException("Файл пуст", DinaException.COMMON_ERROR);
        }
        returnBuffer = new Stack<int[]>();
        buffer = new StringBuffer();
        buffer.append(text);
        buffer.append(' ');
    }

    public boolean hasMoreChars() {
        return position != buffer.length() - 1;
    }

    public char currentChar() {
        return buffer.charAt(position);
    }

    public char nextChar() {
        if (hasMoreChars()) {
            returnBuffer.push(new int[]{column, line, position});
            char currentChar = buffer.charAt(++position);
            if (DinaParser.isNewline(currentChar)) {
                newLine();
            } else {
                column++;
            }
            return currentChar;
        }
        throw new RuntimeException("Неожиданный конец файла");
    }

    public void returnChar() {
        if (!returnBuffer.empty()) {
            int[] lastLocation = returnBuffer.pop();
            column = lastLocation[LAST_COLUMN_INDEX];
            line = lastLocation[LAST_LINE_INDEX];
            position = lastLocation[LAST_POSITION_INDEX];
        } else {
            throw new RuntimeException("returnBuffer is empty");
        }
    }

    private void newLine() {
        column = 0;
        line++;
    }

    public int size() {
        return buffer.length();
    }

    public boolean empty() {
        return buffer.length() == 1;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public int getPosition() {
        return position;
    }

    public void clearReturnBuffer() {
        returnBuffer.clear();
    }
}
