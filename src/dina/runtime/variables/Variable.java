package dina.runtime.variables;

import dina.*;
import dina.runtime.*;

public class Variable implements TypeContext {

    public Object value;
    public int dimension;
    public int id;

    public Variable() {
    }

    public Variable(int dimension, int id) {
        this.dimension = dimension;
        this.id = id;
    }

    public Variable newInstance(int entity) {
        return null;
    }

    public String getClassName() {
        return DinaVM.getClassName(id, dimension);
    }
}
