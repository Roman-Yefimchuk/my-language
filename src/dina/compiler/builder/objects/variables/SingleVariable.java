package dina.compiler.builder.objects.variables;

import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;
import dina.compiler.builder.program_construction.*;

public class SingleVariable extends Variable {

    private int type;

    public SingleVariable(String variableName, Body parentBody, int variableType) {
        super(variableName, parentBody);
        this.type = variableType;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.getType(type);
    }
}
