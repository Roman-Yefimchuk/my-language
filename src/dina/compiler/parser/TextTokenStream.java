package dina.compiler.parser;

import java.util.*;

public class TextTokenStream implements AbstractTokenStream {

    private ArrayList<Token> stream;
    private DinaParser parser;
    private int tokenIndex = -1;

    public TextTokenStream(String source) {
        parser = new DinaParser(source);
        stream = new ArrayList<Token>();
    }

    public Token nextToken() {
        tokenIndex++;
        if (tokenIndex == stream.size()) {
            Token token = parser.nextTokens();
            if (token == null) {
                return null;
            }
            stream.add(token);
            return token;
        }
        return stream.get(tokenIndex);
    }

    public Token currentToken() {
        return stream.get(tokenIndex);
    }

    public void back() {
        tokenIndex--;
    }

    public void setCurrentToken(Token token) {
        tokenIndex = stream.indexOf(token);
    }
}
