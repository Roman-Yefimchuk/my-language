package dina.runtime.variables;

public class CharVariable extends Variable {

    public CharVariable() {
        this(0);
    }

    public CharVariable(int entity) {
        super(entity, CHAR);
        if (entity == VALUE) {
            value = new int[1];
        } else {
            value = new int[0];
        }
    }

    @Override
    public Variable newInstance(int dimension) {
        return new CharVariable(dimension);
    }
}
