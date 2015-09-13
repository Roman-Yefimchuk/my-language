package dina.compiler.builder;

import dina.compiler.*;

public class Node {

    private int address = -1;
    private boolean used;

    public int getAddress() {
        return address;
    }

    public void setUsed(boolean value) {
        used = value;
    }

    public boolean isUsed() {
        return used;
    }

    public void setAddress() {
        address = Output.getCurrentAddress() + 1;
    }

    public void compile() {
    }

    public NodeType getNodeType() {//тип объекта
        return null;
    }

    public NodeClass getNodeClass() {//класс объекта
        return null;
    }

    public String trace() {
        return null;
    }

    public int getSize() {
        return 0;
    }

    public boolean getBooleanValue() {
        throw new RuntimeException("Узел '" + this + "' не может вернуть значение типа " + NodeType.BASIC_TYPE_NAMES[NodeType.T_BOOLEAN]);
    }

    public int getCharValue() {
        throw new RuntimeException("Узел '" + this + "' не может вернуть значение типа " + NodeType.BASIC_TYPE_NAMES[NodeType.T_CHAR]);
    }

    public int getIntegerValue() {
        throw new RuntimeException("Узел '" + this + "' не может вернуть значение типа " + NodeType.BASIC_TYPE_NAMES[NodeType.T_INT]);
    }

    public float getFloatValue() {
        throw new RuntimeException("Узел '" + this + "' не может вернуть значение типа " + NodeType.BASIC_TYPE_NAMES[NodeType.T_FLOAT]);
    }

    public String getStringValue() {
        throw new RuntimeException("Узел '" + this + "' не может вернуть значение типа " + NodeType.BASIC_TYPE_NAMES[NodeType.T_STRING]);
    }

    public Node optimize() {
        return this;
    }
}
