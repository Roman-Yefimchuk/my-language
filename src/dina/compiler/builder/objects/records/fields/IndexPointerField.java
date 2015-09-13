package dina.compiler.builder.objects.records.fields;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;

public class IndexPointerField extends Field {

    private Node index;
    private int size = -1;
    private int assignSize = -1;

    public IndexPointerField(String name, NodeType fieldType, Node index, int fieldIndex) {
        super(name, fieldType);
        this.index = index;
        setFieldIndex(fieldIndex);
    }

    @Override
    public void compile() {
        compile0(this);
    }

    public static void compile0(IndexPointerField indexPointerField) {
        Output.writeByte(Constants.GETFIELD);
        Output.writeShort(indexPointerField.getFieldIndex());
        indexPointerField.index.compile();
        switch (indexPointerField.getNodeType().getId()) {
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
            default: {
                Output.writeByte(Constants.RALOAD);
                break;
            }
        }
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(IndexPointerField indexPointerField) {
        int value = 0;
        value += SizeOf.SIZEOF_GETFIELD;
        value += SizeOf.SIZEOF_SHORT;
        value += indexPointerField.index.getSize();
        value += SizeOf.SIZEOF_BYTE;
        return value;
    }

    @Override
    public void assign() {
        assign0(this);
    }

    public static void assign0(IndexPointerField indexPointerField) {
        Output.writeByte(Constants.GETFIELD);
        Output.writeShort(indexPointerField.getFieldIndex());
        indexPointerField.index.compile();
        switch (indexPointerField.getNodeType().getId()) {
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
            default: {
                Output.writeByte(Constants.RASTORE_0);
                break;
            }
        }
    }

    @Override
    public int getAssignSize() {
        if (assignSize == -1) {
            assignSize = getAssignSize0(this);
        }
        return assignSize;
    }

    private static int getAssignSize0(IndexPointerField indexPointerField) {
        int value = 0;
        value += SizeOf.SIZEOF_GETFIELD;
        value += SizeOf.SIZEOF_SHORT;
        value += indexPointerField.index.getSize();
        value += SizeOf.SIZEOF_BYTE;
        return value;
    }

    @Override
    public String trace() {
        return getName() + "[" + index.trace() + "]";
    }
}
