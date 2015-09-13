package dina.compiler;

import dina.compiler.builder.*;
import dina.compiler.builder.objects.constants.*;

public class InternalConversion extends Node {

    private NodeType type;
    private Node object;
    private int size = -1;

    private static void init(NodeType type, Node object) {
    }

    private InternalConversion(NodeType type, Node object) {
        init(type, object);
        this.type = type;
        this.object = object;
    }

    @Override
    public void compile() {
        compile0(this);
    }

    private static void compile0(InternalConversion conversion) {
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(InternalConversion conversion) {
        int value = 0;
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

    public static Node getNode(NodeType type, Node object) {
        if (NodeType.hardCompare(type, object.getNodeType())) {
            return object;
        }
        return new InternalConversion(type, object).optimize();
    }

    @Override
    public Node optimize() {
        if (object.getNodeClass() == NodeClass.CONSTANT && !type.isArray()) {
            NodeType objectType = object.getNodeType();
            if (!objectType.isArray()) {
                int typeId = type.getId();
                int objectTypeId = objectType.getId();
                switch (typeId) {
                    case NodeType.T_FLOAT: {
                        switch (objectTypeId) {
                            case NodeType.T_CHAR: {
                                return new FloatConstant(object.getCharValue());
                            }
                            case NodeType.T_INT: {
                                return new FloatConstant(object.getIntegerValue());
                            }
                        }
                    }
                }
            }
        }
        return this;
    }
}
