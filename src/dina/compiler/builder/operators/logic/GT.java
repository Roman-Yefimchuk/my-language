package dina.compiler.builder.operators.logic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class GT extends PrimitiveExpression {

    private int size = -1;

    public GT(Node leftOperand, Node rightOperand) {
        Common.initArithmetic(">", leftOperand, rightOperand);
        init(this, leftOperand, rightOperand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " > " + rightOperand.trace();
    }

    private static void compile0(GT gt) {
        if (gt.leftOperand.getNodeType().getId() == gt.rightOperand.getNodeType().getId()
                || Common.isInteger(gt.leftOperand, gt.rightOperand)) {
            gt.leftOperand.compile();
            gt.rightOperand.compile();
            switch (gt.leftOperand.getNodeType().getId()) {
                case NodeType.T_CHAR:
                case NodeType.T_INT: {
                    Output.writeByte(Constants.IF_ICMPGT);
                    break;
                }
                case NodeType.T_FLOAT: {
                    Output.writeByte(Constants.IF_FCMPGT);
                    break;
                }
            }
        } else {
            if ((gt.leftOperand.getNodeType().getId() == NodeType.T_INT
                    || gt.leftOperand.getNodeType().getId() == NodeType.T_CHAR)
                    && gt.rightOperand.getNodeType().getId() == NodeType.T_FLOAT) {
                gt.leftOperand.compile();
                Output.writeByte(Constants.I2F);
                gt.rightOperand.compile();
                Output.writeByte(Constants.IF_FCMPGT);
            }
            if (gt.leftOperand.getNodeType().getId() == NodeType.T_FLOAT
                    && (gt.rightOperand.getNodeType().getId() == NodeType.T_INT
                    || gt.rightOperand.getNodeType().getId() == NodeType.T_CHAR)) {
                gt.leftOperand.compile();
                gt.rightOperand.compile();
                Output.writeByte(Constants.I2F);
                Output.writeByte(Constants.IF_FCMPGT);
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

    private static int getSize0(GT gt) {
        int value = 0;
        if (gt.leftOperand.getNodeType().getId() == gt.rightOperand.getNodeType().getId()
                || Common.isInteger(gt.leftOperand, gt.rightOperand)) {
            value += gt.leftOperand.getSize();
            value += gt.rightOperand.getSize();
            value += SizeOf.SIZEOF_BYTE;
        } else {
            value += gt.leftOperand.getSize();
            value += gt.rightOperand.getSize();
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
                return new BooleanConstant(leftOperand.getIntegerValue() > rightOperand.getIntegerValue());
            }
            if (leftId == NodeType.T_FLOAT && rightId == NodeType.T_FLOAT) {
                float left = leftOperand.getFloatValue();
                float right = rightOperand.getFloatValue();
                return new BooleanConstant(Float.floatToIntBits(left) > Float.floatToIntBits(right));
            }
        }
        return this;
    }
}
