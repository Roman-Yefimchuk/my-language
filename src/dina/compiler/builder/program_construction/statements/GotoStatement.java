package dina.compiler.builder.program_construction.statements;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;

public class GotoStatement extends AbstractStatement {

    private Label label;
    private int positionInOutput;

    public GotoStatement() {
        setStatementId(GOTO);
    }

    public GotoStatement(Label label) {
        this();
        setLabel(label);
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    @Override
    public String trace() {
        return "goto " + label.getName() + ";";
    }

    @Override
    public void compile() {
        setAddress();
        positionInOutput = Output.getSize() - 1;
        compile0(this);
    }

    public static void compile0(GotoStatement gotoStmt) {
        if (gotoStmt.label.getLabelAddress() != -1) {
            Output.writeByte(Constants.GOTO);
            Output.writeInteger(gotoStmt.label.getLabelAddress());
        } else {
            Output.writeByte(Constants.GOTO);
            Output.writeInteger(-1);
            gotoStmt.label.getGotoAddress().push(new Integer(gotoStmt.positionInOutput));
        }
    }

    @Override
    public int getSize() {
        return SizeOf.SIZEOF_GOTO + SizeOf.SIZEOF_INTEGER;
    }
}
