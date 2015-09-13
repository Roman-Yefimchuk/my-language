package dina.compiler.builder.operators.logic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.constants.*;

public class Not extends Node {

    private Node expression;
    private int size = -1;

    private static void init(Node operand) {
        if (operand.getNodeType().getId() == NodeType.T_BOOLEAN && operand.getNodeType().getDimension() == 0) {
            return;
        }
        throw new DinaException("Оператор ! не может быть применен к '"
                + operand.getNodeType() + "'", DinaException.COMPILATION_ERROR);
    }

    public Not(Node operand) {
        init(operand);
        this.expression = operand;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    @Override
    public NodeClass getNodeClass() {
        return expression.getNodeClass();
    }

    private static void compile0(Not not) {
        not.expression.compile();
        Output.writeByte(Constants.NOT);
    }

    @Override
    public void compile() {
        compile0(this);
    }

    @Override
    public String trace() {
        return "!" + expression.trace();
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = expression.getSize() + SizeOf.SIZEOF_NOT;
        }
        return size;
    }

    @Override
    public Node optimize() {
        if (expression.getNodeClass() == NodeClass.CONSTANT) {
            return new BooleanConstant(!expression.getBooleanValue());
        }
        return this;
    }
}
