package dina.compiler.builder.operators;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;

public class Conditional extends Node {

    private Node expression;
    private Node leftOperand;
    private Node rightOperand;
    private int size = -1;

    public Conditional(Node expression, Node leftOperand, Node rightOperand) {
        this.expression = expression;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.getMaxType(leftOperand.getNodeType(), rightOperand.getNodeType());
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.VALUE;
    }

    @Override
    public String trace() {
        return expression.trace() + " ? " + leftOperand.trace() + " : " + rightOperand.trace();
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compile0(Conditional conditional) {
        conditional.expression.compile();
        Output.writeByte(Constants.IFNE);
        Output.writeInteger(conditional.expression.getSize() + conditional.leftOperand.getSize() + 5 + 5 + conditional.getAddress());
        conditional.leftOperand.compile();
        Output.writeByte(Constants.GOTO);
        Output.writeInteger(conditional.expression.getSize() + conditional.leftOperand.getSize() + 5 + 5 + conditional.rightOperand.getSize() + conditional.getAddress());
        conditional.rightOperand.compile();
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(Conditional conditional) {
        int value = 0;
        value += conditional.expression.getSize();
        value += SizeOf.SIZEOF_IFNE;
        value += SizeOf.SIZEOF_INTEGER;
        value += conditional.leftOperand.getSize();
        value += SizeOf.SIZEOF_GOTO;
        value += SizeOf.SIZEOF_INTEGER;
        value += conditional.rightOperand.getSize();
        return value;
    }

    @Override
    public Node optimize() {
        if (expression.getNodeClass() == NodeClass.CONSTANT) {
            if (expression.getBooleanValue()) {
                return leftOperand.optimize();
            }
            return rightOperand.optimize();
        }
        return this;
    }
}
