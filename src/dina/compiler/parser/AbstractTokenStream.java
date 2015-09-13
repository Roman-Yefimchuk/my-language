package dina.compiler.parser;

public interface AbstractTokenStream {

    public Token nextToken();

    public Token currentToken();

    public void setCurrentToken(Token token);

    public void back();
}
