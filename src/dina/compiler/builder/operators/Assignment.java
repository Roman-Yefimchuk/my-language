package dina.compiler.builder.operators;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;

public class Assignment extends Node {

    private Variable variable;
    private Node expression;
    private int size = -1;

    private static void init(Variable variable, Node expression) {
        if (expression == null) {
            throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
        }
        if (!NodeType.compare(variable, expression)) {
            Common.incomparableTypesException(variable, expression);
        }
    }

    public Assignment(Variable variable, Node expression, boolean checkDuplication) {
        init(variable, expression);
        this.variable = variable;
        this.expression = expression;
        if (checkDuplication) {
            canDuplicate(this);
        }
    }

    public Assignment(Variable variable, Node expression) {
        this(variable, expression, true);
    }

    @Override
    public NodeType getNodeType() {
        return variable.getNodeType();
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.ASSIGNMENT;
    }

    @Override
    public String trace() {
        return variable.trace() + " = " + expression.trace();
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compileExpression(Assignment assignment) {
        assignment.expression.compile();
        switch (assignment.expression.getNodeType().getId()) {
            case NodeType.T_CHAR:
            case NodeType.T_INT: {
                if (assignment.variable.getNodeType().getId() == NodeType.T_FLOAT) {
                    Output.writeByte(Constants.I2F);
                }
                break;
            }
        }
    }

    private static void compile0(Assignment assignment) {
        compileExpression(assignment);
        assignment.variable.assign();
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getExpressionSize(Assignment assignment) {
        int value = 0;
        value += assignment.expression.getSize();
        switch (assignment.expression.getNodeType().getId()) {
            case NodeType.T_CHAR:
            case NodeType.T_INT: {
                if (assignment.variable.getNodeType().getId() == NodeType.T_FLOAT) {
                    value += SizeOf.SIZEOF_I2F;
                }
                break;
            }
        }
        return value;
    }

    private static int getSize0(Assignment assignment) {
        int value = 0;
        value += getExpressionSize(assignment);
        value += assignment.variable.getAssignSize();
        return value;
    }

    private static void canDuplicate(Assignment assignment) {
        if (assignment.expression.getNodeClass() == NodeClass.ASSIGNMENT) {
            Assignment a = extractAssignment(assignment.expression);
            a.expression = new Duplicate(a.expression);
        }
    }

    private static Assignment extractAssignment(Node expression) {
        if (expression instanceof Brackets) {
            while (true) {
                expression = ((Brackets) expression).getExpression();
                if (expression instanceof Assignment) {
                    return (Assignment) expression;
                }
            }
        }
        return (Assignment) expression;
    }

    public Variable getVariable() {
        return variable;
    }

    public Node getExpression() {
        return expression;
    }
}
