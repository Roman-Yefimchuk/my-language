package dina.compiler.builder.program_construction.statements.cycle;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.program_construction.statements.*;

public class Do extends Cycle {

    private Node expression;
    private int size = -1;

    public Do() {
        setStatementId(DO);
    }

    public static void setExpression(Do doBlock, Node expression) {
        if (expression == null) {
            throw new DinaException("Выражение на задано", DinaException.COMPILATION_ERROR);
        }
        if (expression.getNodeType().getId() == NodeType.T_BOOLEAN && expression.getNodeType().getDimension() == 0) {
            doBlock.expression = expression;
        } else {
            throw new DinaException("Выражение должно иметь тип 'boolean', найдено: "
                    + expression.getNodeType(), DinaException.COMPILATION_ERROR);
        }
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compile0(Do doStmt) {
        for (int nodeIndex = 0; nodeIndex < doStmt.getNodesAmount(); nodeIndex++) {
            ((Node) doStmt.getNode(nodeIndex)).compile();
        }
        doStmt.expression.compile();
        Output.writeByte(Constants.IFEQ);
        Output.writeInteger(doStmt.getBeginAddress());
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(Do doStmt) {
        int value = 0;
        for (int nodeIndex = 0; nodeIndex < doStmt.getNodesAmount(); nodeIndex++) {
            value += ((Node) doStmt.getNode(nodeIndex)).getSize();
        }
        value += doStmt.expression.getSize();
        value += SizeOf.SIZEOF_IFEQ;
        value += SizeOf.SIZEOF_INTEGER;
        return value;
    }

    @Override
    public String trace() {
        String result = "do {\n";
        for (int i = 0; i < getNodesAmount(); i++) {
            result += ((Node) getNode(i)).trace() + (getNode(i) instanceof StackInspector ? ";" : "") + "\n";
        }
        return result + "} while (" + expression.trace() + ");";
    }

    @Override
    public Node optimize() {
        if (expression.getNodeClass() == NodeClass.CONSTANT) {
            int amount = getNodesAmount();
            Block block = new Block();
            block.setParentContainer(getParentContainer());
            block.setFunction(getFunction());
            for (int nodeIndex = 0; nodeIndex < amount; nodeIndex++) {
                block.addNode(getNode(nodeIndex));
            }
            if (expression.getBooleanValue()) {
                Label label = new Label("$" + nextValue());
                block.insertNode(new LabelStatement(label), 0);
                block.addNode(new GotoStatement(label));
            }
            return block;
        }
        return this;
    }
}
