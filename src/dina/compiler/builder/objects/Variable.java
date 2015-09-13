package dina.compiler.builder.objects;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.program_construction.*;

public class Variable extends Node {

    private Body parentBody;
    private String name;
    private Node value;

    public Variable() {
    }

    public Variable(Body parentBody) {
        this.parentBody = parentBody;
    }

    public Variable(String variableName) {
        this.name = variableName;
    }

    public Variable(String variableName, Body parentBody) {
        this.name = variableName;
        this.parentBody = parentBody;
    }

    public Body getParentBody() {
        return parentBody;
    }

    public String getName() {
        return name;
    }

    public void setValue(Node value) {
        this.value = value;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.VARIABLE;
    }

    @Override
    public String trace() {
        return name;
    }

    public static void assign0(Variable variable) {
        Body body = variable.parentBody;
        Output.writeByte(body.isGlobalVariables() ? Constants.GSTORE : Constants.LSTORE);
        Output.writeShort(body.getVariableIndex(variable.name));
    }

    public static void compile0(Variable variable) {
        Body body = variable.parentBody;
        Output.writeByte(body.isGlobalVariables() ? Constants.GLOAD : Constants.LLOAD);
        Output.writeShort(body.getVariableIndex(variable.name));
    }

    public void assign() {
        assign0(this);
    }

    @Override
    public void compile() {
        compile0(this);
    }

    public int getAssignSize() {
        return SizeOf.SIZEOF_BYTE + SizeOf.SIZEOF_SHORT;
    }

    @Override
    public int getSize() {
        return SizeOf.SIZEOF_BYTE + SizeOf.SIZEOF_SHORT;
    }

    public boolean isRecord() {
        return false;
    }
}
