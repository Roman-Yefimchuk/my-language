package dina.compiler.builder.operators.logic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class And extends PrimitiveExpression {

    private int size = -1;

    public And(Node leftOperand, Node rightOperand) {
        Common.initLogic("&&", leftOperand, rightOperand);
        this.leftOperand = leftOperand.optimize();
        this.rightOperand = rightOperand.optimize();
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    private static void compile0(And and) {
        and.leftOperand.compile();
        and.rightOperand.compile();
        Output.writeByte(Constants.AND);
    }

    @Override
    public void compile() {
        compile0(this);
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " && " + rightOperand.trace();
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(And and) {
        return and.leftOperand.getSize() + and.rightOperand.getSize() + SizeOf.SIZEOF_AND;
    }

    @Override
    public Node optimize() {
        if (getNodeClass() == NodeClass.CONSTANT) {
            boolean left = leftOperand.getBooleanValue();
            boolean right = rightOperand.getBooleanValue();
            return new BooleanConstant(left && right);
        }
        return this;
    }
}
