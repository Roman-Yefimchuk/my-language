package dina.compiler.builder.operators;

import dina.Constants;
import dina.compiler.Output;
import dina.compiler.builder.*;

public class TypeOfNE extends Node {

    private Node object;
    private NodeType type;
    private int size = -1;

    public TypeOfNE(Node object, NodeType type) {
        this.object = object;
        this.type = type;
    }

    @Override
    public void compile() {
        compile0(this);
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.VALUE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.TYPE_BOOLEAN;
    }

    private static void compile0(TypeOfNE typeOfNE) {
        typeOfNE.object.compile();
        Output.writeByte(Constants.TYPEOF);
        int classId = typeOfNE.type.getId() - 1;
        int arrayDepth = typeOfNE.type.getDimension();
        Output.writeShort(classId);
        Output.writeByte(arrayDepth);
        Output.writeByte(Constants.NOT);
    }

    private static int getSize0(TypeOfNE typeOfNE) {
        int value = 0;
        value += typeOfNE.object.getSize();
        value += SizeOf.SIZEOF_TYPEOF;
        value += SizeOf.SIZEOF_SHORT;
        value += SizeOf.SIZEOF_BYTE;
        value += SizeOf.SIZEOF_NOT;
        return value;
    }
}
