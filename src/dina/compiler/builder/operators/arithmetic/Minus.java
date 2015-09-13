package dina.compiler.builder.operators.arithmetic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.constants.*;

public class Minus extends Node {

    private Node expression;
    private int size = -1;

    private static void init(Node expression) {
        if (NodeType.isNumeric(expression) && expression.getNodeType().getDimension() == 0) {
            return;
        }
        throw new DinaException("Оператор - не может быть применен к '"
                + expression.getNodeType() + "'", DinaException.COMPILATION_ERROR);
    }

    public Minus(Node expression) {
        init(expression);
        this.expression = expression;
    }

    @Override
    public NodeType getNodeType() {
        return expression.getNodeType().getId() == NodeType.T_CHAR ? NodeType.TYPE_INT : expression.getNodeType();
    }

    @Override
    public String trace() {
        return "-" + expression.trace();
    }

    private static void compile0(Minus minus) {
        minus.expression.compile();
        switch (minus.expression.getNodeType().getId()) {
            case NodeType.T_FLOAT: {
                Output.writeByte(Constants.FNEG);
                break;
            }
            case NodeType.T_CHAR:
            case NodeType.T_INT: {
                Output.writeByte(Constants.INEG);
                break;
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
            size = expression.getSize() + SizeOf.SIZEOF_BYTE;
        }
        return size;
    }

    @Override
    public NodeClass getNodeClass() {
        return expression.getNodeClass();
    }

    @Override
    public Node optimize() {
        if (expression.getNodeClass() == NodeClass.CONSTANT) {
            NumberConstant numberConstant = Common.extractNumberConstant(expression);
            return numberConstant.getNegativeValue();
        }
        return this;
    }
}
