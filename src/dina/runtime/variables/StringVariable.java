package dina.runtime.variables;

public class StringVariable extends Variable {

    public static final String[] EMPTY_STRING = {""};

    public StringVariable() {
        this(0);
    }

    public StringVariable(int entity) {
        super(entity, STRING);
        if (entity == VALUE) {
            value = EMPTY_STRING;
        } else {
            value = new String[0];
        }
    }

    @Override
    public Variable newInstance(int dimension) {
        return new StringVariable(dimension);
    }
}
