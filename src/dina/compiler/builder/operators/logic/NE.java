package dina.compiler.builder.operators.logic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class NE extends PrimitiveExpression {

    private int size = -1;

    public NE(Node leftOperand, Node rightOperand) {
        Common.equalsOrNotEquasl("!=", leftOperand, rightOperand);
        init(this, leftOperand, rightOperand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " != " + rightOperand.trace();
    }

    private static void compile0(NE ne) {
        int leftType = ne.leftOperand.getNodeType().getId();
        int rightType = ne.rightOperand.getNodeType().getId();
        int arrayDepth = ne.leftOperand.getNodeType().getDimension();
        if (leftType == rightType || Common.isInteger(ne.leftOperand, ne.rightOperand)) {
            ne.leftOperand.compile();
            ne.rightOperand.compile();
            switch (arrayDepth) {
                case 0: {
                    switch (leftType) {
                        case NodeType.T_BOOLEAN:
                        case NodeType.T_CHAR:
                        case NodeType.T_INT: {
                            Output.writeByte(Constants.IF_ICMPNE);
                            break;
                        }
                        case NodeType.T_FLOAT: {
                            Output.writeByte(Constants.IF_FCMPNE);
                            break;
                        }
                        case NodeType.T_STRING: {
                            Output.writeByte(Constants.IF_SCMPNE);
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    Output.writeByte(Constants.IF_ACMPNE);
                    Output.writeShort(ne.leftOperand.getNodeType().getId());
                    break;
                }
            }
        } else {
            if ((leftType == NodeType.T_INT || leftType == NodeType.T_CHAR) && rightType == NodeType.T_FLOAT) {
                ne.leftOperand.compile();
                Output.writeByte(Constants.I2F);
                ne.rightOperand.compile();
                Output.writeByte(Constants.IF_FCMPNE);
            }
            if (leftType == NodeType.T_FLOAT && (rightType == NodeType.T_INT || rightType == NodeType.T_CHAR)) {
                ne.leftOperand.compile();
                ne.rightOperand.compile();
                Output.writeByte(Constants.I2F);
                Output.writeByte(Constants.IF_FCMPNE);
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

    private static int getSize0(NE ne) {
        int value = 0;
        int leftType = ne.leftOperand.getNodeType().getId();
        int rightType = ne.rightOperand.getNodeType().getId();
        int arrayDepth = ne.leftOperand.getNodeType().getDimension();
        value += ne.leftOperand.getSize();
        value += ne.rightOperand.getSize();
        if (leftType == rightType || Common.isInteger(ne.leftOperand, ne.rightOperand)) {
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
                    return new BooleanConstant(leftOperand.getBooleanValue() != rightOperand.getBooleanValue());
                }
                if ((leftId == NodeType.T_CHAR || leftId == NodeType.T_INT)
                        && (rightId == NodeType.T_CHAR || rightId == NodeType.T_INT)) {
                    return new BooleanConstant(leftOperand.getIntegerValue() != rightOperand.getIntegerValue());
                }
                if (leftId == NodeType.T_FLOAT && rightId == NodeType.T_FLOAT) {
                    float left = leftOperand.getFloatValue();
                    float right = rightOperand.getFloatValue();
                    return new BooleanConstant(Float.floatToIntBits(left) != Float.floatToIntBits(right));
                }
                if (leftId == NodeType.T_STRING && rightId == NodeType.T_STRING) {
                    String left = leftOperand.getStringValue();
                    String right = rightOperand.getStringValue();
                    return new BooleanConstant(!left.equals(right));
                }
            }
        }
        return this;
    }
}
