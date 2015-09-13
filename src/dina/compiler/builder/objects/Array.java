package dina.compiler.builder.objects;

import java.util.*;
import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.constants.*;
import dina.compiler.builder.program_construction.*;

public class Array extends Variable {

    private NodeType type;
    private Node[] nodes;
    private Node length;
    private int size = -1;

    public Array(NodeType arrayType, ArrayList<Node> nodes, Body parent) {
        super(parent);
        this.type = arrayType;
        this.nodes = nodes.toArray(new Node[nodes.size()]);
    }

    public Array(NodeType arrayType, Node size, Body parent) {
        super(parent);
        this.type = arrayType;
        this.length = size;
    }

    @Override
    public NodeType getNodeType() {
        return type;
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.VALUE;
    }

    @Override
    public String trace() {
        String result = "new " + NodeType.forName(type.getId()) + "{";
        for (int i = 0; i < nodes.length; i++) {
            result += nodes[i].trace() + (i == nodes.length - 1 ? "" : ", ");
        }
        return result + "}";
    }

    public static int getSize0(Array array) {
        int value = 0;
        if (array.length == null) {
            value += new IntegerConstant(array.nodes.length).getSize();
            value += SizeOf.SIZEOF_NEWARRAY;
            value += SizeOf.SIZEOF_SHORT;
            for (int nodeIndex = 0; nodeIndex < array.nodes.length; nodeIndex++) {
                Node atom = array.nodes[nodeIndex];
                value += atom.getSize();
                switch (atom.getNodeType().getId()) {
                    case NodeType.T_CHAR:
                    case NodeType.T_INT: {
                        if (array.type.getId() == NodeType.T_FLOAT) {
                            value += SizeOf.SIZEOF_BYTE;
                        }
                        break;
                    }
                }
                value += new IntegerConstant(nodeIndex).getSize();
                value += SizeOf.SIZEOF_BYTE;
            }
        } else {
            value += array.length.getSize();
            value += SizeOf.SIZEOF_NEWARRAY;
            value += SizeOf.SIZEOF_SHORT;
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

    @Override
    public void compile() {
        compile0(this);
    }

    public static void compile0(Array array) {
        if (array.length == null) {
            new IntegerConstant(array.nodes.length).compile();
            Output.writeByte(Constants.NEWARRAY);
            Output.writeShort(array.type.getId());
            for (int nodeIndex = 0; nodeIndex < array.nodes.length; nodeIndex++) {
                Node atom = array.nodes[nodeIndex];
                atom.compile();
                switch (atom.getNodeType().getId()) {
                    case NodeType.T_CHAR:
                    case NodeType.T_INT: {
                        if (array.type.getId() == NodeType.T_FLOAT) {
                            Output.writeByte(Constants.I2F);
                        }
                        break;
                    }
                }
                new IntegerConstant(nodeIndex).compile();
                switch (array.getNodeType().getId()) {
                    case NodeType.T_BOOLEAN:
                    case NodeType.T_CHAR:
                    case NodeType.T_INT: {
                        if (array.type.getId() == NodeType.T_FLOAT) {
                            Output.writeByte(Constants.FASTORE_1);
                        } else {
                            Output.writeByte(Constants.IASTORE_1);
                        }
                        break;
                    }
                    case NodeType.T_FLOAT: {
                        Output.writeByte(Constants.FASTORE_1);
                        break;
                    }
                    case NodeType.T_STRING: {
                        Output.writeByte(Constants.SASTORE_1);
                        break;
                    }
                    case NodeType.T_OBJECT: {
                        Output.writeByte(Constants.OASTORE_1);
                        break;
                    }
                    default: {
                        Output.writeByte(Constants.RASTORE_1);
                        break;
                    }
                }
            }
        } else {
            array.length.compile();
            Output.writeByte(Constants.NEWARRAY);
            Output.writeShort(array.type.getId());
        }
    }
}
