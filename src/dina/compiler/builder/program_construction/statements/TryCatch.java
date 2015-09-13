package dina.compiler.builder.program_construction.statements;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;

public class TryCatch extends StatementContainer {

    private StatementContainer tryBlock;
    private StatementContainer catchBlock;
    private int size = -1;

    public TryCatch(StatementContainer tryBlock, StatementContainer catchBlock) {
        this.tryBlock = tryBlock;
        this.catchBlock = catchBlock;
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    public StatementContainer getTryContainer() {
        return tryBlock;
    }

    public StatementContainer getCatchContainer() {
        return catchBlock;
    }

    private static void compile0(TryCatch tryCatchContainer) {
        Output.writeByte(Constants.TRY);
        Output.writeInteger(getTrySize(tryCatchContainer) + tryCatchContainer.getAddress());
        tryCatchContainer.tryBlock.compile();
        Output.writeByte(Constants.BREAK_TRY);
        Output.writeInteger(tryCatchContainer.getSize() + tryCatchContainer.getAddress());
        tryCatchContainer.catchBlock.compile();
    }

    private static int getTrySize(TryCatch tryCatchContainer) {
        int value = 0;
        value += SizeOf.SIZEOF_TRY;
        value += SizeOf.SIZEOF_INTEGER;
        for (int nodeIndex = 0; nodeIndex < tryCatchContainer.tryBlock.getNodesAmount(); nodeIndex++) {
            Node node = tryCatchContainer.tryBlock.getNode(nodeIndex);
            value += node.getSize();
        }
        value += SizeOf.SIZEOF_BREAK_TRY;
        value += SizeOf.SIZEOF_INTEGER;
        return value;
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(TryCatch tryCatch) {
        return getTrySize(tryCatch) + tryCatch.catchBlock.getSize();
    }
}
