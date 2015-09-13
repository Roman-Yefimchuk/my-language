package dina.compiler.builder.operators.logic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class LT extends PrimitiveExpression {

    private int size = -1;

    public LT(Node leftOperand, Node rightOperand) {
        Common.initArithmetic("<", leftOperand, rightOperand);
        init(this, leftOperand, rightOperand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " < " + rightOperand.trace();
    }

    private static void compile0(LT lt) {
        if (lt.leftOperand.getNodeType().getId() == lt.rightOperand.getNodeType().getId()
                || Common.isInteger(lt.leftOperand, lt.rightOperand)) {
            lt.leftOperand.compile();
            lt.rightOperand.compile();
            switch (lt.leftOperand.getNodeType().getId()) {
                case NodeType.T_CHAR:
                case NodeType.T_INT: {
                    Output.writeByte(Constants.IF_ICMPLT);
                    break;
                }
                case NodeType.T_FLOAT: {
                    Output.writeByte(Constants.IF_FCMPLT);
                    break;
                }
            }
        } else {
            if ((lt.leftOperand.getNodeType().getId() == NodeType.T_INT
                    || lt.leftOperand.getNodeType().getId() == NodeType.T_CHAR)
                    && lt.rightOperand.getNodeType().getId() == NodeType.T_FLOAT) {
                lt.leftOperand.compile();
                Output.writeByte(Constants.I2F);
                lt.rightOperand.compile();
                Output.writeByte(Constants.IF_FCMPLT);
            }
            if (lt.leftOperand.getNodeType().getId() == NodeType.T_FLOAT
                    && (lt.rightOperand.getNodeType().getId() == NodeType.T_INT
                    || lt.rightOperand.getNodeType().getId() == NodeType.T_CHAR)) {
                lt.leftOperand.compile();
                lt.rightOperand.compile();
                Output.writeByte(Constants.I2F);
                Output.writeByte(Constants.IF_FCMPLT);
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

    private static int getSize0(LT lt) {
        int value = 0;
        if (lt.leftOperand.getNodeType().getId() == lt.rightOperand.getNodeType().getId()
                || Common.isInteger(lt.leftOperand, lt.rightOperand)) {
            value += lt.leftOperand.getSize();
            value += lt.rightOperand.getSize();
            value += SizeOf.SIZEOF_BYTE;
        } else {
            value += lt.leftOperand.getSize();
            value += lt.rightOperand.getSize();
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
                return new BooleanConstant(leftOperand.getIntegerValue() < rightOperand.getIntegerValue());
            }
            if (leftId == NodeType.T_FLOAT && rightId == NodeType.T_FLOAT) {
                float left = leftOperand.getFloatValue();
                float right = rightOperand.getFloatValue();
                return new BooleanConstant(Float.floatToIntBits(left) < Float.floatToIntBits(right));
            }
        }
        return this;
    }
}
