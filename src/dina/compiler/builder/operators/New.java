package dina.compiler.builder.operators;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import java.util.*;

public class New extends Node {

    private int id;
    private ArrayList<Node> arguments;
    private int size = -1;

    public New(int id) {
        this.id = id;
    }

    public New(int id, ArrayList<Node> arguments) {
        this(id);
        if (!arguments.isEmpty()) {
            this.arguments = arguments;
        }
    }

    @Override
    public void compile() {
        compile0(this);
    }

    private static void compile0(New $new) {
        if ($new.arguments == null) {
            Output.writeByte(Constants.NEW);
            Output.writeByte(TypeContext.QUICK);
            Output.writeShort($new.id);
        } else {
            ArrayList<Node> arguments = $new.arguments;
            int argumentsAmount = arguments.size();
            for (int argumentIndex = argumentsAmount - 1; argumentIndex >= 0; argumentIndex--) {
                arguments.get(argumentIndex).compile();
            }
            Output.writeByte(Constants.NEW);
            Output.writeByte(TypeContext.FULL);
            Output.writeShort($new.id);
        }
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(New $new) {
        int value = 0;
        if ($new.arguments == null) {
            value += SizeOf.SIZEOF_NEW;
            value += SizeOf.SIZEOF_BYTE;
            value += SizeOf.SIZEOF_SHORT;
        } else {
            ArrayList<Node> arguments = $new.arguments;
            int argumentsAmount = arguments.size();
            for (int argumentIndex = 0; argumentIndex < argumentsAmount; argumentIndex++) {
                value += arguments.get(argumentIndex).getSize();
            }
            value += SizeOf.SIZEOF_NEW;
            value += SizeOf.SIZEOF_BYTE;
            value += SizeOf.SIZEOF_SHORT;
        }
        return value;
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.VALUE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.forNodeType(id);
    }

    @Override
    public String trace() {
        String s = "new " + NodeType.forName(id);
        if (arguments == null) {
            s += "()";
            return s;
        } else {
            s += "(";
            for (int i = 0; i < arguments.size(); i++) {
                s += arguments.get(i).trace() + (i == arguments.size() - 1 ? "" : ", ");
            }
            s += ")";
        }
        return s;
    }
}
