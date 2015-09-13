package dina.compiler.builder.objects.constants;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;

public class BooleanConstant extends Constant {

    public static final int TRUE = 1;
    public static final int FALSE = 0;
    private boolean booleanValue;

    public BooleanConstant(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    @Override
    public String trace() {
        return String.valueOf(booleanValue);
    }

    public static void compile0(BooleanConstant booleanConstant) {
        Output.writeByte(Constants.ZCONST);
        if (booleanConstant.booleanValue) {
            Output.writeByte(TRUE);
        } else {
            Output.writeByte(FALSE);
        }
    }

    @Override
    public void compile() {
        compile0(this);
    }

    @Override
    public int getSize() {
        return SizeOf.SIZEOF_BCONST + SizeOf.SIZEOF_BYTE;
    }

    @Override
    public boolean getBooleanValue() {
        return booleanValue;
    }
}
