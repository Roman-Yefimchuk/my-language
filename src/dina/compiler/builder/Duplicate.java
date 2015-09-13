package dina.compiler.builder;

import dina.*;
import dina.compiler.*;

public class Duplicate extends Node {

    private Node target;

    public Duplicate(Node target) {
        this.target = target;
    }

    @Override
    public void compile() {
        target.compile();
        Output.writeByte(Constants.DUP);
    }

    @Override
    public int getSize() {
        return target.getSize() + SizeOf.SIZEOF_DUP;
    }

    @Override
    public NodeClass getNodeClass() {
        return target.getNodeClass();
    }

    @Override
    public NodeType getNodeType() {
        return target.getNodeType();
    }

    @Override
    public String trace() {
        return target.trace();
    }
}
