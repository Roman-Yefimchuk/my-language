package dina.compiler.builder.objects.constants;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;

public class CharConstant extends Constant implements NumberConstant {

    private char charValue;

    public CharConstant(char charValue) {
        this.charValue = charValue;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_CHAR;
    }

    @Override
    public String trace() {
        return "'" + toChar(charValue) + "'";
    }

    public static void compile0(CharConstant charConstant) {
        Output.writeByte(Constants.CCONST);
        Output.writeShort(charConstant.charValue);
    }

    @Override
    public void compile() {
        compile0(this);
    }

    @Override
    public int getSize() {
        return SizeOf.SIZEOF_CCONST + SizeOf.SIZEOF_SHORT;
    }

    public static String toChar(char charValue) {
        switch (charValue) {
            case '"': {
                return "\\";
            }
            case '\'': {
                return "\\'";
            }
            case '\\': {
                return "\\\\";
            }
            case '\b': {
                return "\\b";
            }
            case '\f': {
                return "\\f";
            }
            case '\n': {
                return "\\n";
            }
            case '\r': {
                return "\\r";
            }
            case '\t': {
                return "\\t";
            }
            default: {
                return String.valueOf(charValue);
            }
        }
    }

    @Override
    public int getIntegerValue() {
        return charValue;
    }

    public Node getNegativeValue() {
        return new IntegerConstant(-charValue);
    }

    @Override
    public String getStringValue() {
        return String.valueOf(charValue);
    }
}
