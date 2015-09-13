package dina.compiler.builder.objects;

import dina.compiler.builder.*;

public class Constant extends Node {

    public static final int MAX_CONSTANTS_COUNT = 65535;

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.CONSTANT;
    }

    public static int sizeOf(int value) {
        if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            return SizeOf.SIZEOF_BYTE;
        }
        if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            return SizeOf.SIZEOF_SHORT;
        }
        return SizeOf.SIZEOF_INTEGER;
    }
}
