package dina.compiler.builder.program_construction;

import java.util.*;
import dina.*;
import dina.compiler.*;
import dina.compiler.utils.*;
import dina.compiler.parser.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;
import dina.compiler.builder.objects.constants.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.program_construction.statements.*;

public class Function extends Body {

    public static final int MAX_ARGUMENTS_COUNT = 255;
    private Argument[] arguments = new Argument[0];//Аргументы, которые может принимать функция
    private SetTable<String, Variable> argumentsVariables;
    private Hashtable<String, Label> labels;//Таблица меток
    private NodeType functionType;//Тип функции
    private StatementContainer statement;
    private boolean forward;
    private boolean completed;
    private Signature functionSignature;
    //native
    private static SetTable<String, Function> nativeFunctionsTable = new SetTable<String, Function>();
    private boolean nativeFunction;//Является ли функция системной...
    private int functionId;//Идентификатор системной функции(для интерпретатора)
    private int libraryId;//Идентификатор библиотеки системных функций

    static {
        NativeFunctions.init();
    }
    private int functionKind = -1;

    public Function(int functionKind) {
        this();
        this.functionKind = functionKind;
        functionType = NodeType.TYPE_VOID;
    }

    public Function() {
        setVariablesKind(LOCAL);
        argumentsVariables = new SetTable<String, Variable>();
        labels = new Hashtable<String, Label>();
    }

    public int getBlockId() {
        return functionKind;
    }

    public int getFunctionId() {
        return functionId;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isCompleted() {
        return completed;
    }

    public static SetTable<String, Function> getNativeFunctionsTable() {
        return nativeFunctionsTable;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Argument[] getArguments() {
        return arguments;
    }

    public Variable getArgumentVariable(String name) {
        return argumentsVariables.getValue(name);
    }

    public void addLabel(Label label) {
        labels.put(label.getName(), label);
    }

    public Label getLabel(String labelName) {
        return (Label) labels.get(labelName);
    }

    public void setFunctionType(NodeType functionType) {
        this.functionType = functionType;
    }

    public boolean isNativeFunction() {
        return nativeFunction;
    }

    public StatementContainer getStatement() {
        return statement;
    }

    public void setStatement(StatementContainer statement) {
        this.statement = statement;
    }

    public int getArgumentsAmount() {
        return argumentsVariables.size();
    }

    public static void addArgument(Function function, Argument argument) {
        Argument[] tempArguments = new Argument[function.arguments.length + 1];
        System.arraycopy(function.arguments, 0, tempArguments, 0, function.arguments.length);
        tempArguments[function.arguments.length] = argument;
        function.arguments = tempArguments;
        tempArguments = null;
        System.gc();
    }

    public void addArgumentVariable(Variable variable) {
        if (argumentsVariables.size() == MAX_ARGUMENTS_COUNT) {
            throw new DinaException("Количество аргументов функции '" + getSignatureLabel() + "' > " + MAX_ARGUMENTS_COUNT,
                    DinaException.ASSEMBLY_ERROR);
        }
        argumentsVariables.put(variable.getName(), variable);
    }

    public Function(String signatureLabel) {//native function constructor
        this();
        functionSignature = Signature.getSignature(signatureLabel);
        setName(functionSignature.getFunctionName());
        this.arguments = functionSignature.getFunctionArguments();
        this.functionType = functionSignature.getFunctionType();
        nativeFunction = true;
    }

    public void createSignature() {
        functionSignature = Signature.getSignature(getSignatureLabel() + functionType.getSignature());
    }

    public Signature getSignature() {
        return functionSignature;
    }

    public static void addNativeFunction(Function function, int libratyID, int functionID) {
        function.libraryId = libratyID;
        function.functionId = functionID;
        nativeFunctionsTable.put(function.getSignatureLabel(), function);
    }

    @Override
    public NodeType getNodeType() {
        return functionType;
    }

    @Override
    public NodeClass getNodeClass() {
        return NodeClass.VALUE;
    }

    @Override
    public String trace() {
        return statement.trace();
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    private static void compile0(Function function) {
        int variablesAmount = function.getVariablesAmount();
        Output.writeShort(variablesAmount);
        Output.writeByte(function.argumentsVariables.size());
        for (int variableIndex = 0; variableIndex < variablesAmount; variableIndex++) {
            Variable variable = function.getVariable(variableIndex);
            try {
                Output.writeShort(variable.getNodeType().getId());
                Output.writeByte(variable.getNodeType().getDimension());
            } catch (Exception ex) {
            }
        }
        function.statement.compile();
        if (function.statement.getNodesAmount() == 0) {
            addReturn(function);
        } else if (!(function.statement.getNode(function.statement.getNodesAmount() - 1) instanceof Return
                || function.statement.getNode(function.statement.getNodesAmount() - 1) instanceof Throw)) {
            addReturn(function);
        }
    }

    public static void checkLabels(Function function) {
        Enumeration labels = function.labels.elements();
        while (labels.hasMoreElements()) {
            Label label = (Label) labels.nextElement();
            if (label.isCalled() == true && label.isInitialized() == false) {
                throw new DinaException("Метка '" + label.getName() + "' не установлена",
                        DinaException.COMPILATION_ERROR);
            }
            if (label.isInitialized()) {
                StatementContainer stmt_1 = AbstractStatement.forStatement(AbstractStatement.TRY, label);
                while (!label.getReferencing().empty()) {
                    GotoStatement gotoStmt = (GotoStatement) label.getReferencing().pop();
                    StatementContainer stmt_2 = AbstractStatement.forStatement(AbstractStatement.TRY, gotoStmt);
                    if (stmt_2 != stmt_1) {
                        System.out.println(stmt_1);
                        System.out.println(stmt_2);
                        throw new DinaException("Метка '"
                                + label.getName() + "' и переход(ы) на нее должны находится в одном блоке 'try'",
                                DinaException.COMPILATION_ERROR);
                    }
                }
            }
        }
    }

    private static void addReturn(Function function) {
        if (function.getNodeType().getId() != NodeType.T_VOID) {
            if (!function.getNodeType().isArray()) {
                switch (function.getNodeType().getId()) {
                    case NodeType.T_BOOLEAN: {
                        new BooleanConstant(false).compile();
                        break;
                    }
                    case NodeType.T_CHAR: {
                        new CharConstant('\000').compile();
                        break;
                    }
                    case NodeType.T_INT: {
                        new IntegerConstant(0).compile();
                        break;
                    }
                    case NodeType.T_FLOAT: {
                        new FloatConstant(0).compile();
                        break;
                    }
                    case NodeType.T_STRING: {
                        new StringConstant("").compile();
                        break;
                    }
                    default: {
                        new New(function.getNodeType().getId()).compile();
                        break;
                    }
                }
            } else {
                Output.writeByte(Constants.BCONST);
                Output.writeByte(0);
                Output.writeByte(Constants.NEWARRAY);
                Output.writeShort(function.getNodeType().getId());
            }
        }
        Output.writeByte(Constants.RETURN);
    }

    private static String getInternalName0(Function function) {
        switch (function.getBlockId()) {
            case -1: {
                StringBuilder internalName = new StringBuilder();
                internalName.append(function.getName()).append('(');
                for (int index = 0; index < function.arguments.length; index++) {
                    internalName.append(function.argumentsVariables.getKey(index));
                    internalName.append(function.arguments[index].getNodeType().getSignature());
                }
                internalName.append(')').append(function.functionType.getSignature());
                return internalName.toString();
            }
            case ParserConstants.CONSTRUCTOR: {
                return "<constructor>";
            }
            case ParserConstants.DESTRUCTOR: {
                return "<destructor>";
            }
        }
        return null;
    }

    public String getInternalName() {
        return getInternalName0(this);
    }

    private static String getSignatureLabel0(Function function) {
        if (function.functionKind == -1) {
            StringBuilder name = new StringBuilder();
            name.append(function.getName()).append('(');
            for (int argumentIndex = 0; argumentIndex < function.arguments.length; argumentIndex++) {
                name.append(function.arguments[argumentIndex].getNodeType().getSignature());
            }
            name.append(')');
            return name.toString();
        }
        return null;
    }

    public String getSignatureLabel() {
        return getSignatureLabel0(this);
    }

    public static String getFullName(String signatureLabel) {
        StringBuilder result = new StringBuilder();
        result.append(signatureLabel.substring(0, signatureLabel.indexOf('(') + 1));
        int seek = result.length();
        int dimension = 0;
        while (true) {
            switch (signatureLabel.charAt(seek)) {
                case '[': {
                    dimension++;
                    seek++;
                    continue;
                }
                case 'V': {
                    result.append("void");
                    seek++;
                    break;
                }
                case 'B': {
                    result.append("boolean");
                    seek++;
                    break;
                }
                case 'C': {
                    result.append("char");
                    seek++;
                    break;
                }
                case 'I': {
                    result.append("int");
                    seek++;
                    break;
                }
                case 'F': {
                    result.append("float");
                    seek++;
                    break;
                }
                case 'S': {
                    result.append("string");
                    seek++;
                    break;
                }
                case 'O': {
                    result.append("object");
                    seek++;
                    break;
                }
                case '@': {
                    seek++;
                    String recordName = signatureLabel.substring(seek, signatureLabel.indexOf(';', seek));
                    result.append(recordName);
                    seek += recordName.length();
                    seek++;
                    break;
                }
            }
            for (int index = 0; index < dimension; index++) {
                result.append("[]");
            }
            dimension = 0;
            if (signatureLabel.charAt(seek) == ')') {
                break;
            }
            result.append(", ");
        }
        result.append(')');
        return result.toString();
    }
}
