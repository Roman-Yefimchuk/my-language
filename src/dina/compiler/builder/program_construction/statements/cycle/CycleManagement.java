package dina.compiler.builder.program_construction.statements.cycle;

import dina.*;
import dina.compiler.*;
import dina.compiler.parser.*;
import dina.compiler.builder.*;
import dina.compiler.builder.program_construction.statements.*;

public class CycleManagement extends AbstractStatement {

    public static final int BREAK = ParserConstants.BREAK;
    public static final int CONTINUE = ParserConstants.CONTINUE;
    private int cycleManagementType;
    private StatementContainer cycleContainer;
    private int size = -1;

    public CycleManagement(int cycleManagementType) {
        this.cycleManagementType = cycleManagementType;
    }

    public void setCycleContainer(StatementContainer cycleContainer) {
        this.cycleContainer = cycleContainer;
    }

    public int getCycleManagementType() {
        return cycleManagementType;
    }

    @Override
    public String trace() {
        return ParserConstants.keyWords[cycleManagementType] + ";";
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(CycleManagement cycleManagement) {
        int value = SizeOf.SIZEOF_GOTO + SizeOf.SIZEOF_INTEGER;
        int count = amountOf(TRY, cycleManagement);
        if (count != 0) {
            if (deepOf(TRY, cycleManagement) < deepOf(cycleManagement.cycleContainer.getStatementId(), cycleManagement)) {
                value += SizeOf.SIZEOF_QUIT * count;
            }
        }
        return value;
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compile0(CycleManagement cycleManagement) {
        StatementContainer cycleContainer = cycleManagement.cycleContainer;
        int count = amountOf(TRY, cycleManagement);
        if (count != 0) {
            if (deepOf(TRY, cycleManagement) < deepOf(cycleContainer.getStatementId(), cycleManagement)) {
                for (int countIndex = 0; countIndex < count; countIndex++) {
                    Output.writeByte(Constants.QUIT);
                }
            }
        }
        switch (cycleManagement.cycleManagementType) {
            case CONTINUE: {
                Output.writeByte(Constants.GOTO);
                Output.writeInteger(cycleContainer.getBeginAddress());
                break;
            }
            case BREAK: {
                Output.writeByte(Constants.GOTO);
                Output.writeInteger(cycleContainer.getEndAddress());
                break;
            }
        }
    }
}
