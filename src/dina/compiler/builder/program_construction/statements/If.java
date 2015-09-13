package dina.compiler.builder.program_construction.statements;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;

public class If extends StatementContainer {

    private Node expression;
    private Block elseBlock;
    private int size = -1;

    public StatementContainer getElseBlock() {
        return elseBlock;
    }

    private static void init(Node expression) {
        if (expression == null) {
            throw new DinaException("Выражение на задано", DinaException.COMPILATION_ERROR);
        }
        if (expression.getNodeType().getId() == NodeType.T_BOOLEAN && expression.getNodeType().getDimension() == 0) {
            return;
        } else {
            throw new DinaException("Выражение должно иметь тип 'boolean', найдено: "
                    + expression.getNodeType(), DinaException.COMPILATION_ERROR);
        }
    }

    public If(Node expression) {
        init(expression);
        this.expression = expression;
        setStatementId(IF);
        elseBlock = new Block();
    }

    @Override
    public String trace() {
        String result = "if (" + expression.trace() + ") {\n";
        for (int i = 0; i < getNodesAmount(); i++) {
            result += getNode(i).trace() + (getNode(i) instanceof StackInspector ? ";" : "") + "\n";
        }
        result += "}";
        if (elseBlock.getNodesAmount() != 0) {
            result += " else {\n";
            for (int i = 0; i < elseBlock.getNodesAmount(); i++) {
                result += (elseBlock.getNode(i)).trace() + (elseBlock.getNode(i) instanceof StackInspector ? ";" : "") + "\n";
            }
            result += "}";
        }
        return result;
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compile0(If ifStmt) {
        int ifSize = getIfStmtSize(ifStmt);
        ifStmt.expression.compile();
        Output.writeByte(Constants.IFNE);
        Output.writeInteger(ifSize + ifStmt.getAddress());
        for (int nodeIndex = 0; nodeIndex < ifStmt.getNodesAmount(); nodeIndex++) {
            Node node = ifStmt.getNode(nodeIndex);
            node.compile();
        }
        if (ifStmt.elseBlock.getNodesAmount() != 0) {
            Output.writeByte(Constants.GOTO);
            Output.writeInteger(ifSize + ifStmt.elseBlock.getSize() + ifStmt.getAddress());
            ifStmt.elseBlock.compile();
        }
    }

    private static int getIfStmtSize(If ifStmt) {
        int value = ifStmt.expression.getSize();
        value += SizeOf.SIZEOF_IFNE;
        value += SizeOf.SIZEOF_INTEGER;
        for (int nodeIndex = 0; nodeIndex < ifStmt.getNodesAmount(); nodeIndex++) {
            Node node = ifStmt.getNode(nodeIndex);
            value += node.getSize();
        }
        if (ifStmt.elseBlock.getNodesAmount() != 0) {
            value += SizeOf.SIZEOF_GOTO;
            value += SizeOf.SIZEOF_INTEGER;
        }
        return value;
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getIfStmtSize(this) + elseBlock.getSize();
        }
        return size;
    }
}
