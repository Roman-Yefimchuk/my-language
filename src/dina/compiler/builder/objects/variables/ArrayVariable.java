package dina.compiler.builder.objects.variables;

import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;
import dina.compiler.builder.program_construction.*;

public class ArrayVariable extends Variable {

    private NodeType type;

    public ArrayVariable(String variableName, Body parentBody, NodeType arrayType) {
        super(variableName, parentBody);
        this.type = NodeType.getPrototype(arrayType.getId(), 1);
    }

    @Override
    public NodeType getNodeType() {
        return type;
    }

    @Override
    public String trace() {
        return getName();
    }
}
