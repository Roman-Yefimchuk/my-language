package dina.compiler.builder.program_construction.statements.cycle;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;

public class For extends Cycle {

    private Node forInit;
    private Node expression;
    private Node forUpdate;
    private int size = -1;

    private static void init(Node forInit, Node expression, Node forUpdate) {
        if (expression != null) {
            if (expression.getNodeType().getId() == NodeType.T_BOOLEAN && expression.getNodeType().getDimension() == 0) {
                return;
            } else {
                throw new DinaException("Выражение должно иметь тип 'boolean', найдено: "
                        + expression.getNodeType(), DinaException.COMPILATION_ERROR);
            }
        }
    }

    public For(Node forInit, Node expression, Node forUpdate) {
        init(forInit, expression, forUpdate);
        this.forInit = forInit;
        this.expression = expression;
        this.forUpdate = forUpdate;
        setStatementId(FOR);
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compile0(For forStmt) {
        if (forStmt.forInit != null) {
            forStmt.forInit.compile();
        }
        if (forStmt.expression != null) {
            forStmt.expression.compile();
        } else {
            Output.writeByte(Constants.BCONST);
            Output.writeByte(1);
        }
        Output.writeByte(Constants.IFNE);
        Output.writeInteger(forStmt.getEndAddress());
        for (int nodeIndex = 0; nodeIndex < forStmt.getNodesAmount(); nodeIndex++) {
            Node node = forStmt.getNode(nodeIndex);
            node.compile();
        }
        if (forStmt.forUpdate != null) {
            forStmt.forUpdate.compile();
        }
        Output.writeByte(Constants.GOTO);
        Output.writeInteger(forStmt.getBeginAddress());
    }

    @Override
    public int getBeginAddress() {
        return getAddress() + (forInit == null ? 0 : forInit.getSize());
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(For forStmt) {
        int value = 0;
        value += forStmt.forInit == null ? 0 : forStmt.forInit.getSize();
        value += forStmt.expression == null ? SizeOf.SIZEOF_BCONST + SizeOf.SIZEOF_BYTE : forStmt.expression.getSize();
        value += SizeOf.SIZEOF_IFNE;
        value += SizeOf.SIZEOF_INTEGER;
        for (int nodeIndex = 0; nodeIndex < forStmt.getNodesAmount(); nodeIndex++) {
            Node node = forStmt.getNode(nodeIndex);
            value += node.getSize();
        }
        value += forStmt.forUpdate == null ? 0 : forStmt.forUpdate.getSize();
        value += SizeOf.SIZEOF_GOTO;
        value += SizeOf.SIZEOF_INTEGER;
        return value;
    }

    @Override
    public String trace() {
        String result = "for(" + (forInit == null ? "" : forInit.trace())
                + "; " + (expression == null ? "" : expression.trace())
                + "; " + (forUpdate == null ? "" : forUpdate.trace()) + ") {\n";
        for (int i = 0; i < getNodesAmount(); i++) {
            result += getNode(i).trace() + (getNode(i) instanceof StackInspector ? ";" : "") + "\n";
        }
        return result + "\n}";
    }
}
