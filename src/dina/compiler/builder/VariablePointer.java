package dina.compiler.builder;

import dina.compiler.builder.objects.*;

public class VariablePointer {

    private int variableIndex;
    private Variable variable;

    public VariablePointer(Variable variable, int variableIndex) {
        this.variable = variable;
        this.variableIndex = variableIndex;
    }

    public int getVariableIndex() {
        return variableIndex;
    }

    public Variable getVariable() {
        return variable;
    }
}
