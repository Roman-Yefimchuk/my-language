package dina.compiler.parser;

public class Token implements TokenType {

    private String tokenName;
    private byte tokenType;
    private Location location;

    public Token(String tokenName, byte tokenType) {
        this(tokenName, tokenType, null);
    }

    public Token(String tokenName, byte tokenType, Location location) {
        this.tokenType = tokenType;
        this.tokenName = tokenName;
        this.location = location;
    }

    public String getName() {
        return tokenName;
    }

    public int getType() {
        return tokenType;
    }

    public int getColumn() {
        return location.getColumn();
    }

    public int getLine() {
        return location.getLine();
    }

    public int getPosition() {
        return location.getPosition();
    }

    @Override
    public String toString() {
        if (location == null) {
            return "'" + tokenName + "' = " + tokenTypeName[tokenType];
        }
        return "'" + tokenName + "' = " + tokenTypeName[tokenType] + ", Line: " + location.getLine() + ", Column: " + location.getColumn() + ", Position: " + location.getPosition();
    }

    public static char getChar(String tokenName) {
        return tokenName.charAt(1);
    }

    public static String getString(String tokenName) {
        return tokenName.substring(1, tokenName.length() - 1);
    }
    //
    private static String[] tokenTypeName = {
        "ttUnknown", "ttKeyWord", "ttOperator",
        "ttStringConstant", "ttIntegerConstant",
        "ttFloatConstant", "ttCharacterConstant",
        "ttSeparator", "ttIdentifier"
    };
}
