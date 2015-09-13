package dina;

import ide.*;
import javax.swing.text.*;
import dina.compiler.parser.*;

public class DinaException extends RuntimeException {

    public static final int PARSING_ERROR = 0;
    public static final int COMPILATION_ERROR = 1;
    public static final int ASSEMBLY_ERROR = 2;
    public static final int COMMON_ERROR = 3;
    public static final String[] errorMessage = {"Ошибка при парсинге", "Ошибка при компиляции", "Ошибка при сборке", "Ошибка"};
    private int errorId;
    private Object target;

    public DinaException(String message, int errorId) {
        super(message);
        this.errorId = errorId;
    }

    public DinaException(String message, Object target) {//PARSING_ERROR
        this(message, PARSING_ERROR);
        this.target = target;
    }

    public String getErrorName() {
        return errorMessage[errorId];
    }

    public int getErrorId() {
        return errorId;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        switch (errorId) {
            case PARSING_ERROR: {
                Location errorPosition = (Location) target;
                return "Ln: " + errorPosition.getLine() + ", Col: " + errorPosition.getColumn() + "\n" + message;
            }
            case COMPILATION_ERROR: {
                return /*ShrimpCompiler.currentToken().getLine() + ": " +*/ message;
            }
            default: {
                return message;
            }
        }
    }

//    public void showError() {
//        switch (errorId) {
//            case PARSING_ERROR: {
//                final int POSITION = 2;
//                int position = ((int[]) target)[POSITION];
//                selectError(position, position + 1);
//                break;
//            }
//            case COMPILATION_ERROR: {
//                Token token = ShrimpCompiler.currentToken();
//                int position = token.getPosition();
//                int from = position;
//                int to = position;
//                String source = IDE.getEditor().getText();
//                int length = source.length();
//                char ch = source.charAt(from);
//                while (ch != '\r' || ch != '\n' || from-- >= 0) {
//                    ch = source.charAt(from);
//                }
//                ch = source.charAt(to);
//                while (ch != '\r' || ch != '\n' || to++ < length) {
//                    ch = source.charAt(to);
//                }
//                selectError(from, to);
//                break;
//            }
//        }
//    }

    private static void selectError(int from, int to) {
        System.out.println("from - " + from + ", to - " + to);
        JTextComponent editor = IDE.getEditor();
        editor.requestFocus();
        editor.setCaretPosition(from);
        editor.select(from, to);
        editor.validate();
    }
}
