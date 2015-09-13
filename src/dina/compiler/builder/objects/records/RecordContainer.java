package dina.compiler.builder.objects.records;

import dina.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.records.fields.*;
import dina.compiler.utils.SetTable;

public class RecordContainer extends Node {

    private SetTable<String, Field> fields;
    private String name;

    public RecordContainer(String name) {
        fields = new SetTable<String, Field>();
        this.name = name;
        if (!NodeType.addRecord(this)) {
            throw new DinaException("Запись с именем '" + name + "' уже существует", DinaException.COMPILATION_ERROR);
        }
    }

    public void addFields(Field field) {
        if (fields.getValue(field.getName()) != null) {
            throw new DinaException("Поле с именем '" + field.getName() + "' уже существует", DinaException.COMPILATION_ERROR);
        }
        field.setFieldIndex(fields.size());
        fields.put(field.getName(), field);
    }

    public Field getField(String name) {
        return (Field) fields.getValue(name);
    }

    public Field getField(int index) {
        return fields.getValue(index);
    }

    public int getFieldsAmount() {
        return fields.size();
    }

    public String getName() {
        return name;
    }

    public NodeType getType() {
        return NodeType.forNodeType(name);
    }
}
