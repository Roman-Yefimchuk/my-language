package dina.compiler.builder.operators;

import dina.compiler.builder.*;
import dina.compiler.builder.objects.constants.*;

public class PrimitiveExpression extends Node {

    protected Node leftOperand;
    protected Node rightOperand;

    public static void init(PrimitiveExpression pe, Node leftOperand, Node rightOperand) {
        pe.leftOperand = leftOperand;
        pe.rightOperand = rightOperand;
        int leftType = leftOperand.getNodeType().getId();
        int rightType = rightOperand.getNodeType().getId();
        if (NodeType.isNumeric(leftOperand) && NodeType.isNumeric(rightOperand)) {
            if (leftType == NodeType.T_FLOAT) {
                if (rightOperand.getNodeClass() == NodeClass.CONSTANT && rightType != NodeType.T_FLOAT) {
                    pe.rightOperand = new FloatConstant(rightOperand.getIntegerValue());
                } else {
                    pe.rightOperand = pe.rightOperand.optimize();
                }
            } else if (rightType == NodeType.T_FLOAT) {
                if (leftOperand.getNodeClass() == NodeClass.CONSTANT && leftType != NodeType.T_FLOAT) {
                    pe.leftOperand = new FloatConstant(leftOperand.getIntegerValue());
                } else {
                    pe.leftOperand = pe.leftOperand.optimize();
                }
            }
        }
    }

    @Override
    public NodeClass getNodeClass() {
        return getNodeClass0(this);
    }

    private static NodeClass getNodeClass0(PrimitiveExpression pe) {
        NodeClass leftOperand = pe.leftOperand.getNodeClass();
        NodeClass rightOperand = pe.rightOperand.getNodeClass();
        if (leftOperand == rightOperand) {
            return leftOperand;
        }
        if (leftOperand == NodeClass.CONSTANT) {
            return rightOperand;
        } else {
            return leftOperand;
        }
    }
}
