package dina.runtime.variables;

public class BooleanVariable extends Variable {

    public BooleanVariable() {
        this(0);
    }

    public BooleanVariable(int entity) {
        super(entity, BOOLEAN);
        if (entity == VALUE) {
            value = new int[1];
        } else {
            value = new int[0];
        }
    }

    @Override
    public Variable newInstance(int dimension) {
        return new BooleanVariable(dimension);
    }
}
