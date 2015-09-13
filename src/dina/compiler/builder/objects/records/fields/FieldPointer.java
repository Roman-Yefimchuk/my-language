package dina.compiler.builder.objects.records.fields;

import java.util.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;

public class FieldPointer extends Variable {

    private Node targetRecord;
    private Field targetField;
    private ArrayList<Field> gap = new ArrayList<Field>();
    private int size = -1;
    private int assignSize = -1;

    public void setTargetRecord(Node targetRecord) {
        this.targetRecord = Common.checkNode(targetRecord);
    }

    public void addGap(Field field) {
        gap.add(field);
    }

    public void setTargetField(Field targetField) {
        this.targetField = targetField;
    }

    public Field getTargetField() {
        return targetField;
    }

    public Node getTargetRecord() {
        return targetRecord;
    }

    @Override
    public void compile() {
        targetRecord.compile();
        for (int gapIndex = 0; gapIndex < gap.size(); gapIndex++) {
            gap.get(gapIndex).compile();
        }
        targetField.compile();
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(FieldPointer fieldPointer) {
        int value = 0;
        value += fieldPointer.targetRecord.getSize();
        for (int gapIndex = 0; gapIndex < fieldPointer.gap.size(); gapIndex++) {
            value += fieldPointer.gap.get(gapIndex).getSize();
        }
        value += fieldPointer.targetField.getSize();
        return value;
    }

    @Override
    public void assign() {
        targetRecord.compile();
        for (int gapIndex = 0; gapIndex < gap.size(); gapIndex++) {
            gap.get(gapIndex).compile();
        }
        targetField.assign();
    }

    @Override
    public int getAssignSize() {
        if (assignSize == -1) {
            assignSize = getAssignSize(this);
        }
        return assignSize;
    }

    private static int getAssignSize(FieldPointer fieldPointer) {
        int value = 0;
        value += fieldPointer.targetRecord.getSize();
        for (int gapIndex = 0; gapIndex < fieldPointer.gap.size(); gapIndex++) {
            value += fieldPointer.gap.get(gapIndex).getSize();
        }
        value += fieldPointer.targetField.getAssignSize();
        return value;
    }

    @Override
    public NodeType getNodeType() {
        return targetField.getNodeType();
    }

    @Override
    public String trace() {
        String s = targetRecord.trace() + ".";
        for (int i = 0; i < gap.size(); i++) {
            s += gap.get(i).trace() + (i == gap.size() - 1 ? "" : ".");
        }
        return s += targetField.trace();
    }
}
