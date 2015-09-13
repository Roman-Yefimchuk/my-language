package dina.runtime.variables;

public class FloatVariable extends Variable {

    public FloatVariable() {
        this(0);
    }

    public FloatVariable(int entity) {
        super(entity, FLOAT);
        if (entity == VALUE) {
            value = new float[1];
        } else {
            value = new float[0];
        }
    }

    @Override
    public Variable newInstance(int dimension) {
        return new FloatVariable(dimension);
    }
}
