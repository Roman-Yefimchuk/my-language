package dina.compiler.builder.operators.logic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class Or extends PrimitiveExpression {

    private int size = -1;

    public Or(Node leftOperand, Node rightOperand) {
        Common.initLogic("||", leftOperand, rightOperand);
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " || " + rightOperand.trace();
    }

    private static void compile0(Or or) {
        or.leftOperand.compile();
        or.rightOperand.compile();
        Output.writeByte(Constants.OR);
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

    private static int getSize0(Or or) {
        return or.leftOperand.getSize() + or.rightOperand.getSize() + SizeOf.SIZEOF_OR;
    }

    @Override
    public Node optimize() {
        if (getNodeClass() == NodeClass.CONSTANT) {
            boolean left = leftOperand.getBooleanValue();
            boolean right = rightOperand.getBooleanValue();
            return new BooleanConstant(left || right);
        }
        return this;
    }
}
