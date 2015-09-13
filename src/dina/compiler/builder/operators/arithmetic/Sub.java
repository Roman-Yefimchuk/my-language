package dina.compiler.builder.operators.arithmetic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class Sub extends PrimitiveExpression {

    private int size = -1;

    public Sub(Node leftOperand, Node rightOperand) {
        Common.initArithmetic("-", leftOperand, rightOperand);
        init(this, leftOperand, rightOperand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.getMaxType(leftOperand.getNodeType(), rightOperand.getNodeType());
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " - " + rightOperand.trace();
    }

    @Override
    public void compile() {
        compile0(this);
    }

    private static void compile0(Sub sub) {
        int leftType = sub.leftOperand.getNodeType().getId();
        int rightType = sub.rightOperand.getNodeType().getId();
        if ((leftType == rightType) || Common.isInteger(sub.leftOperand, sub.rightOperand)) {
            sub.leftOperand.compile();
            sub.rightOperand.compile();
            switch (leftType) {
                case NodeType.T_CHAR:
                case NodeType.T_INT: {
                    Output.writeByte(Constants.ISUB);
                    break;
                }
                case NodeType.T_FLOAT: {
                    Output.writeByte(Constants.FSUB);
                    break;
                }
            }
        } else {
            if ((leftType == NodeType.T_INT || leftType == NodeType.T_CHAR) && rightType == NodeType.T_FLOAT) {
                sub.leftOperand.compile();
                Output.writeByte(Constants.I2F);
                sub.rightOperand.compile();
                Output.writeByte(Constants.FSUB);
            } else if (leftType == NodeType.T_FLOAT && (rightType == NodeType.T_INT || rightType == NodeType.T_CHAR)) {
                sub.leftOperand.compile();
                sub.rightOperand.compile();
                Output.writeByte(Constants.I2F);
                Output.writeByte(Constants.FSUB);
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

    private static int getSize0(Sub sub) {
        int value = 0;
        value += sub.leftOperand.getSize();
        value += sub.rightOperand.getSize();
        if (sub.leftOperand.getNodeType().getId() == sub.rightOperand.getNodeType().getId()
                || Common.isInteger(sub.leftOperand, sub.rightOperand)) {
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
                return new IntegerConstant(leftOperand.getIntegerValue() - rightOperand.getIntegerValue());
            }
            if (leftId == NodeType.T_FLOAT && rightId == NodeType.T_FLOAT) {
                float left = leftOperand.getFloatValue();
                float right = rightOperand.getFloatValue();
                return new FloatConstant(left - right);
            }
        }
        return this;
    }
}
