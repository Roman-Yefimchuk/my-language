package dina.compiler.builder.objects.constants;

import java.util.*;
import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;

public class StringConstant extends Constant {

    private static ArrayList<String> constantPool = new ArrayList<String>();
    private static ArrayList<String> optimizeConstantPool = new ArrayList<String>();
    private String stringValue;

    public StringConstant(String stringConstant) {
        int index = constantPool.indexOf(stringConstant);
        if (index != -1) {
            stringValue = constantPool.get(index);
        } else {
            if (constantPool.size() > MAX_CONSTANTS_COUNT) {
                throw new DinaException("Количество строковых констант > "
                        + MAX_CONSTANTS_COUNT, DinaException.COMPILATION_ERROR);
            }
            stringValue = stringConstant;
            constantPool.add(stringValue);
        }
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_STRING;
    }

    @Override
    public String trace() {
        return "\"" + stringValue + "\"";
    }

    public static void compile0(StringConstant stringConstant) {
        String value = stringConstant.stringValue;
        Output.writeByte(Constants.SCONST);
        int index = optimizeConstantPool.indexOf(value);
        if (index == -1) {
            index = optimizeConstantPool.size();
            optimizeConstantPool.add(value);
        }
        Output.writeShort(index);
    }

    @Override
    public void compile() {
        compile0(this);
    }

    @Override
    public int getSize() {
        return SizeOf.SIZEOF_FCONST + SizeOf.SIZEOF_SHORT;
    }

    public static void reset() {
        constantPool.clear();
        optimizeConstantPool.clear();
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }

    public static ArrayList<String> getOptimizedConstantPool() {
        return optimizeConstantPool;
    }
}
