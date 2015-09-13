package dina.compiler.parser;

public interface ParserConstants {

    public static final String[] operators = {
        "+", "-", "*", "/", "%", "&&", "||", "<", "<=", ">",
        ">=", "==", "!=", "=", "!", "+=", "-=", "*=", "/=",
        "&=", "|=", "%=", "===", "!=="
    };
    public static final String[] keyWords = {
        "program", "var", "if", "do", "while", "return", "true",
        "false", "else", "break", "continue", "for", "function",
        "void", "string", "int", "float", "char", "boolean", "goto",
        "new", "try", "catch", "throw", "forward", "constructor",
        "destructor", "record", "const", "switch", "case", "default",
        "exit", "object"
    };
    public static final String[] separators = {
        "(", ")", ".", "{", "}", ",", ";", "?", ":", "[", "]"
    };
    //operators
    int PLUS = 0;//"+"
    int MINUS = 1;//"-"
    int STAR = 2;//"*"
    int SLASH = 3;//"/"
    int REM = 4;//"%"
    int AND = 5;//"&&"
    int OR = 6;//"||"
    int LT = 7;//"<"
    int LE = 8;//"<="
    int GT = 9;//">"
    int GE = 10;//">="
    int EQ = 11;//"=="
    int NE = 12;//"!="
    int ASSIGN = 13;//"="
    int BANG = 14;//"!"
    int PLUSASSIGN = 15;//"+="
    int MINUSASSIGN = 16;//"-="
    int STARASSIGN = 17;//"*="
    int SLASHASSIGN = 18;//"/="
    int ANDASSIGN = 19;//"&="
    int ORASSIGN = 20;//"|="
    int REMASSIGN = 21;//"%="
    int TYPE_OF_EQ = 22;//"==="
    int TYPE_OF_NE = 23;//"!=="
    //keywords
    int PROGRAM = 0;
    int VAR = 1;
    int IF = 2;
    int DO = 3;
    int WHILE = 4;
    int RETURN = 5;
    int TRUE = 6;
    int FALSE = 7;
    int ELSE = 8;
    int BREAK = 9;
    int CONTINUE = 10;
    int FOR = 11;
    int FUNCTION = 12;
    int VOID = 13;
    int STRING = 14;
    int INT = 15;
    int FLOAT = 16;
    int CHAR = 17;
    int BOOLEAN = 18;
    int GOTO = 19;
    int NEW = 20;
    int TRY = 21;
    int CATCH = 22;
    int THROW = 23;
    int FORWARD = 24;
    int CONSTRUCTOR = 25;
    int DESTRUCTOR = 26;
    int RECORD = 27;
    int CONST = 28;
    int SWITCH = 29;
    int CASE = 30;
    int DEFAULT = 31;
    int EXIT = 32;
    int OBJECT = 33;
    //separators
    int LPAREN = 0;//"("
    int RPAREN = 1;//")"
    int DOT = 2;//"."
    int LBRACE = 3;//"{"
    int RBRACE = 4;//"}"
    int COMMA = 5;//","
    int SEMICOLON = 6;//";"
    int HOOK = 7;//"?"
    int COLON = 8;// ":"
    int LBRACKET = 9;//"["
    int RBRACKET = 10;//"]"
}
