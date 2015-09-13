package dina.compiler.builder.program_construction.statements;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;

public class Return extends AbstractStatement {

    private Node value;
    private int size = -1;

    public Return(Node value) {
        setStatementId(RETURN);
        this.value = value;
    }

    @Override
    public String trace() {
        return "return" + (value == null ? "" : " " + value.trace()) + ";";
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compile0(Return returnStmt) {
        if (returnStmt.value != null) {
            returnStmt.value.compile();
            switch (returnStmt.value.getNodeType().getId()) {
                case NodeType.T_CHAR:
                case NodeType.T_INT: {
                    if (returnStmt.getParentContainer().getFunction().getNodeType().getId() == NodeType.T_FLOAT) {
                        Output.writeByte(Constants.I2F);
                    }
                    break;
                }
            }
        }
        int count = amountOf(TRY, returnStmt);
        for (int countIndex = 0; countIndex < count; countIndex++) {
            Output.writeByte(Constants.QUIT);
        }
        Output.writeByte(Constants.RETURN);
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(Return returnStmt) {
        int value = 0;
        if (returnStmt.value != null) {
            value += returnStmt.value.getSize();
            switch (returnStmt.value.getNodeType().getId()) {
                case NodeType.T_CHAR:
                case NodeType.T_INT: {
                    if (returnStmt.getParentContainer().getFunction().getNodeType().getId() == NodeType.T_FLOAT) {
                        value += SizeOf.SIZEOF_I2F;
                    }
                    break;
                }
            }
        }
        int count = amountOf(TRY, returnStmt);
        value += SizeOf.SIZEOF_QUIT * count;
        value += SizeOf.SIZEOF_RETURN;
        return value;
    }
}
