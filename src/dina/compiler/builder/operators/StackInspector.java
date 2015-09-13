package dina.compiler.builder.operators;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;

public class StackInspector extends Node {

    private Node node;
    private int size = -1;

    public StackInspector(Node node) {
        this.node = node;
    }

    @Override
    public NodeType getNodeType() {
        return node.getNodeType();
    }

    @Override
    public NodeClass getNodeClass() {
        return node.getNodeClass();
    }

    @Override
    public String trace() {
        return node.trace();
    }

    @Override
    public void compile() {
        compile0(this);
    }

    private static void compile0(StackInspector stackInspector) {
        stackInspector.node.compile();
        if (stackInspector.node.getNodeType().getId() == NodeType.T_VOID) {
            return;
        }
        Output.writeByte(Constants.POP);
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(StackInspector stackInspector) {
        int value = 0;
        value += stackInspector.node.getSize();
        if (stackInspector.node.getNodeType().getId() != NodeType.T_VOID) {
            value += SizeOf.SIZEOF_BYTE;
        }
        return value;
    }
}
