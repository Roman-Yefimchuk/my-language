package dina.compiler.builder;

public class Brackets extends Node {

    private Node expression;
    private int size = -1;

    public Brackets(Node expression) {
        this.expression = expression;
    }

    @Override
    public NodeType getNodeType() {
        return expression.getNodeType();
    }

    @Override
    public NodeClass getNodeClass() {
        return expression.getNodeClass();
    }

    @Override
    public String trace() {
        return "(" + expression.trace() + ")";
    }

    @Override
    public void compile() {
        expression.compile();
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = expression.getSize();
        }
        return size;
    }

    public Node getExpression() {
        return expression;
    }

    @Override
    public boolean getBooleanValue() {
        return expression.getBooleanValue();
    }

    @Override
    public int getIntegerValue() {
        return expression.getIntegerValue();
    }

    @Override
    public float getFloatValue() {
        return expression.getFloatValue();
    }

    @Override
    public String getStringValue() {
        return expression.getStringValue();
    }

    @Override
    public Node optimize() {
        return expression.optimize();
    }
}
