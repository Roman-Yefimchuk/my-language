package dina.compiler.builder.operators.logic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class GE extends PrimitiveExpression {

    private int size = -1;

    public GE(Node leftOperand, Node rightOperand) {
        Common.initArithmetic(">=", leftOperand, rightOperand);
        init(this, leftOperand, rightOperand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " >= " + rightOperand.trace();
    }

    private static void compile0(GE ge) {
        if (ge.leftOperand.getNodeType().getId() == ge.rightOperand.getNodeType().getId()
                || Common.isInteger(ge.leftOperand, ge.rightOperand)) {
            ge.leftOperand.compile();
            ge.rightOperand.compile();
            switch (ge.leftOperand.getNodeType().getId()) {
                case NodeType.T_CHAR:
                case NodeType.T_INT: {
                    Output.writeByte(Constants.IF_ICMPGE);
                    break;
                }
                case NodeType.T_FLOAT: {
                    Output.writeByte(Constants.IF_FCMPGE);
                    break;
                }
            }
        } else {
            if ((ge.leftOperand.getNodeType().getId() == NodeType.T_INT
                    || ge.leftOperand.getNodeType().getId() == NodeType.T_CHAR)
                    && ge.rightOperand.getNodeType().getId() == NodeType.T_FLOAT) {
                ge.leftOperand.compile();
                Output.writeByte(Constants.I2F);
                ge.rightOperand.compile();
                Output.writeByte(Constants.IF_FCMPGE);
            }
            if (ge.leftOperand.getNodeType().getId() == NodeType.T_FLOAT
                    && (ge.rightOperand.getNodeType().getId() == NodeType.T_INT
                    || ge.rightOperand.getNodeType().getId() == NodeType.T_CHAR)) {
                ge.leftOperand.compile();
                ge.rightOperand.compile();
                Output.writeByte(Constants.I2F);
                Output.writeByte(Constants.IF_FCMPGE);
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

    private static int getSize0(GE ge) {
        int value = 0;
        if (ge.leftOperand.getNodeType().getId() == ge.rightOperand.getNodeType().getId()
                || Common.isInteger(ge.leftOperand, ge.rightOperand)) {
            value += ge.leftOperand.getSize();
            value += ge.rightOperand.getSize();
            value += SizeOf.SIZEOF_BYTE;
        } else {
            value += ge.leftOperand.getSize();
            value += ge.rightOperand.getSize();
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
                return new BooleanConstant(leftOperand.getIntegerValue() >= rightOperand.getIntegerValue());
            }
            if (leftId == NodeType.T_FLOAT && rightId == NodeType.T_FLOAT) {
                float left = leftOperand.getFloatValue();
                float right = rightOperand.getFloatValue();
                return new BooleanConstant(Float.floatToIntBits(left) >= Float.floatToIntBits(right));
            }
        }
        return this;
    }
}
