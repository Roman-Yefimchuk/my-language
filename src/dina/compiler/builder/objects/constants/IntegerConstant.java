package dina.compiler.builder.objects.constants;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;

public class IntegerConstant extends Constant implements NumberConstant {

    private String internalValue;
    private Integer integerValue;
    private int size = -1;

    public IntegerConstant(int value) {
        integerValue = new Integer(value);
    }

    public IntegerConstant(String integerValue) {
        this.internalValue = integerValue;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_INT;
    }

    @Override
    public String trace() {
        if (internalValue == null) {
            return String.valueOf(integerValue.intValue());
        }
        return internalValue;
    }

    private static void checkValue(IntegerConstant integerConstant) {
        if (integerConstant.integerValue == null) {
            integerConstant.integerValue = getIntegerValue(integerConstant);
        }
    }

    private static void compile0(IntegerConstant integerConstant) {
        checkValue(integerConstant);
        int value = integerConstant.integerValue.intValue();
        switch (sizeOf(value)) {
            case SizeOf.SIZEOF_BYTE: {
                Output.writeByte(Constants.BCONST);
                Output.writeByte(value);
                break;
            }
            case SizeOf.SIZEOF_SHORT: {
                Output.writeByte(Constants.SICONST);
                Output.writeShort(value);
                break;
            }
            case SizeOf.SIZEOF_INTEGER: {
                Output.writeByte(Constants.ICONST);
                Output.writeInteger(value);
                break;
            }
        }
    }

    @Override
    public void compile() {
        compile0(this);
    }

    private static int getSize0(IntegerConstant integerConstant) {
        checkValue(integerConstant);
        int value = integerConstant.integerValue.intValue();
        switch (sizeOf(value)) {
            case SizeOf.SIZEOF_BYTE: {
                return SizeOf.SIZEOF_BCONST + SizeOf.SIZEOF_BYTE;
            }
            case SizeOf.SIZEOF_SHORT: {
                return SizeOf.SIZEOF_SICONST + SizeOf.SIZEOF_SHORT;
            }
            default: {
                return SizeOf.SIZEOF_ICONST + SizeOf.SIZEOF_INTEGER;
            }
        }
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    public Node getNegativeValue() {
        if (integerValue == null) {
            return new IntegerConstant("-" + internalValue);
        }
        return new IntegerConstant(-integerValue.intValue());
    }

    @Override
    public int getIntegerValue() {
        if (integerValue == null) {
            integerValue = getIntegerValue(this);
        }
        return integerValue.intValue();
    }

    private static Integer getIntegerValue(IntegerConstant constant) {
        int value = 0;
        try {
            value = Integer.parseInt(constant.internalValue);
        } catch (Exception ex) {
            throw new DinaException("Выход за границы диапазона: " + constant.internalValue, DinaException.COMPILATION_ERROR);
        }
        constant.internalValue = null;
        return new Integer(value);
    }

    @Override
    public String getStringValue() {
        if (integerValue == null) {
            integerValue = getIntegerValue(this);
        }
        return String.valueOf(integerValue.intValue());
    }
}
