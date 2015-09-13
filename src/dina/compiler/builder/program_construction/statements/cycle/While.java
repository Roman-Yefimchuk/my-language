package dina.compiler.builder.program_construction.statements.cycle;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;

public class While extends Cycle {

    private Node expression;
    private int size = -1;

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

    public While(Node expression) {
        init(expression);
        this.expression = expression;
        setStatementId(WHILE);
    }

    @Override
    public String trace() {
        String result = "while(" + expression.trace() + "){\n";
        for (int i = 0; i < getNodesAmount(); i++) {
            result += getNode(i).trace() + (getNode(i) instanceof StackInspector ? ";" : "") + "\n";
        }
        return result + "}";
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compile0(While whileStmt) {
        whileStmt.expression.compile();
        Output.writeByte(Constants.IFNE);
        Output.writeInteger(whileStmt.getEndAddress());
        for (int nodeIndex = 0; nodeIndex < whileStmt.getNodesAmount(); nodeIndex++) {
            whileStmt.getNode(nodeIndex).compile();
        }
        Output.writeByte(Constants.GOTO);
        Output.writeInteger(whileStmt.getBeginAddress());
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(While whileStmt) {
        int value = whileStmt.expression.getSize();
        value += SizeOf.SIZEOF_IFNE;
        value += SizeOf.SIZEOF_INTEGER;
        for (int nodeIndex = 0; nodeIndex < whileStmt.getNodesAmount(); nodeIndex++) {
            value += whileStmt.getNode(nodeIndex).getSize();
        }
        value += SizeOf.SIZEOF_GOTO;
        value += SizeOf.SIZEOF_INTEGER;
        return value;
    }
}
