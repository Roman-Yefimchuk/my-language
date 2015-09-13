package dina.compiler.builder.operators;

import dina.Constants;
import dina.compiler.Output;
import dina.compiler.builder.*;

public class TypeConversion extends Node {

    private NodeType type;
    private Node object;
    private int size = -1;

    private static void init(NodeType type, Node object) {
    }

    public TypeConversion(NodeType type, Node object) {
        init(type, object);
        this.type = type;
        this.object = object;
    }

    @Override
    public void compile() {
        compile0(this);
    }

    private static void compile0(TypeConversion typeConversion) {
        typeConversion.object.compile();
        Output.writeByte(Constants.CHECKCAST);
        int id = typeConversion.type.getId();
        int dimension = typeConversion.type.getDimension();
        Output.writeShort(id);
        Output.writeByte(dimension);
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(TypeConversion typeConversion) {
        int value = 0;
        value += typeConversion.object.getSize();
        value += SizeOf.SIZEOF_TYPEOF;
        value += SizeOf.SIZEOF_SHORT;
        value += SizeOf.SIZEOF_BYTE;
        return value;
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.VALUE;
    }

    @Override
    public NodeType getNodeType() {
        return type;
    }
}
