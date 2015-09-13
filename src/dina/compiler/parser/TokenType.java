package dina.compiler.parser;

public interface TokenType {

    byte ttUnknown = 0;
    byte ttKeyWord = 1;
    byte ttOperator = 2;
    byte ttStringConstant = 3;
    byte ttIntConstant = 4;
    byte ttFloatConstant = 5;
    byte ttCharConstant = 6;
    byte ttSeparator = 7;
    byte ttIdentifier = 8;
}
