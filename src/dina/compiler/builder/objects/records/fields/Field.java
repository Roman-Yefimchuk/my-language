package dina.compiler.builder.objects.records.fields;

import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;

public class Field extends Variable {

    private NodeType fieldType;
    private NodeClass fieldClass;
    private int fieldIndex;

    public Field() {
        super("$object");
        fieldType = NodeType.TYPE_OBJECT;
    }

    public Field(String name, NodeType fieldType) {
        super(name);
        this.fieldType = fieldType;
    }

    public Field(String name, NodeType fieldType, NodeClass fieldClass) {
        this(name, fieldType);
        this.fieldClass = fieldClass;
    }

    @Override
    public NodeType getNodeType() {
        return fieldType;
    }

    @Override
    public NodeClass getNodeClass() {
        if (fieldClass != null) {
            return fieldClass;
        }
        return NodeClass.VARIABLE;
    }

    @Override
    public boolean isRecord() {
        return fieldType.isRecord();
    }

    @Override
    public String trace() {
        return getName();
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }
}
