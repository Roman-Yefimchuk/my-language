package dina.runtime.variables;

public class IntVariable extends Variable {

    public IntVariable() {
        this(0);
    }

    public IntVariable(int entity) {
        super(entity, INT);
        if (entity == VALUE) {
            value = new int[1];
        } else {
            value = new int[0];
        }
    }

    @Override
    public Variable newInstance(int dimension) {
        return new IntVariable(dimension);
    }
}
