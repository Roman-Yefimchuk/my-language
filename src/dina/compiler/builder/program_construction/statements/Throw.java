package dina.compiler.builder.program_construction.statements;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;

public class Throw extends AbstractStatement {

    private Node exceptionMessage;
    private int size = -1;

    public Throw(Node exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public void compile() {
        setAddress();
        exceptionMessage.compile();
        Output.writeByte(Constants.THROW);
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = SizeOf.SIZEOF_THROW + exceptionMessage.getSize();
        }
        return size;
    }
}
