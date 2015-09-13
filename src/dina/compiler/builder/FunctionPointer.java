package dina.compiler.builder;

import java.util.*;
import dina.*;
import dina.compiler.*;
import dina.compiler.builder.program_construction.*;

public class FunctionPointer extends Node {

    private Function function;
    private ArrayList<Node> arguments;
    public static Stack forwardFunctions = new Stack();
    private int size = -1;

    public FunctionPointer(Function function, ArrayList<Node> arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    public Function getFunction() {
        return function;
    }

    @Override
    public NodeType getNodeType() {
        return function.getNodeType();
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.VALUE;
    }

    @Override
    public String trace() {
        String args = "";
        for (int i = 0; i < arguments.size(); i++) {
            args += (arguments.get(i)).trace() + (arguments.size() - 1 == i ? "" : ", ");
        }
        return function.getName() + "(" + args + ")";
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compileArguments(FunctionPointer functionPointer) {
        for (int argumentIndex = functionPointer.arguments.size() - 1; argumentIndex >= 0; argumentIndex--) {
            Node node = functionPointer.arguments.get(argumentIndex);
            node.compile();
            if (functionPointer.function.getArguments()[argumentIndex].getNodeType().getId() == NodeType.T_FLOAT) {
                switch (node.getNodeType().getId()) {
                    case NodeType.T_CHAR:
                    case NodeType.T_INT: {
                        Output.writeByte(Constants.I2F);
                        break;
                    }
                }
            }
        }
    }

    private static int getArgumentsSize(FunctionPointer functionPointer) {
        int value = 0;
        for (int argumentIndex = 0; argumentIndex < functionPointer.arguments.size(); argumentIndex++) {
            Node node = functionPointer.arguments.get(argumentIndex);
            value += node.getSize();
            if (functionPointer.function.getArguments()[argumentIndex].getNodeType().getId() == NodeType.T_FLOAT) {
                switch (node.getNodeType().getId()) {
                    case NodeType.T_CHAR:
                    case NodeType.T_INT: {
                        value += SizeOf.SIZEOF_I2F;
                        break;
                    }
                }
            }
        }
        return value;
    }

    private static void compileNativeFunction(FunctionPointer functionPointer) {
        Output.writeByte(Constants.INVOKENATIVE);
        Output.writeByte(functionPointer.function.getLibraryId());
        Output.writeByte(functionPointer.function.getFunctionId());
    }

    private static void compileCustomFunction(FunctionPointer functionPointer) {
        int positionInOutput = Output.getSize() - 1;
        Output.writeByte(Constants.INVOKE);
        int functionAddress = functionPointer.function.getAddress();
        Output.writeInteger(functionAddress);
        if (functionAddress == -1) {
            forwardFunctions.push(new Object[]{functionPointer.function, new Integer(positionInOutput)});
        }
    }

    private static void compile0(FunctionPointer functionPointer) {
        compileArguments(functionPointer);
        if (functionPointer.function.isNativeFunction()) {
            compileNativeFunction(functionPointer);
        } else {
            compileCustomFunction(functionPointer);
        }
    }

    private static int getNativeFunctioSize() {
        int value = 0;
        value += SizeOf.SIZEOF_INVOKENATIVE;
        value += SizeOf.SIZEOF_BYTE;
        value += SizeOf.SIZEOF_BYTE;
        return value;
    }

    private static int getCustomFunctioSize() {
        int value = 0;
        value += SizeOf.SIZEOF_INVOKE;
        value += SizeOf.SIZEOF_INTEGER;
        return value;
    }

    private static int getSize0(FunctionPointer functionPointer) {
        int value = 0;
        value += getArgumentsSize(functionPointer);
        if (functionPointer.function.isNativeFunction()) {
            value += getNativeFunctioSize();
        } else {
            value += getCustomFunctioSize();
        }
        return value;
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }
}
