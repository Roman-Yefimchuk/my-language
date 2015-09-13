package dina.compiler.builder;

import dina.*;
import dina.compiler.*;

public class StringLength extends Node {

    private Node target;
    private int size = -1;

    public StringLength(Node target) {
        this.target = Common.checkNode(target);
    }

    @Override
    public void compile() {
        target.compile();
        Output.writeByte(Constants.STRINGLENGTH);
    }

    private static int getSize0(StringLength stringLength) {
        int value = stringLength.target.getSize();
        value += SizeOf.SIZEOF_STRINGLENGTH;
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
