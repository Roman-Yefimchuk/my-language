package dina.compiler.builder.program_construction;

import java.util.*;
import dina.compiler.builder.*;

public abstract class InternalFunctionContainer {

    private String name;
    private NodeType functionType;
    private ArrayList<Node> arguments;
    private InternalFunctionPointer functionPointer;

    public InternalFunctionContainer(String name, NodeType functionType) {
        this.name = name;
        this.functionType = functionType;
    }

    public void setFunctionPointer(InternalFunctionPointer functionPointer) {
        this.functionPointer = functionPointer;
    }

    public InternalFunctionPointer getFunctionPointer() {
        return functionPointer;
    }

    public void setArguments() {
        this.arguments = functionPointer.getArguments();
    }

    public Node getArgument(int index) {
        return arguments.get(index);
    }

    public String getName() {
        return name;
    }

    public NodeType getNodeType() {
        return functionType;
    }

    public abstract boolean check(Argument[] arguments);

    public abstract int getSize();

    public abstract void compile();

    public static Argument[] toArray(ArrayList<Node> arguments) {
        Argument[] result = new Argument[arguments.size()];
        for (int argumentIndex = 0; argumentIndex < result.length; argumentIndex++) {
            Node node = arguments.get(argumentIndex);
            result[argumentIndex] = new Argument(node.getNodeType(), node.getNodeType().getDimension());
        }
        return result;
    }
}
