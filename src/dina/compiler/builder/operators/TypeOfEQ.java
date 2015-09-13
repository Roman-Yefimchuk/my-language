package dina.compiler.builder.operators;

import dina.Constants;
import dina.compiler.Output;
import dina.compiler.builder.*;

public class TypeOfEQ extends Node {

    private Node object;
    private NodeType type;
    private int size = -1;

    public TypeOfEQ(Node object, NodeType type) {
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

    private static void compile0(TypeOfEQ typeOfEQ) {
        typeOfEQ.object.compile();
        Output.writeByte(Constants.TYPEOF);
        int classId = typeOfEQ.type.getId() - 1;
        int arrayDepth = typeOfEQ.type.getDimension();
        Output.writeShort(classId);
        Output.writeByte(arrayDepth);
    }

    private static int getSize0(TypeOfEQ typeOfEQ) {
        int value = 0;
        value += typeOfEQ.object.getSize();
        value += SizeOf.SIZEOF_TYPEOF;
        value += SizeOf.SIZEOF_SHORT;
        value += SizeOf.SIZEOF_BYTE;
        return value;
    }
}
