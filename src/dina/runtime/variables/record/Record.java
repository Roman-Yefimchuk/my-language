package dina.runtime.variables.record;

import dina.runtime.variables.Variable;

public class Record extends Variable {

    public static final int OBJECT = 0;
    public static final int ID = 1;
    public static final int ID_VALUE = 0;
    public RecordContext recordContext;

    public Record(RecordContext recordContext) {
        this(0, recordContext);
    }

    public Record(int entity, RecordContext recordContext) {
        super(entity, recordContext.id);
        this.recordContext = recordContext;
        Variable[] recordContainer;
        if (entity == VALUE) {
            recordContainer = new RecordContainer[]{new RecordContainer(recordContext)};
        } else {
            recordContainer = new RecordContainer[0];
        }
        value = new Object[]{recordContainer, new int[]{id}};
    }

    @Override
    public Variable newInstance(int dimension) {
        return new Record(dimension, recordContext);
    }
}
