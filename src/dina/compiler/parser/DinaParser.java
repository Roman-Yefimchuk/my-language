package dina.compiler.parser;

import java.util.*;
import dina.*;

public class DinaParser {

    public static final int UNCLOSED_COMMENT = 0;
    public static final int UNCLOSED_STRING_LITERAL = 1;
    public static final int UNCLOSED_SYMBOL_LITERAL = 2;
    public static final int INVALID_STRING_LITERAL = 3;
    public static final int INVALID_SYMBOL_LITERAL = 4;
    public static final int EMPTY_SYMBOL_LITERAL = 5;
    public static final int INVALID_DECIMAL_LITERAL = 6;
    public static final int INVALID_ESCAPE_SYMBOL_LITERAL = 7;
    public static final int ERROR = -1;
    public static final int ABSENT = 0;
    public static final int PRESENT = 1;
    public static final Hashtable operatorsTable = new Hashtable();
    public static final Hashtable keyWordsTable = new Hashtable();
    public static final Hashtable separatorsTable = new Hashtable();
    public static final String[] messages = {
        "Незакрытый комментарий",
        "Незакрытый строковой литерал",
        "Незакрытый символьный литерал",
        "Строковой литерал задан некорректно",
        "Символьный литерал задан некорректно",
        "Пустой символьный литерал",
        "Дробный литерал задана некорректно",
        "Некорректный бегущий символьный литерал"
    };
    private CharStream stream;

    static {
        for (int operatorIndex = 0; operatorIndex < ParserConstants.operators.length; operatorIndex++) {
            operatorsTable.put(ParserConstants.operators[operatorIndex], operatorIndex);
        }
        for (int keywordIndex = 0; keywordIndex < ParserConstants.keyWords.length; keywordIndex++) {
            keyWordsTable.put(ParserConstants.keyWords[keywordIndex], keywordIndex);
        }
        for (int separatorIndex = 0; separatorIndex < ParserConstants.separators.length; separatorIndex++) {
            separatorsTable.put(ParserConstants.separators[separatorIndex], separatorIndex);
        }
    }

    private static boolean russia_encoding(char ch) {
        return (ch >= 'А' && ch <= 'Я') || (ch >= 'а' && ch <= 'я');
    }

    private static boolean english_encoding(char ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
    }

    private static boolean isWord(char ch) {
        return russia_encoding(ch) || english_encoding(ch) || (ch == '_');
    }

    private static boolean isHexDigit(char ch) {
        return isDecDigit(ch) || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f');
    }

    private static boolean isDecDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static boolean isString(char ch) {
        return (ch >= '\u0000' && ch <= '!') || (ch >= '#' && ch <= '[') || (ch >= ']' && ch <= '\uFFFF');
    }

    private static boolean isSymbol(char ch) {
        return (ch >= '\u0000' && ch <= '&') || (ch >= '(' && ch <= '[') || (ch >= ']' && ch <= '\uFFFF');
    }

    private static boolean isWhiteSpace(char ch) {
        return (ch >= '\t' && ch <= '\n') || (ch == '\r') || (ch == ' ');
    }

    public static boolean isNewline(char ch) {
        return ch == '\n' || ch == '\r';
    }

    private static void error(int errorId, Location location) {
        throw new DinaException(messages[errorId], location);
    }

    public static int getOperator(String operatorName) {
        Integer index = (Integer) operatorsTable.get(operatorName);
        return index != null ? index.intValue() : -1;
    }

    public static int getKeyWord(String keyWordName) {
        Integer index = (Integer) keyWordsTable.get(keyWordName);
        return index != null ? index.intValue() : -1;
    }

    public static int getSeparator(String separatorName) {
        Integer index = (Integer) separatorsTable.get(separatorName);
        return index != null ? index.intValue() : -1;
    }

    private static Token getToken(DinaParser parser) {
        CharStream stream = parser.stream;
        while (true) {
            if (stream.hasMoreChars()) {
                StringBuffer sb = new StringBuffer();
                char ch = stream.nextChar();
                Location location = new Location(stream.getColumn(), stream.getLine(), stream.getPosition());
                stream.returnChar();
                if (appendNumber(sb, stream) == PRESENT) {
                    switch (appendExponent(sb, stream)) {
                        case PRESENT: {
                            return new Token(sb.toString(), Token.ttFloatConstant, location);
                        }
                        case ERROR: {
                            error(INVALID_DECIMAL_LITERAL, location);
                        }
                    }
                    ch = stream.nextChar();
                    if (ch == '.') {
                        sb.append(ch);
                        switch (appendExponent(sb, stream)) {
                            case PRESENT: {
                                return new Token(sb.toString(), Token.ttFloatConstant, location);
                            }
                            case ERROR: {
                                error(INVALID_DECIMAL_LITERAL, location);
                            }
                        }
                        if (appendNumber(sb, stream) == PRESENT) {
                            if (appendExponent(sb, stream) == ERROR) {
                                error(INVALID_DECIMAL_LITERAL, location);
                            }
                        }
                        return new Token(sb.toString(), Token.ttFloatConstant, location);
                    } else {
                        stream.returnChar();
                        return new Token(sb.toString(), Token.ttIntConstant, location);
                    }
                } else {
                    ch = stream.nextChar();
                }
                if (ch == '/') {
                    sb.append(ch);
                    ch = stream.nextChar();
                    switch (ch) {
                        case '=': {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        case '/': {
                            do {
                                ch = stream.nextChar();
                            } while (stream.hasMoreChars() && (!isNewline(ch)));
                            continue;
                        }
                        case '*': {
                            loop:
                            do {
                                if (!stream.hasMoreChars()) {
                                    error(UNCLOSED_COMMENT, location);
                                }
                                ch = stream.nextChar();
                                if (ch == '*') {
                                    if (stream.hasMoreChars()) {
                                        ch = stream.nextChar();
                                        if (ch == '/') {
                                            break loop;
                                        }
                                    } else {
                                        error(UNCLOSED_COMMENT, location);
                                    }
                                }
                            } while (true);
                            continue;
                        }
                    }
                    stream.returnChar();
                    return new Token(sb.toString(), Token.ttOperator, location);
                } else if (isWhiteSpace(ch)) {
                    continue;
                } else if (isWord(ch)) {
                    while (isWord(ch) || isDecDigit(ch)) {
                        sb.append(ch);
                        if (stream.hasMoreChars()) {
                            ch = stream.nextChar();
                            continue;
                        }
                        break;
                    }
                    stream.returnChar();
                    String tokenName = sb.toString();
                    if (getKeyWord(tokenName) != -1) {
                        return new Token(tokenName, Token.ttKeyWord, location);
                    }
                    return new Token(tokenName, Token.ttIdentifier, location);
                } else if ("(){}[],:;?".indexOf(ch) != -1) {
                    return new Token(String.valueOf(ch), Token.ttSeparator, location);
                } else if (ch == '#') {
                    do {
                        ch = stream.nextChar();
                    } while (stream.hasMoreChars() && (!isNewline(ch)));
                    continue;
                } else if (ch == '+') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '=') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttOperator, location);
                } else if (ch == '-') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '=') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttOperator, location);
                } else if (ch == '*') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '=') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttOperator, location);
                } else if (ch == '%') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '=') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttOperator, location);
                } else if (ch == '<') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '=') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttOperator, location);
                } else if (ch == '>') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '=') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttOperator, location);
                } else if (ch == '!') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '=') {
                            sb.append(ch);
                            if (stream.hasMoreChars()) {
                                ch = stream.nextChar();
                                if (ch == '=') {
                                    sb.append(ch);
                                    return new Token(sb.toString(), Token.ttOperator, location);
                                }
                                stream.returnChar();
                            }
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttOperator, location);
                } else if (ch == '=') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '=') {
                            sb.append(ch);
                            if (stream.hasMoreChars()) {
                                ch = stream.nextChar();
                                if (ch == '=') {
                                    sb.append(ch);
                                    return new Token(sb.toString(), Token.ttOperator, location);
                                }
                                stream.returnChar();
                            }
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttOperator, location);
                } else if (ch == '&') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '&') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        if (ch == '=') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttUnknown, location);
                } else if (ch == '|') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch == '|') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        if (ch == '=') {
                            sb.append(ch);
                            return new Token(sb.toString(), Token.ttOperator, location);
                        }
                        stream.returnChar();
                    }
                    return new Token(sb.toString(), Token.ttUnknown, location);
                } else if (ch == '.') {
                    sb.append(ch);
                    if (appendNumber(sb, stream) == PRESENT) {
                        if (appendExponent(sb, stream) == ERROR) {
                            error(INVALID_DECIMAL_LITERAL, location);
                        }
                        return new Token(sb.toString(), Token.ttFloatConstant, location);
                    }
                    return new Token(sb.toString(), Token.ttSeparator, location);
                } else if (ch == '\'') {
                    sb.append(ch);
                    ESC esc = getESC_SEQ(stream);
                    if (esc != null) {
                        esc.appendSymbol(sb, location, false);
                    } else {
                        if (stream.hasMoreChars()) {
                            ch = stream.nextChar();
                            if (ch == '\'') {
                                error(EMPTY_SYMBOL_LITERAL, location);
                            }
                            if (isSymbol(ch)) {
                                sb.append(ch);
                            } else {
                                error(INVALID_SYMBOL_LITERAL, location);
                            }
                        }
                    }
                    if (stream.hasMoreChars()) {
                        ch = stream.nextChar();
                        if (ch != '\'') {
                            error(UNCLOSED_SYMBOL_LITERAL, location);
                        }
                        sb.append(ch);
                    }
                    return new Token(sb.toString(), Token.ttCharConstant, location);
                } else if (ch == '\"') {
                    sb.append(ch);
                    if (stream.hasMoreChars()) {
                        while (true) {
                            ESC esc = getESC_SEQ(stream);
                            if (esc != null) {
                                esc.appendSymbol(sb, location, true);
                            } else {
                                if (stream.hasMoreChars()) {
                                    ch = stream.nextChar();
                                    if (ch == '\"') {
                                        sb.append(ch);
                                        break;
                                    }
                                    if (isNewline(ch)) {
                                        error(UNCLOSED_STRING_LITERAL, location);
                                    }
                                    if (isString(ch)) {
                                        sb.append(ch);
                                        continue;
                                    } else {
                                        error(INVALID_STRING_LITERAL, location);
                                    }
                                } else {
                                    error(UNCLOSED_STRING_LITERAL, location);
                                }
                            }
                        }
                        return new Token(sb.toString(), Token.ttStringConstant, location);
                    } else {
                        error(INVALID_STRING_LITERAL, location);
                    }
                } else {
                    throw new DinaException("Недопустимый символ: '" + ch + "' (0x" + Integer.toHexString(ch) + ")", location);
                }
            }
            return null;
        }
    }

    private static int appendNumber(StringBuffer sb, CharStream stream) {
        if (stream.hasMoreChars()) {
            char ch = stream.nextChar();
            if (isDecDigit(ch)) {
                while (isDecDigit(ch)) {
                    sb.append(ch);
                    ch = stream.nextChar();
                    continue;
                }
                stream.returnChar();
                return PRESENT;
            }
            stream.returnChar();
        }
        return ABSENT;
    }

    private static int appendExponent(StringBuffer sb, CharStream stream) {
        if (stream.hasMoreChars()) {
            char ch = stream.nextChar();
            if (ch == 'e' || ch == 'E') {
                sb.append(ch);
                ch = stream.nextChar();
                if (ch == '+' || ch == '-') {
                    sb.append(ch);
                } else {
                    stream.returnChar();
                }
                if (appendNumber(sb, stream) == PRESENT) {
                    return PRESENT;
                }
                return ERROR;
            }
            stream.returnChar();
        }
        return ABSENT;
    }

    private static ESC getESC_SEQ(CharStream stream) {
        if (stream.hasMoreChars()) {
            char ch = stream.nextChar();
            if (ch == '\\') {
                if (stream.hasMoreChars()) {
                    ch = stream.nextChar();
                    {//CONTROL
                        int controlSymbol = getControlSymbol(ch);
                        if (controlSymbol != -1) {
                            return new ESC(ESC.CONTROL, String.valueOf((char) controlSymbol));
                        }
                    }
                    {//OCTAL
                        if (ch >= '0' && ch <= '3') {//'\000'..'\377'
                            StringBuilder sb = new StringBuilder();
                            sb.append(ch);
                            if (stream.hasMoreChars()) {
                                ch = stream.nextChar();
                                if (ch >= '0' && ch <= '7') {
                                    sb.append(ch);
                                    if (stream.hasMoreChars()) {
                                        ch = stream.nextChar();
                                        if (ch >= '0' && ch <= '7') {
                                            sb.append(ch);
                                        } else {
                                            stream.returnChar();
                                        }
                                    }
                                } else {
                                    stream.returnChar();
                                }
                            }
                            return new ESC(ESC.OCTAL, sb.toString());
                        }
                        if (ch >= '0' && ch <= '7') {//'\000'..'\007' or '\000'..'\077'
                            StringBuilder sb = new StringBuilder();
                            sb.append(ch);
                            if (stream.hasMoreChars()) {
                                ch = stream.nextChar();
                                if (ch >= '0' && ch <= '7') {
                                    sb.append(ch);
                                } else {
                                    stream.returnChar();
                                }
                            }
                            return new ESC(ESC.OCTAL, sb.toString());
                        }
                    }
                    {//UNICODE
                        if (ch == 'u') {
                            StringBuilder sb = new StringBuilder();
                            int numberOfDigits = 4;
                            for (int shiftIndex = 0; shiftIndex < numberOfDigits; shiftIndex++) {
                                if (stream.hasMoreChars()) {
                                    ch = stream.nextChar();
                                    if (isHexDigit(ch)) {
                                        sb.append(ch);
                                        continue;
                                    }
                                }
                                return new ESC();
                            }
                            return new ESC(ESC.UNICODE, sb.toString());
                        }
                    }
                    return new ESC();
                } else {
                    return new ESC();
                }
            } else {
                stream.returnChar();
                return null;
            }
        }
        return null;
    }

    private static int getControlSymbol(char ch) {
        switch (ch) {
            case '"':
            case '\'':
            case '\\': {
                return ch;
            }
            case 'b': {
                return '\b';
            }
            case 'f': {
                return '\f';
            }
            case 'n': {
                return '\n';
            }
            case 'r': {
                return '\r';
            }
            case 't': {
                return '\t';
            }
        }
        return -1;
    }

    public DinaParser(String source) {
        stream = new CharStream(source);
    }

    public Token nextTokens() {
        Token token = getToken(this);
        stream.clearReturnBuffer();
        return token;
    }

    private static class ESC {

        public static final int UNICODE = 16;
        public static final int OCTAL = 8;
        public static final int CONTROL = 0;
        private int symbolKind;
        private String symbol;
        private int result;

        public ESC(int symbolKing, String symbol) {
            this.symbolKind = symbolKing;
            this.symbol = symbol;
            result = PRESENT;
        }

        public ESC() {
            this.result = ERROR;
        }

        public void appendSymbol(StringBuffer sb, Location position, boolean inString) {
            switch (result) {
                case PRESENT: {
                    switch (symbolKind) {
                        case CONTROL: {
                            sb.append(symbol);
                            break;
                        }
                        default: {
                            sb.append((char) Integer.parseInt(symbol, symbolKind));
                            break;
                        }
                    }
                    break;
                }
                case ERROR: {
                    if (inString) {
                        error(INVALID_ESCAPE_SYMBOL_LITERAL, position);
                    }
                    error(INVALID_SYMBOL_LITERAL, position);
                }
            }
        }
    }
}
