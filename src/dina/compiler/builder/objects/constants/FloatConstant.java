package dina.compiler.builder.objects.constants;

import java.util.*;
import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;

public class FloatConstant extends Constant implements NumberConstant {

    private static ArrayList<Float> constantPool = new ArrayList<Float>();
    private static ArrayList<Float> optimizeConstantPool = new ArrayList<Float>();
    private float floatValue;

    public FloatConstant(float floatConstant) {
        int index = constantPool.indexOf(floatConstant);
        if (index != -1) {
            floatValue = constantPool.get(index);
        } else {
            if (constantPool.size() > MAX_CONSTANTS_COUNT) {
                throw new DinaException("Количество строковых констант > "
                        + MAX_CONSTANTS_COUNT, DinaException.COMPILATION_ERROR);
            }
            floatValue = floatConstant;
            constantPool.add(floatValue);
        }
    }

    public FloatConstant(String internalValue) {
        this(Float.parseFloat(internalValue));
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_FLOAT;
    }

    @Override
    public String trace() {
        return String.valueOf(floatValue);
    }

    public static void compile0(FloatConstant floatConstant) {
        float value = floatConstant.floatValue;
        Output.writeByte(Constants.FCONST);
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

    public Node getNegativeValue() {
        return new FloatConstant(-floatValue);
    }

    @Override
    public float getFloatValue() {
        return floatValue;
    }

    public static ArrayList<Float> getOptimizedConstantPool() {
        return optimizeConstantPool;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(floatValue);
    }
}
