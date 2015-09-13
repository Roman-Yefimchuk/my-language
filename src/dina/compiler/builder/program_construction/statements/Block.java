package dina.compiler.builder.program_construction.statements;

import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;

public class Block extends StatementContainer {

    private int size = -1;

    public Block(int statementType) {
        setStatementId(statementType);
    }

    public Block() {
        this(BLOCK);
    }

    @Override
    public String trace() {
        String result = "{\n";
        for (int i = 0; i < getNodesAmount(); i++) {
            result += getNode(i).trace() + (getNode(i) instanceof StackInspector ? ";" : "") + "\n";
        }
        return result + "}\n";
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compile0(Block block) {
        for (int atomIndex = 0; atomIndex < block.getNodesAmount(); atomIndex++) {
            Node atom = block.getNode(atomIndex);
            atom.compile();
        }
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(Block block) {
        int value = 0;
        for (int atomIndex = 0; atomIndex < block.getNodesAmount(); atomIndex++) {
            Node atom = block.getNode(atomIndex);
            value += atom.getSize();
        }
        return value;
    }
}
