package dina.compiler.builder.operators.logic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class LE extends PrimitiveExpression {

    private int size = -1;

    public LE(Node leftOperand, Node rightOperand) {
        Common.initArithmetic("<=", leftOperand, rightOperand);
        init(this, leftOperand, rightOperand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " <= " + rightOperand.trace();
    }

    private static void compile0(LE le) {
        if (le.leftOperand.getNodeType().getId() == le.rightOperand.getNodeType().getId()
                || Common.isInteger(le.leftOperand, le.rightOperand)) {
            le.leftOperand.compile();
            le.rightOperand.compile();
            switch (le.leftOperand.getNodeType().getId()) {
                case NodeType.T_CHAR:
                case NodeType.T_INT: {
                    Output.writeByte(Constants.IF_ICMPLE);
                    break;
                }
                case NodeType.T_FLOAT: {
                    Output.writeByte(Constants.IF_FCMPLE);
                    break;
                }
            }
        } else {
            if ((le.leftOperand.getNodeType().getId() == NodeType.T_INT
                    || le.leftOperand.getNodeType().getId() == NodeType.T_CHAR)
                    && le.rightOperand.getNodeType().getId() == NodeType.T_FLOAT) {
                le.leftOperand.compile();
                Output.writeByte(Constants.I2F);
                le.rightOperand.compile();
                Output.writeByte(Constants.IF_FCMPLE);
            }
            if (le.leftOperand.getNodeType().getId() == NodeType.T_FLOAT
                    && (le.rightOperand.getNodeType().getId() == NodeType.T_INT
                    || le.rightOperand.getNodeType().getId() == NodeType.T_CHAR)) {
                le.leftOperand.compile();
                le.rightOperand.compile();
                Output.writeByte(Constants.I2F);
                Output.writeByte(Constants.IF_FCMPLE);
            }
        }
    }

    @Override
    public void compile() {
        compile0(this);
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(LE le) {
        int value = 0;
        if (le.leftOperand.getNodeType().getId() == le.rightOperand.getNodeType().getId()
                || Common.isInteger(le.leftOperand, le.rightOperand)) {
            value += le.leftOperand.getSize();
            value += le.rightOperand.getSize();
            value += SizeOf.SIZEOF_BYTE;
        } else {
            value += le.leftOperand.getSize();
            value += le.rightOperand.getSize();
            value += SizeOf.SIZEOF_BYTE;
            value += SizeOf.SIZEOF_BYTE;
        }
        return value;
    }

    @Override
    public Node optimize() {
        if (getNodeClass() == NodeClass.CONSTANT) {
            int leftId = leftOperand.getNodeType().getId();
            int rightId = rightOperand.getNodeType().getId();
            if ((leftId == NodeType.T_CHAR || leftId == NodeType.T_INT)
                    && (rightId == NodeType.T_CHAR || rightId == NodeType.T_INT)) {
                return new BooleanConstant(leftOperand.getIntegerValue() <= rightOperand.getIntegerValue());
            }
            if (leftId == NodeType.T_FLOAT && rightId == NodeType.T_FLOAT) {
                float left = leftOperand.getFloatValue();
                float right = rightOperand.getFloatValue();
                return new BooleanConstant(Float.floatToIntBits(left) <= Float.floatToIntBits(right));
            }
        }
        return this;
    }
}
