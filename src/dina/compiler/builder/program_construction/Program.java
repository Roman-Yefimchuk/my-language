package dina.compiler.builder.program_construction;

import ide.*;
import java.util.*;
import dina.*;
import dina.compiler.*;
import dina.compiler.utils.*;
import dina.compiler.parser.*;
import dina.compiler.builder.objects.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.records.*;
import dina.compiler.builder.program_construction.statements.*;

public class Program extends Body {

    private Vector records = new Vector();
    private SetTable<String, Function> functions;
    private Function constructor;
    private Function destructor;

    public Program(String name) {
        setName(name);
        setVariablesKind(GLOBAL);
        functions = new SetTable<String, Function>();
    }

    public Function getConstructor() {
        return constructor;
    }

    public void setConstructor(Function constructor) {
        this.constructor = constructor;
    }

    public Function getDestructor() {
        return destructor;
    }

    public void setDestructor(Function destructor) {
        this.destructor = destructor;
    }

    private static void error(Function function) {
        throw new DinaException("Функция '" + function.getSignature().toString()
                + "' является системной",
                DinaException.COMPILATION_ERROR);
    }

    public void addFunction(Function function) {
        String functionSignature = function.getSignatureLabel();
        if (functions.getValue(functionSignature) != null) {
            throw new DinaException("Функция '" + function.getSignature().toString() + "' уже существует",
                    DinaException.COMPILATION_ERROR);
        }
        if (Function.getNativeFunctionsTable().getValue(functionSignature) != null) {
            error(function);
        }
        InternalFunctionContainer functionContainer = InternalFunctions.getFunctionContainer(function.getName());
        if (functionContainer != null) {
            if (functionContainer.check(function.getArguments())) {
                error(function);
            }
        }
        functions.put(functionSignature, function);
    }

    public Function getFunction(String signature) {
        return functions.getValue(signature);
    }

    public SetTable getFunctions() {
        return functions;
    }

    public void addRecord(RecordContainer record) {
        records.addElement(record);
    }

    public Vector getRecords() {
        return records;
    }

    public RecordContainer getRecord(int typeId) {
        final int PRIMITIVE_TYPES_COUNT = 6;
        return (RecordContainer) records.elementAt(typeId - PRIMITIVE_TYPES_COUNT);
    }

    public int getFunctionsAmount() {
        int value = functions.size();
        value += constructor == null ? 0 : 1;
        value += destructor == null ? 0 : 1;
        return value;
    }

    @Override
    public void compile() {
        Debugger debugContext = DinaCompiler.getDebugger();
        int variablesWithValue = 0;
        int variablesAmount = getVariablesAmount();
        for (int variableIndex = 0; variableIndex < variablesAmount; variableIndex++) {
            Variable variable = getVariable(variableIndex);
            if (variable.getValue() != null) {
                if (constructor == null) {
                    constructor = new Function(ParserConstants.CONSTRUCTOR);
                    constructor.setStatement(new Block());
                    constructor.getStatement().setFunction(constructor);
                }
                Assignment assignment = new Assignment(variable, variable.getValue());
                constructor.getStatement().insertNode(assignment, variablesWithValue);
                variablesWithValue++;
            }
        }
        if (constructor != null) {
            Output.writeSystemInformation(Constants.BEGIN_FUNCTION);
            constructor.compile();
            if (Debugger.DEVELOPMENT_MODE) {
                debugContext.putFunction("<constructor>", constructor.getAddress());
            }
            Output.writeSystemInformation(Constants.END_FUNCTION);
        }
        int functionsAmount = functions.size();
        for (int functionIndex = 0; functionIndex < functionsAmount; functionIndex++) {
            Function function = functions.getValue(functionIndex);
            Output.writeSystemInformation(Constants.BEGIN_FUNCTION);
            function.compile();
            if (Debugger.DEVELOPMENT_MODE) {
                debugContext.putFunction(function.getSignatureLabel() + function.getNodeType().getSignature(), function.getAddress());
            }
            Output.writeSystemInformation(Constants.END_FUNCTION);
        }
        if (destructor != null) {
            Output.writeSystemInformation(Constants.BEGIN_FUNCTION);
            destructor.compile();
            if (Debugger.DEVELOPMENT_MODE) {
                debugContext.putFunction("<destructor>", destructor.getAddress());
            }
            Output.writeSystemInformation(Constants.END_FUNCTION);
        }
    }

    public void checkFunctions() {
        Enumeration<Function> en = functions.elements();
        while (en.hasMoreElements()) {
            Function function = (Function) en.nextElement();
            if (function.isForward()) {
                if (!function.isCompleted()) {
                    throw new DinaException("Неопределенная функция '"
                            + function.getSignature()
                            + "'", DinaException.COMPILATION_ERROR);
                }
            }
        }
    }

    @Override
    public String trace() {
        String s = "";
        for (int i = 0; i < functions.size(); i++) {
            s += functions.getValue(i).trace();
        }
        return s;
    }
}
