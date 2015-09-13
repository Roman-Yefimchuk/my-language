package dina.compiler.builder.operators.arithmetic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.constants.*;
import dina.compiler.builder.operators.arithmetic.divisible.*;

public class Div extends Divisible {

    private int size = -1;

    public Div(Node leftOperand, Node rightOperand) {
        Common.initArithmetic("/", leftOperand, rightOperand);
        init(this, leftOperand, rightOperand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.getMaxType(leftOperand.getNodeType(), rightOperand.getNodeType());
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " / " + rightOperand.trace();
    }

    @Override
    public void compile() {
        compile0(this);
    }

    private static void compile0(Div div) {
        int leftType = div.leftOperand.getNodeType().getId();
        int rightType = div.rightOperand.getNodeType().getId();
        if ((leftType == rightType) || Common.isInteger(div.leftOperand, div.rightOperand)) {
            div.leftOperand.compile();
            div.rightOperand.compile();
            switch (leftType) {
                case NodeType.T_CHAR:
                case NodeType.T_INT: {
                    Output.writeByte(Constants.IDIV);
                    break;
                }
                case NodeType.T_FLOAT: {
                    Output.writeByte(Constants.FDIV);
                    break;
                }
            }
        } else {
            if ((leftType == NodeType.T_INT || leftType == NodeType.T_CHAR) && rightType == NodeType.T_FLOAT) {
                div.leftOperand.compile();
                Output.writeByte(Constants.I2F);
                div.rightOperand.compile();
                Output.writeByte(Constants.FDIV);
            } else if (leftType == NodeType.T_FLOAT && (rightType == NodeType.T_INT || rightType == NodeType.T_CHAR)) {
                div.leftOperand.compile();
                div.rightOperand.compile();
                Output.writeByte(Constants.I2F);
                Output.writeByte(Constants.FDIV);
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

    private static int getSize0(Div div) {
        int value = 0;
        value += div.leftOperand.getSize();
        value += div.rightOperand.getSize();
        if (div.leftOperand.getNodeType().getId() == div.rightOperand.getNodeType().getId()
                || Common.isInteger(div.leftOperand, div.rightOperand)) {
            value += SizeOf.SIZEOF_BYTE;
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
            if ((leftId == NodeType.T_CHAR || leftId == NodeType.T_INT)
                    && (rightId == NodeType.T_CHAR || rightId == NodeType.T_INT)) {
                return new IntegerConstant(leftOperand.getIntegerValue() / rightOperand.getIntegerValue());
            }
            if (leftId == NodeType.T_FLOAT && rightId == NodeType.T_FLOAT) {
                float left = leftOperand.getFloatValue();
                float right = rightOperand.getFloatValue();
                return new FloatConstant(left / right);
            }
        }
        return this;
    }
}
