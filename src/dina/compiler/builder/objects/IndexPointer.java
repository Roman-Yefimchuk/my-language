package dina.compiler.builder.objects;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.program_construction.*;

public class IndexPointer extends Variable {

    private Node array;
    private Node index;
    private int size = -1;
    private int assignSize = -1;

    public static void init(Node index) {
        if (index.getNodeType().isInteger() && !index.getNodeType().isArray()) {
            return;
        }
        throw new DinaException("Указатель на индекс массива должен быть целого типа, найдено '"
                + index.getNodeType() + "'", DinaException.COMPILATION_ERROR);
    }

    public IndexPointer(Node array, Node index, Body parentBody) {
        super(parentBody);
        init(index);
        this.array = array;
        this.index = index;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.getType(array.getNodeType().getId());
    }

    @Override
    public String trace() {
        return array.trace() + "[" + index.trace() + "]";
    }

    @Override
    public void compile() {
        compile0(this);
    }

    public static void compile0(IndexPointer indexPointer) {
        indexPointer.array.compile();
        indexPointer.index.compile();
        switch (indexPointer.getNodeType().getId()) {
            case NodeType.T_BOOLEAN:
            case NodeType.T_CHAR:
            case NodeType.T_INT: {
                Output.writeByte(Constants.IALOAD);
                break;
            }
            case NodeType.T_FLOAT: {
                Output.writeByte(Constants.FALOAD);
                break;
            }
            case NodeType.T_STRING: {
                Output.writeByte(Constants.SALOAD);
                break;
            }
            case NodeType.T_OBJECT: {
                Output.writeByte(Constants.OALOAD);
                break;
            }
            default: {
                Output.writeByte(Constants.RALOAD);
                break;
            }
        }
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = SizeOf.SIZEOF_BYTE + index.getSize() + array.getSize();
        }
        return size;
    }

    @Override
    public int getAssignSize() {
        if (assignSize == -1) {
            assignSize = SizeOf.SIZEOF_BYTE + index.getSize() + array.getSize();
        }
        return assignSize;
    }

    @Override
    public void assign() {
        assign0(this);
    }

    public static void assign0(IndexPointer indexPointer) {
        indexPointer.array.compile();
        indexPointer.index.compile();
        switch (indexPointer.getNodeType().getId()) {
            case NodeType.T_BOOLEAN:
            case NodeType.T_CHAR:
            case NodeType.T_INT: {
                Output.writeByte(Constants.IASTORE_0);
                break;
            }
            case NodeType.T_FLOAT: {
                Output.writeByte(Constants.FASTORE_0);
                break;
            }
            case NodeType.T_STRING: {
                Output.writeByte(Constants.SASTORE_0);
                break;
            }
            case NodeType.T_OBJECT: {
                Output.writeByte(Constants.OASTORE_0);
                break;
            }
            default: {
                Output.writeByte(Constants.RASTORE_0);
                break;
            }
        }
    }
}
