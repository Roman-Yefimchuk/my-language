package dina.compiler.builder;

import dina.*;
import dina.compiler.*;

public class ArrayLength extends Node {

    private Node target;
    private int size = -1;

    public ArrayLength(Node target) {
        this.target = Common.checkNode(target);
    }

    @Override
    public void compile() {
        target.compile();
        Output.writeByte(Constants.ARRAYLENGTH);
        Output.writeShort(target.getNodeType().getId());
    }

    private static int getSize0(ArrayLength arrayLength) {
        int value = arrayLength.target.getSize();
        value += SizeOf.SIZEOF_ARRAYLENGTH;
        value += SizeOf.SIZEOF_SHORT;
        return value;
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
        return NodeType.TYPE_INT;
    }
}
