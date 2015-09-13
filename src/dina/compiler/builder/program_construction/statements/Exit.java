package dina.compiler.builder.program_construction.statements;

import dina.Constants;
import dina.compiler.Output;
import dina.compiler.builder.SizeOf;

public class Exit extends AbstractStatement {

    private Switch switchContainer;
    private int size = -1;

    public Exit() {
        setStatementId(EXIT);
    }

    public void setSwitchContainer(Switch switchContainer) {
        this.switchContainer = switchContainer;
    }

    @Override
    public void compile() {
        compile0(this);
    }

    private static void compile0(Exit exit) {
        Switch switchContainer = exit.switchContainer;
        int count = amountOf(TRY, exit);
        if (count != 0) {
            if (deepOf(TRY, exit) < deepOf(SWITCH, exit)) {
                for (int countIndex = 0; countIndex < count; countIndex++) {
                    Output.writeByte(Constants.QUIT);
                }
            }
        }
        Output.writeByte(Constants.GOTO);
        Output.writeInteger(switchContainer.getEndAddress());
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(Exit exit) {
        int value = SizeOf.SIZEOF_GOTO + SizeOf.SIZEOF_INTEGER;
        int count = amountOf(TRY, exit);
        if (count != 0) {
            if (deepOf(TRY, exit) < deepOf(SWITCH, exit)) {
                value += SizeOf.SIZEOF_QUIT * count;
            }
        }
        return value;
    }
}
