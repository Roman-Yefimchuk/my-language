package dina.runtime.variables;

public class ObjectVariable extends Variable {

    public static final Object[] EMPTY_OBJECT = {""};

    public ObjectVariable() {
        this(0);
    }

    public ObjectVariable(int entity) {
        super(entity, OBJECT);
        if (entity == VALUE) {
            value = new Object[1];
        } else {
            value = new Object[0];
        }
    }

    public ObjectVariable(Object object) {
        super(VALUE, OBJECT);
        value = new Object[]{object};
    }

    @Override
    public Variable newInstance(int dimension) {
        return new ObjectVariable(dimension);
    }
}
