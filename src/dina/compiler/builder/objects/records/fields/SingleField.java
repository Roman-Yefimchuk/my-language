package dina.compiler.builder.objects.records.fields;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;

public class SingleField extends Field {

    public SingleField(String name, NodeType fieldType, int fieldIndex) {
        super(name, fieldType);
        setFieldIndex(fieldIndex);
    }

    @Override
    public void compile() {
        Output.writeByte(Constants.GETFIELD);
        Output.writeShort(getFieldIndex());
    }

    @Override
    public void assign() {
        Output.writeByte(Constants.PUTFIELD);
        Output.writeShort(getFieldIndex());
    }

    @Override
    public int getSize() {
        return SizeOf.SIZEOF_GETFIELD + SizeOf.SIZEOF_SHORT;
    }

    @Override
    public int getAssignSize() {
        return SizeOf.SIZEOF_PUTFIELD + SizeOf.SIZEOF_SHORT;
    }

    @Override
    public String trace() {
        return getName();
    }
}
