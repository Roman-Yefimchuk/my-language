package dina.runtime.variables.record;

import dina.runtime.*;
import dina.runtime.variables.*;

public class RecordContainer extends Variable {

    public Variable[] fields;
    public RecordContext recordContext;

    //native records(Image, File, Font...)
    public RecordContainer(Object object) {
        fields = new Variable[]{new ObjectVariable(object)};
    }

    public Object getObject() {
        return ((Object[]) fields[0].value)[0];
    }

    public void setObject(Object object) {
        ((Object[]) fields[0].value)[0] = object;
    }

    //custom records
    public RecordContainer(RecordContext recordContext) {
        this.recordContext = recordContext;
        fields = new Variable[recordContext.fieldsAmount];
        int length = fields.length;
        for (int fieldIndex = 0; fieldIndex < length; fieldIndex++) {
            fields[fieldIndex] = DinaVM.prototypes[recordContext.fieldsId[fieldIndex]].newInstance(recordContext.fieldsArrayDimension[fieldIndex]);
        }
    }
}
