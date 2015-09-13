package dina.compiler.builder.program_construction.statements;

import dina.compiler.*;
import dina.compiler.builder.*;

public class LabelStatement extends AbstractStatement {

    private Label label;

    public LabelStatement(Label label) {
        this.label = label;
        setStatementId(LABEL);
    }

    @Override
    public String trace() {
        return label.getName() + ":";
    }

    @Override
    public void compile() {
        compile0(this);
    }

    private static void compile0(LabelStatement labelStmt) {
        labelStmt.label.setLabelAddress(Output.getCurrentAddress() + 1);
        if (!labelStmt.label.getGotoAddress().empty()) {
            while (!labelStmt.label.getGotoAddress().empty()) {
                int gotoAddress = ((Integer) labelStmt.label.getGotoAddress().pop()).intValue();
                Output.setAddress(gotoAddress + 1, labelStmt.label.getLabelAddress());
            }
        }
    }
}
