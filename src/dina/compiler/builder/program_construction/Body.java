package dina.compiler.builder.program_construction;

import java.util.*;
import dina.*;
import dina.compiler.utils.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;

public class Body extends Node {

    public static final int LOCAL = 0;
    public static final int GLOBAL = 1;
    //
    private String name;
    private SetTable<String, VariablePointer> variables;
    private Hashtable<String, Constant> constantPool;
    private int variableKind;
    private int variablesCount;
    private int currentVariableIndex = -1;

    public Body() {
        variables = new SetTable<String, VariablePointer>();
        constantPool = new Hashtable<String, Constant>();
    }

    public void addConstant(String name, Constant value) {
        constantPool.put(name, value);
    }

    public Node getConstant(String name) {
        return constantPool.get(name);
    }

    public void addVariable(Variable variable) {
        if (getConstant(variable.getName()) != null) {
            throw new DinaException("Константа '" + variable.getName() + "' уже существует",
                    DinaException.COMPILATION_ERROR);
        }
        variables.put(variable.getName(), new VariablePointer(variable, ++currentVariableIndex));
        variablesCount++;
    }

    public Variable getVariable(int index) {
        return variables.getValue(index).getVariable();
    }

    public Variable getVariable(String name) {
        VariablePointer variablePointer = variables.getValue(name);
        if (variablePointer != null) {
            return variablePointer.getVariable();
        }
        return null;
    }

    public int getVariableIndex(String name) {
        return variables.getValue(name).getVariableIndex();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVariablesAmount() {
        return variables.size();
    }

    public boolean isGlobalVariables() {
        return variableKind == GLOBAL;
    }

    public void setVariablesKind(int variableKind) {
        this.variableKind = variableKind;
    }
}
