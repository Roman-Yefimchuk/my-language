package dina.compiler.builder.operators.logic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class EQ extends PrimitiveExpression {

    private int size = -1;

    public EQ(Node leftOperand, Node rightOperand) {
        Common.equalsOrNotEquasl("==", leftOperand, rightOperand);
        init(this, leftOperand, rightOperand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    private static void compile0(EQ eq) {
        int leftType = eq.leftOperand.getNodeType().getId();
        int rightType = eq.rightOperand.getNodeType().getId();
        int arrayDepth = eq.leftOperand.getNodeType().getDimension();
        if (leftType == rightType || Common.isInteger(eq.leftOperand, eq.rightOperand)) {
            eq.leftOperand.compile();
            eq.rightOperand.compile();
            switch (arrayDepth) {
                case 0: {
                    switch (leftType) {
                        case NodeType.T_BOOLEAN:
                        case NodeType.T_CHAR:
                        case NodeType.T_INT: {
                            Output.writeByte(Constants.IF_ICMPEQ);
                            break;
                        }
                        case NodeType.T_FLOAT: {
                            Output.writeByte(Constants.IF_FCMPEQ);
                            break;
                        }
                        case NodeType.T_STRING: {
                            Output.writeByte(Constants.IF_SCMPEQ);
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    Output.writeByte(Constants.IF_ACMPEQ);
                    Output.writeShort(eq.leftOperand.getNodeType().getId());
                    break;
                }
            }
        } else {
            if ((leftType == NodeType.T_INT || leftType == NodeType.T_CHAR) && rightType == NodeType.T_FLOAT) {
                eq.leftOperand.compile();
                Output.writeByte(Constants.I2F);
                eq.rightOperand.compile();
                Output.writeByte(Constants.IF_FCMPEQ);
            }
            if (leftType == NodeType.T_FLOAT && (rightType == NodeType.T_INT || rightType == NodeType.T_CHAR)) {
                eq.leftOperand.compile();
                eq.rightOperand.compile();
                Output.writeByte(Constants.I2F);
                Output.writeByte(Constants.IF_FCMPEQ);
            }
        }
    }

    @Override
    public void compile() {
        compile0(this);
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " == " + rightOperand.trace();
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(EQ eq) {
        int value = 0;
        int leftType = eq.leftOperand.getNodeType().getId();
        int rightType = eq.rightOperand.getNodeType().getId();
        int arrayDepth = eq.leftOperand.getNodeType().getDimension();
        value += eq.leftOperand.getSize();
        value += eq.rightOperand.getSize();
        if (leftType == rightType || Common.isInteger(eq.leftOperand, eq.rightOperand)) {
            switch (arrayDepth) {
                case 0: {
                    value += SizeOf.SIZEOF_BYTE;
                    break;
                }
                case 1: {
                    value += SizeOf.SIZEOF_BYTE;
                    value += SizeOf.SIZEOF_SHORT;
                    break;
                }
            }
        } else {
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
            int arrayDepth = leftOperand.getNodeType().getDimension();
            if (arrayDepth == 0) {
                if (leftId == NodeType.T_BOOLEAN && rightId == NodeType.T_BOOLEAN) {
                    return new BooleanConstant(leftOperand.getBooleanValue() == rightOperand.getBooleanValue());
                }
                if ((leftId == NodeType.T_CHAR || leftId == NodeType.T_INT)
                        && (rightId == NodeType.T_CHAR || rightId == NodeType.T_INT)) {
                    return new BooleanConstant(leftOperand.getIntegerValue() == rightOperand.getIntegerValue());
                }
                if (leftId == NodeType.T_FLOAT && rightId == NodeType.T_FLOAT) {
                    float left = leftOperand.getFloatValue();
                    float right = rightOperand.getFloatValue();
                    return new BooleanConstant(Float.floatToIntBits(left) == Float.floatToIntBits(right));
                }
                if (leftId == NodeType.T_STRING && rightId == NodeType.T_STRING) {
                    String left = leftOperand.getStringValue();
                    String right = rightOperand.getStringValue();
                    return new BooleanConstant(left.equals(right));
                }
            }
        }
        return this;
    }
}
