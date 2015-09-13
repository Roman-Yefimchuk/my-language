package dina.compiler.builder.program_construction;

import java.util.*;
import dina.compiler.builder.*;

public class InternalFunctionPointer extends Node {

    private InternalFunctionContainer functionContainer;
    private ArrayList<Node> arguments;

    public InternalFunctionPointer(InternalFunctionContainer functionContainer, ArrayList<Node> arguments) {
        this.functionContainer = functionContainer;
        this.arguments = arguments;
    }

    @Override
    public void compile() {
        setAddress();
        functionContainer.setFunctionPointer(this);
        functionContainer.compile();
    }

    public ArrayList<Node> getArguments() {
        return arguments;
    }

    @Override
    public int getSize() {
        functionContainer.setFunctionPointer(this);
        return functionContainer.getSize();
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.VALUE;
    }

    @Override
    public NodeType getNodeType() {
        return functionContainer.getNodeType();
    }
}
