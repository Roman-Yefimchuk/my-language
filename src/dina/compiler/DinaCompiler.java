package dina.compiler;

import ide.*;
import java.util.*;
import dina.*;
import dina.compiler.parser.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.operators.logic.*;
import dina.compiler.builder.operators.arithmetic.*;
import dina.compiler.builder.objects.*;
import dina.compiler.builder.objects.constants.*;
import dina.compiler.builder.objects.records.*;
import dina.compiler.builder.objects.records.fields.*;
import dina.compiler.builder.objects.variables.*;
import dina.compiler.builder.program_construction.*;
import dina.compiler.builder.program_construction.statements.*;
import dina.compiler.builder.program_construction.statements.cycle.*;
import dina.compiler.utils.*;

public class DinaCompiler implements ParserConstants, SystemConstants {

    public static final int ARGUMENT_LIST = 0;
    public static final int FUNCTION_SIGNATURE = 1;
    private static Program program;
    private static Body currentBody;
    private static int actionIndex = -1;
    private static AbstractTokenStream tokenStream;
    private static String[] actionName = {
        "Генерация промежуточного кода",
        "Компиляция программы",
        "Создание исполняемого файла",
        "Сборка программы завершена"
    };
    private static long startCompileTime;
    private static Debugger debugger;

    public static Program getProgram() {
        return program;
    }

    public static Debugger getDebugger() {
        return debugger;
    }

    public static void writeCompileLog(String logString) {
        Logger.println(logString);
    }

    public static void nextAction() {
        if (actionIndex == -1) {
            startCompileTime = System.currentTimeMillis();
        }
        actionIndex++;
        writeCompileLog(actionName[actionIndex]);
        if (actionIndex == actionName.length - 1) {
            writeCompileLog("Время сборки: "
                    + (((double) (System.currentTimeMillis() - startCompileTime)) / (double) 1000) + " сек.");
        }
    }

    public static int getEntryPoint(String mainFunctionSignature) {
        Signature signature = Signature.getSignature(mainFunctionSignature);
        Function mainFunction = forFunction(signature, program.getFunctions());
        if (mainFunction == null) {
            throw new DinaException("Точка входа в программу отсутствует. Функция '"
                    + signature + "' не обнаружена", DinaException.ASSEMBLY_ERROR);
        }
        return mainFunction.getAddress();
    }

    public static void back() {
        tokenStream.back();
    }

    public static Token currentToken() {
        return tokenStream.currentToken();
    }

    public static Token nextToken() {
        Token nextToken = tokenStream.nextToken();
        if (nextToken == null) {
            throw new DinaException("Неожиданный конец файла", DinaException.COMPILATION_ERROR);
        }
        return nextToken;
    }

    public static void resetCompiler() {
        debugger = new Debugger();
        NodeType.reset();
        program = null;
        FloatConstant.reset();
        StringConstant.reset();
        AbstractStatement.reset();
        FunctionPointer.forwardFunctions.removeAllElements();
        Runtime.getRuntime().gc();
    }

    public static void buildProject(String source) {
        actionIndex = -1;
        tokenStream = new TextTokenStream(source);
        resetCompiler();
        compile();
    }

    public static void throwException(Exception ex) {
        if (Debugger.DEVELOPMENT_MODE) {
            ex.printStackTrace();
        }
        throw new DinaException("Невозможно собрать программу. Причина: " + ex.getMessage(),
                DinaException.ASSEMBLY_ERROR);
    }

    public static void writeHeader() {
        try {
            Output.outputStream.writeInt(HEADER);//write header
            Output.outputStream.writeFloat(VERSION);//write varsion
        } catch (Exception ex) {
            throwException(ex);
        }
    }

    public static void writeFloatConstants() {
        try {
            ArrayList<Float> constantPool = FloatConstant.getOptimizedConstantPool();
            int constantPoolSize = constantPool.size();
            Output.outputStream.writeShort(constantPoolSize);//write float constants count
            for (int index = 0; index < constantPoolSize; index++) {
                Output.outputStream.writeFloat(constantPool.get(index));//write float constants
            }
        } catch (Exception ex) {
            throwException(ex);
        }
    }

    public static void writeStringConstants() {
        try {
            ArrayList<String> constantPool = StringConstant.getOptimizedConstantPool();
            int constantPoolSize = constantPool.size();
            Output.outputStream.writeShort(constantPoolSize);//write string constants count
            for (int index = 0; index < constantPoolSize; index++) {
                Output.outputStream.writeUTF(constantPool.get(index));//write string constants
            }
        } catch (Exception ex) {
            throwException(ex);
        }
    }

    public static void writeConstants() {
        writeFloatConstants();
        writeStringConstants();
    }

    public static void writeRecords() {
        try {
            int recordsAmount = program.getRecords().size();
            Output.outputStream.writeShort(recordsAmount);
            if (recordsAmount != 0) {
                for (int recordIndex = 0; recordIndex < recordsAmount; recordIndex++) {
                    RecordContainer recordContainer = (RecordContainer) program.getRecords().elementAt(recordIndex);
                    int recordId = recordContainer.getType().getId();
                    Output.outputStream.writeShort(recordId);
                    Output.outputStream.writeUTF(recordContainer.getName());
                    int fieldsAmount = recordContainer.getFieldsAmount();
                    Output.outputStream.writeShort(fieldsAmount);
                    if (fieldsAmount != 0) {
                        for (int fieldIndex = 0; fieldIndex < fieldsAmount; fieldIndex++) {
                            Field field = recordContainer.getField(fieldIndex);
                            int fieldId = field.getNodeType().getId();
                            int fieldDimension = field.getNodeType().getDimension();
                            Output.outputStream.writeShort(fieldId);
                            Output.outputStream.writeByte(fieldDimension);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throwException(ex);
        }
    }

    public static void writeGlobalVariables() {
        try {
            int variablesAmount = program.getVariablesAmount();
            Output.outputStream.writeShort(variablesAmount);//write global variables count
            for (int variableIndex = 0; variableIndex < variablesAmount; variableIndex++) {
                Variable variable = program.getVariable(variableIndex);
                Output.outputStream.writeShort(variable.getNodeType().getId());
                Output.outputStream.writeByte(variable.getNodeType().getDimension());
            }
        } catch (Exception ex) {
            throwException(ex);
        }
    }

    public static void writeBootInfo() {
        int entryPoint = getEntryPoint("main()V");
        int constructorAddress = program.getConstructor() != null ? program.getConstructor().getAddress() : -1;
        int destructorAddress = program.getDestructor() != null ? program.getDestructor().getAddress() : -1;
        try {
            Output.outputStream.writeShort(program.getFunctionsAmount());
            Output.outputStream.writeInt(entryPoint);
            Output.outputStream.writeInt(constructorAddress);
            Output.outputStream.writeInt(destructorAddress);
        } catch (Exception ex) {
            throwException(ex);
        }
    }

    public static void writeInstructions() {
        try {
            byte[] commands = Output.toByteArray();
            Output.outputStream.writeInt(commands.length - (program.getFunctionsAmount() * 2));
            Output.outputStream.write(commands);
        } catch (Exception ex) {
            throwException(ex);
        }
    }

    public static void compileProgram() {
        program.compile();
        while (!FunctionPointer.forwardFunctions.empty()) {
            Object[] obj = (Object[]) FunctionPointer.forwardFunctions.pop();
            Function function = (Function) obj[0];
            int positionInOutput = ((Integer) obj[1]).intValue();
            Output.setAddress(positionInOutput + 1, function.getAddress());
        }
    }

    public static void compile() {
        nextAction();
        buildProgram();
        Output.flush();
        nextAction();
        compileProgram();
        nextAction();
        writeHeader();
        writeConstants();
        writeRecords();
        writeGlobalVariables();
        writeBootInfo();
        writeInstructions();
        nextAction();
        //System.out.println(program.trace());
    }

    public static Node getOrExpression() {
        Node expression = getAndExpression();
        Node temp = null;
        while (getOperator(nextToken().getName()) == OR) {
            temp = getAndExpression();
            expression = new Or(expression, temp).optimize();
        }
        back();
        return expression;
    }

    public static Node getAndExpression() {
        Node expression = getEqualityExpression();
        Node temp = null;
        while (getOperator(nextToken().getName()) == AND) {
            temp = getEqualityExpression();
            expression = new And(expression, temp).optimize();
        }
        back();
        return expression;
    }

    public static Node getEqualityExpression() {
        Node expression = getRelationalExpression();
        Node temp = null;
        int type;
        while ((type = getOperator(nextToken().getName())) == EQ || type == NE) {
            temp = getRelationalExpression();
            switch (type) {
                case EQ: {
                    expression = new EQ(expression, temp).optimize();
                    break;
                }
                case NE: {
                    expression = new NE(expression, temp).optimize();
                    break;
                }
            }
        }
        back();
        return expression;
    }

    public static Node getRelationalExpression() {
        Node expression = getAdditiveExpression();
        Node temp = null;
        int type;
        while ((type = getOperator(nextToken().getName())) == LE || type == LT || type == GT || type == GE || type == TYPE_OF_EQ || type == TYPE_OF_NE) {
            switch (type) {
                case TYPE_OF_EQ: {
                    expression = new TypeOfEQ(expression, getTypeFull()).optimize();
                    continue;
                }
                case TYPE_OF_NE: {
                    expression = new TypeOfNE(expression, getTypeFull()).optimize();
                    continue;
                }
            }
            temp = getAdditiveExpression();
            switch (type) {
                case LE: {
                    expression = new LE(expression, temp).optimize();
                    break;
                }
                case LT: {
                    expression = new LT(expression, temp).optimize();
                    break;
                }
                case GE: {
                    expression = new GE(expression, temp).optimize();
                    break;
                }
                case GT: {
                    expression = new GT(expression, temp).optimize();
                    break;
                }
            }
        }
        back();
        return expression;
    }

    public static Node getAdditiveExpression() {
        Node expression = getMultiplicativeExpression();
        Node temp = null;
        int type;
        while ((type = getOperator(nextToken().getName())) == PLUS || type == MINUS) {
            temp = getMultiplicativeExpression();
            switch (type) {
                case PLUS: {
                    expression = new Add(expression, temp).optimize();
                    break;
                }
                case MINUS: {
                    expression = new Sub(expression, temp).optimize();
                    break;
                }
            }
        }
        back();
        return expression;
    }

    public static Node getMultiplicativeExpression() {
        Node expression = getUnaryExpression();
        Node temp = null;
        int type;
        while ((type = getOperator(nextToken().getName())) == STAR || type == SLASH || type == REM) {
            temp = getUnaryExpression();
            switch (type) {
                case STAR: {
                    expression = new Mul(expression, temp).optimize();
                    break;
                }
                case SLASH: {
                    expression = new Div(expression, temp).optimize();
                    break;
                }
                case REM: {
                    expression = new Mod(expression, temp).optimize();
                    break;
                }
            }
        }
        back();
        return expression;
    }

    public static Node getUnaryExpression() {
        switch (getOperator(nextToken().getName())) {
            case PLUS: {
                return getParenthesisExpression();
            }
            case MINUS: {
                Node expression = getParenthesisExpression();
                if (expression == null) {
                    throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
                }
                return new Minus(expression).optimize();
            }
            case BANG: {
                Node expression = getUnaryExpression();
                if (expression == null) {
                    throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
                }
                return new Not(expression).optimize();
            }
        }
        back();
        return getParenthesisExpression();
    }

    public static Node getParenthesisExpression() {
        if (nextToken().getName().equals("(")) {
            NodeType type = getTypeFull();
            if (type != null) {
                skipSeparator(RPAREN);
                Node object = getNode();
                TypeConversion checkCast = new TypeConversion(type, object);
                return checkCast;
            }
            Brackets brackets = new Brackets(getConditionalExpression());
            skipSeparator(RPAREN);
            return A(brackets);
        }
        back();
        return getNode();
    }

    public static Node getConditionalExpression() {
        Node expression = getOrExpression();
        String error = "Некорректное начало выражения";
        if (expression == null) {
            return expression;
        }
        if (nextToken().getName().equals("?")) {
            if (expression != null) {
                if (expression.getNodeType().getId() != NodeType.T_BOOLEAN) {
                    throw new DinaException("Выражение должно иметь тип 'boolean', обнаружено "
                            + expression.getNodeType(), DinaException.COMPILATION_ERROR);
                }
            } else {
                throw new DinaException(error, DinaException.COMPILATION_ERROR);
            }
            Node leftOperand = getConditionalExpression();
            Node rightOperand = null;
            skipSeparator(COLON);
            rightOperand = getConditionalExpression();
            if (leftOperand == null || rightOperand == null) {
                throw new DinaException(error, DinaException.COMPILATION_ERROR);
            }
            if (!NodeType.compare(leftOperand, rightOperand)) {
                throw new DinaException("Несовместимые типы данных: '"
                        + leftOperand.getNodeType()
                        + "' и '" + rightOperand.getNodeType() + "'", DinaException.COMPILATION_ERROR);
            }
            return new Conditional(expression, leftOperand, rightOperand).optimize();
        } else {
            back();
        }
        return expression.optimize();
    }

    public static Node getIdentifier(Body parent, Token token) {
        Node result = null;
        if (token.getType() != Token.ttIdentifier) {
            throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
        }
        if (parent instanceof Program) {
            Program program = (Program) parent;
            if (nextToken().getName().equals("(")) {
                String functionName = token.getName();
                InternalFunctionContainer functionContainer = InternalFunctions.getFunctionContainer(functionName);
                Object functionPointer = getFunctionPointer(functionName);
                if (functionContainer != null) {
                    ArrayList<Node> argumentList = getArgumentList(functionPointer);
                    if (functionContainer.check(InternalFunctionContainer.toArray(argumentList))) {
                        InternalFunctionPointer internalFunctionPointer = new InternalFunctionPointer(functionContainer, argumentList);
                        return internalFunctionPointer;
                    }
                }
                String functionSignature = getFunctionSignature(functionPointer);
                Function function = forFunction(functionSignature, program.getFunctions());
                if (function == null) {
                    function = forFunction(functionSignature, Function.getNativeFunctionsTable());
                }
                if (function != null) {
                    result = new FunctionPointer(function, getArgumentList(functionPointer));
                    return result;
                }
                throw new DinaException("Фунция '" + Function.getFullName(functionSignature) + "' не обнаружена",
                        DinaException.COMPILATION_ERROR);
            } else {
                back();
            }
            Object object = program.getVariable(token.getName());
            if (object != null) {
                result = (Variable) object;
            }
        }
        if (result == null) {
            result = parent.getConstant(token.getName());
        }
        return result;
    }

    public static Object getFunctionPointer(String functionName) {
        Object[] result = new Object[2];
        String siganture = functionName + "(";
        ArrayList<Node> arguments = getArguments();
        for (int argumentIndex = 0; argumentIndex < arguments.size(); argumentIndex++) {
            Node node = arguments.get(argumentIndex);
            siganture += node.getNodeType().getSignature();
        }
        siganture += ")V";
        result[ARGUMENT_LIST] = arguments;
        result[FUNCTION_SIGNATURE] = siganture;
        return result;
    }

    public static ArrayList<Node> getArguments() {
        Token token = null;
        ArrayList<Node> arguments = new ArrayList<Node>();
        Node nextArgument = null;
        boolean hasMoreArguments = true;
        while (true) {
            nextArgument = null;
            token = nextToken();
            if (token.getName().equals(")")) {
                break;
            }
            if (token.getName().equals(",")) {
                hasMoreArguments = true;
            } else {
                back();
            }
            if (hasMoreArguments) {
                nextArgument = getConditionalExpression();
            }
            if (nextArgument == null) {
                throw new DinaException("Некорректное начало выражения",
                        DinaException.COMPILATION_ERROR);
            }
            arguments.add(Common.checkNode(nextArgument));
            hasMoreArguments = false;
        }
        return arguments;
    }

    public static ArrayList<Node> getArgumentList(Object object) {
        return (ArrayList<Node>) (((Object[]) object)[ARGUMENT_LIST]);
    }

    public static String getFunctionSignature(Object object) {
        return (String) (((Object[]) object)[FUNCTION_SIGNATURE]);
    }

    public static Node getNode() {
        Token token = nextToken();
        Node result = null;
        switch (token.getType()) {
            case Token.ttIntConstant: {
                result = new IntegerConstant(token.getName());
                break;
            }
            case Token.ttCharConstant: {
                result = new CharConstant(Token.getChar(token.getName()));
                break;
            }
            case Token.ttFloatConstant: {
                result = new FloatConstant(token.getName());
                break;
            }
            case Token.ttStringConstant: {
                result = new StringConstant(Token.getString(token.getName()));
                break;
            }
            case Token.ttKeyWord: {
                switch (getKeyWord(token.getName())) {
                    case TRUE: {
                        result = new BooleanConstant(true);
                        break;
                    }
                    case FALSE: {
                        result = new BooleanConstant(false);
                        break;
                    }
                    case NEW: {
                        NodeType type = getTypeQuick();
                        if (type != null) {
                            int typeId = type.getId();
                            if (typeId == NodeType.T_VOID) {
                                throw new DinaException("Некорректное начало выражения",
                                        DinaException.COMPILATION_ERROR);
                            }
                            token = nextToken();
                            if (token.getName().equals("(")) {
                                ArrayList<Node> arguments = getArguments();
                                if (!type.isRecord()) {
                                    throw new DinaException("Тип '"
                                            + type + "' не является записью", DinaException.COMPILATION_ERROR);
                                }
                                int size = arguments.size();
                                RecordContainer recordContainer = type.getRecordContainer();
                                if (size == 0) {
                                    result = new New(typeId);
                                } else {
                                    if (recordContainer.getFieldsAmount() != size) {
                                        throw new DinaException("Количество аргументов конструктора и количество полей в записи '"
                                                + type + "' не совпадают", DinaException.COMPILATION_ERROR);
                                    }
                                    for (int argumenIndex = 0; argumenIndex < size; argumenIndex++) {
                                        if (!NodeType.compare(recordContainer.getField(argumenIndex), arguments.get(argumenIndex))) {
                                            throw new DinaException("Тип " + (argumenIndex + 1) + "-го аргумента конструктора и "
                                                    + (argumenIndex + 1) + "-го поля в записи '" + type
                                                    + "' не совпадают", DinaException.COMPILATION_ERROR);
                                        }
                                    }
                                    result = new New(typeId, arguments);
                                }
                            } else if (token.getName().equals("[")) {
                                if (nextToken().getName().equals("]")) {
                                    if (nextToken().getName().equals("{")) {
                                        ArrayList<Node> elements = new ArrayList<Node>();
                                        Node nextElement = null;
                                        boolean hasMoreElements = true;
                                        while (true) {
                                            nextElement = null;
                                            token = nextToken();
                                            if (token.getName().equals("}")) {
                                                break;
                                            } else if (token.getName().equals(",")) {
                                                hasMoreElements = true;
                                            } else {
                                                back();
                                            }
                                            if (hasMoreElements) {
                                                nextElement = getConditionalExpression();
                                            }
                                            if (nextElement == null) {
                                                throw new DinaException("Некорректное объявление элементов массива",
                                                        DinaException.COMPILATION_ERROR);
                                            }
                                            if (!NodeType.compare(type, nextElement.getNodeType())) {
                                                throw new DinaException("Все элементы массива должны иметь тип '"
                                                        + NodeType.forName(typeId) + "', найдено '"
                                                        + nextElement.getNodeType() + "'",
                                                        DinaException.COMPILATION_ERROR);
                                            }
                                            elements.add(Common.checkNode(nextElement));
                                            hasMoreElements = false;
                                        }
                                        result = new Array(NodeType.getPrototype(typeId, 1), elements, currentBody);
                                    } else {
                                        throw new DinaException("Пропущен размер массива",
                                                DinaException.COMPILATION_ERROR);
                                    }
                                } else {
                                    back();
                                    Node arraySize = getConditionalExpression();
                                    if (arraySize == null) {
                                        throw new DinaException("Размер массива задан некорректно",
                                                DinaException.COMPILATION_ERROR);
                                    }
                                    if (arraySize.getNodeType().getId() != NodeType.T_INT && arraySize.getNodeType().getId()
                                            != NodeType.T_CHAR && arraySize.getNodeType().getDimension() != 0) {
                                        throw new DinaException("Размер массива должен быть целого типа, найдено "
                                                + arraySize.getNodeType(), DinaException.COMPILATION_ERROR);
                                    }
                                    skipSeparator(RBRACKET);
                                    result = new Array(NodeType.getPrototype(typeId, 1), Common.checkNode(arraySize), currentBody);
                                    break;
                                }
                            } else {
                                throw new DinaException("Ожидается '(' или '[', но '" + token.getName()
                                        + "' обнаружено", DinaException.COMPILATION_ERROR);
                            }
                        } else {
                            throw new DinaException("Некорректный тип: '" + token.getName()
                                    + "'", DinaException.COMPILATION_ERROR);
                        }
                        break;
                    }
                }
                break;
            }
            case Token.ttIdentifier: {//Если тип идентификатор
                Object object = ConstantPool.getConstant(token.getName());//Является ли имя токена именем глобальной константы
                if (object != null) {
                    result = (Node) object;
                } else {
                    if (currentBody instanceof Function) {
                        Function function = (Function) currentBody;
                        object = function.getArgumentVariable(token.getName());//Является ли имя токена именем аргумента
                        if (object != null) {
                            result = (Variable) object;
                        } else {
                            object = function.getVariable(token.getName());//Является ли имя токена именем локальной переменной
                            if (object != null) {
                                result = (Variable) object;
                            } else {
                                result = getIdentifier(function, token);
                                if (result == null) {
                                    result = getIdentifier(program, token);
                                }
                            }
                        }
                    } else {
                        result = getIdentifier(program, token);
                    }
                }
                if (result == null) {
                    String unitName = "";
                    if (program.getName().equals(token.getName())) {
                        unitName = token.getName() + ".";
                        skipSeparator(DOT);
                        token = nextToken();
                        result = getIdentifier(program, token);
                    }
                    if (result == null) {
                        throw new DinaException("Неизвестный идентификатор: '" + unitName + token.getName()
                                + "'", DinaException.COMPILATION_ERROR);
                    }
                }
                break;
            }
        }
        if (result == null) {
            back();
        }
        return A(result);
    }

    public static Node A(Node inputExpression) {
        Node result = inputExpression;
        if (result != null) {
            if (nextToken().getName().equals("[")) {
                if (!result.getNodeType().isArray()) {
                    throw new DinaException("Требуется массив, но '" + result.getNodeType()
                            + "' обнаружено", DinaException.COMPILATION_ERROR);
                }
                Node arrayIndex = getConditionalExpression();
                if (arrayIndex == null) {
                    throw new DinaException("Индекс задан некорректно", DinaException.COMPILATION_ERROR);
                }
                skipSeparator(RBRACKET);
                result = new IndexPointer(Common.checkNode(result), Common.checkNode(arrayIndex), currentBody);
            } else {
                back();
            }
            if (nextToken().getName().equals(".")) {
                result = getFieldPointer(result);
            } else {
                back();
            }
            switch (getOperator(nextToken().getName())) {
                case ASSIGN: {
                    checkVariable(result);
                    Node expression = getConditionalExpression();
                    if (result instanceof Brackets) {
                        result = Common.extractVariable(result);
                    }
                    result = new Assignment((Variable) result, expression);
                    break;
                }
                case PLUSASSIGN: {
                    checkVariable(result);
                    Node expression = getConditionalExpression();
                    if (result instanceof Brackets) {
                        result = Common.extractVariable(result);
                    }
                    Add add = new Add(result, expression);
                    result = new Assignment((Variable) result, add);
                    break;
                }
                case MINUSASSIGN: {
                    checkVariable(result);
                    Node expression = getConditionalExpression();
                    if (result instanceof Brackets) {
                        result = Common.extractVariable(result);
                    }
                    Sub sub = new Sub(result, expression);
                    result = new Assignment((Variable) result, sub);
                    break;
                }
                case STARASSIGN: {
                    checkVariable(result);
                    Node expression = getConditionalExpression();
                    if (result instanceof Brackets) {
                        result = Common.extractVariable(result);
                    }
                    Mul mul = new Mul(result, expression);
                    result = new Assignment((Variable) result, mul);
                    break;
                }
                case SLASHASSIGN: {
                    checkVariable(result);
                    Node expression = getConditionalExpression();
                    if (result instanceof Brackets) {
                        result = Common.extractVariable(result);
                    }
                    Div div = new Div(result, expression);
                    result = new Assignment((Variable) result, div);
                    break;
                }
                case REMASSIGN: {
                    checkVariable(result);
                    Node expression = getConditionalExpression();
                    if (result instanceof Brackets) {
                        result = Common.extractVariable(result);
                    }
                    Mod mod = new Mod(result, expression);
                    result = new Assignment((Variable) result, mod);
                    break;
                }
                case ANDASSIGN: {
                    checkVariable(result);
                    Node expression = getConditionalExpression();
                    if (result instanceof Brackets) {
                        result = Common.extractVariable(result);
                    }
                    And and = new And(result, expression);
                    result = new Assignment((Variable) result, and);
                    break;
                }
                case ORASSIGN: {
                    checkVariable(result);
                    Node expression = getConditionalExpression();
                    if (result instanceof Brackets) {
                        result = Common.extractVariable(result);
                    }
                    Or or = new Or(result, expression);
                    result = new Assignment((Variable) result, or);
                    break;
                }
                default: {
                    back();
                }
            }
        }
        return result;
    }

    public static void checkVariable(Node result) {
        if (result.getNodeClass() != NodeClass.VARIABLE) {
            throw new DinaException("Неожиданный тип. Ожидается '"
                    + NodeClass.NAMES[NodeClass.$VARIABLE] + "', но '"
                    + result.getNodeClass() + "' обнаружено", DinaException.COMPILATION_ERROR);
        }
    }

    public static Node getFieldPointer(Node target) {
        if (target.getNodeType().isArray()) {
            Token token = nextToken();
            if (token.getName().equals("length")) {
                return new ArrayLength(target);
            } else {
                back();
            }
        }
        if (target.getNodeType().getId() == NodeType.T_STRING) {
            Token token = nextToken();
            if (token.getName().equals("length")) {
                return new StringLength(target);
            } else {
                back();
            }
        }
        if (!target.getNodeType().isRecord()) {
            throw new DinaException("is not record", DinaException.COMPILATION_ERROR);
        }
        if (target.getNodeType().isArray()) {
            throw new DinaException("record is array", DinaException.COMPILATION_ERROR);
        }
        FieldPointer fieldPointer = new FieldPointer();
        fieldPointer.setTargetRecord(target);
        Token token = null;
        Field field = null;
        RecordContainer recordContainer = null;
        while (true) {
            token = nextToken();
            if (token.getType() != Token.ttIdentifier) {
                throw new DinaException("is not identifier", DinaException.COMPILATION_ERROR);
            }
            recordContainer = target.getNodeType().getRecordContainer();
            Field currentField = recordContainer.getField(token.getName());
            if (currentField == null) {
                throw new DinaException("field '" + token.getName()
                        + "' is found", DinaException.COMPILATION_ERROR);
            }
            if (nextToken().getName().equals("[")) {
                if (currentField.getNodeType().getDimension() == 0) {
                    throw new DinaException(currentField.getName()
                            + " is not array", DinaException.COMPILATION_ERROR);
                }
                Node index = getConditionalExpression();
                field = new IndexPointerField(currentField.getName(), NodeType.forNodeType(NodeType.forName(currentField.getNodeType().getId())), index, currentField.getFieldIndex());
                skipSeparator(RBRACKET);
            } else {
                back();
                field = new SingleField(currentField.getName(), currentField.getNodeType(), currentField.getFieldIndex());
            }
            if (nextToken().getName().equals(".")) {
                if (field.getNodeType().isArray()) {
                    token = nextToken();
                    if (token.getName().equals("length")) {
                        fieldPointer.setTargetField(field);
                        return new ArrayLength(fieldPointer);
                    } else {
                        back();
                    }
                }
                if (field.getNodeType().getId() == NodeType.T_STRING) {
                    token = nextToken();
                    if (token.getName().equals("length")) {
                        fieldPointer.setTargetField(field);
                        return new StringLength(fieldPointer);
                    } else {
                        back();
                    }
                }
                if (!field.isRecord()) {
                    throw new DinaException("field is not record", DinaException.COMPILATION_ERROR);
                }
                if (field.getNodeType().isArray()) {
                    throw new DinaException("field or record is array", DinaException.COMPILATION_ERROR);
                }
                fieldPointer.addGap(field);
                target = field;
                continue;
            } else {
                fieldPointer.setTargetField(field);
                back();
                break;
            }
        }
        return fieldPointer;
    }

    public static void recordBody() {
        if (getKeyWord(nextToken().getName()) == RECORD) {
            skipSeparator(LBRACE);
            getRecord();
            skipSeparator(RBRACE);
        } else {
            back();
        }
    }

    public static void getRecord() {
        Token token;
        while (true) {
            token = nextToken();
            if (token.getType() == TokenType.ttIdentifier) {
                RecordContainer record = new RecordContainer(token.getName());
                skipSeparator(COLON);
                skipSeparator(LBRACE);
                getFields(record);
                skipSeparator(RBRACE);
                program.addRecord(record);
                continue;
            } else {
                back();
                break;
            }
        }
    }

    public static void getFields(RecordContainer record) {
        Token token = null;
        while (true) {
            token = nextToken();
            if (getSeparator(token.getName()) != RBRACE) {
                back();
                NodeType type = getTypeFull();
                if (type == null) {
                    token = nextToken();
                    throw new DinaException("Некорректный тип переменной: '" + token.getName() + "'",
                            DinaException.COMPILATION_ERROR);
                }
                if (type.getId() != NodeType.T_VOID) {
                    getFieldVariable(record, type);
                    if (getSeparator(nextToken().getName()) == SEMICOLON) {
                        continue;
                    }
                } else {
                    throw new DinaException("Некорректный тип переменной: '" + token.getName() + "'",
                            DinaException.COMPILATION_ERROR);
                }
            } else {
                back();
                return;
            }
        }
    }

    public static void getFieldVariable(RecordContainer record, NodeType type) {
        Token token = null;
        while (true) {
            token = nextToken();
            if (token.getType() == Token.ttIdentifier) {
                nameChecking(record, token.getName());
                int arrayDepth = getDimension();
                Field field = null;
                if (type.isArray() || arrayDepth != 0) {
                    field = new Field(token.getName(), NodeType.getPrototype(type.getId(), 1));
                } else {
                    field = new Field(token.getName(), type);
                }
                record.addFields(field);
                if (getSeparator(nextToken().getName()) == COMMA) {
                    continue;
                } else {
                    back();
                }
                skipSeparator(SEMICOLON);
                back();
                return;
            } else {
                throw new DinaException("Ожидается идентификатор, но '" + token.getName() + "' обнаружено",
                        DinaException.COMPILATION_ERROR);
            }
        }
    }

    public static void createEmptyProgram() {
        String programName = getProgramName();
        program = new Program(programName);
        currentBody = program;
    }

    public static void getConstants(Body body) {
        Token token = null;
        while (true) {
            token = nextToken();
            if (token.getName().equals("}")) {
                back();
                return;
            }
            if (token.getType() == Token.ttIdentifier) {
                String constantName = token.getName();
                if (ConstantPool.getConstant(constantName) != null) {
                    throw new DinaException("Глобальная константа '" + constantName + "' уже существует",
                            DinaException.COMPILATION_ERROR);
                }
                skipOperator(ASSIGN);
                Node constant = getConditionalExpression();
                if (constant.getNodeClass() == NodeClass.CONSTANT) {
                    body.addConstant(constantName, (Constant) constant);
                    skipSeparator(SEMICOLON);
                    continue;
                } else {
                    throw new DinaException("Ожидается константное выражение",
                            DinaException.COMPILATION_ERROR);
                }
            }
            throw new DinaException("Ожидается идентификатор, но '" + token.getName() + "' обнаружено",
                    DinaException.COMPILATION_ERROR);
        }
    }

    public static void constantBody(Body body) {
        if (getKeyWord(nextToken().getName()) == CONST) {
            skipSeparator(LBRACE);
            getConstants(body);
            skipSeparator(RBRACE);
        } else {
            back();
        }
    }

    public static void buildProgram() {
        createEmptyProgram();
        skipSeparator(LBRACE);
        constantBody(program);
        recordBody();
        forwardBody();
        varBody(program);
        findFunction();
        skipSeparator(RBRACE);
        program.checkFunctions();
    }

    public static void getConstructor() {
        Function constructor = new Function(CONSTRUCTOR);
        constructor.setStatement(new Block());
        constructor.getStatement().setFunction(constructor);
        getFunctionBody(constructor);
        if (program.getConstructor() != null) {
            throw new DinaException("В программе уже есть конструктор", DinaException.COMPILATION_ERROR);
        }
        program.setConstructor(constructor);
    }

    public static void getDestructor() {
        Function destructor = new Function(DESTRUCTOR);
        destructor.setStatement(new Block());
        destructor.getStatement().setFunction(destructor);
        getFunctionBody(destructor);
        if (program.getDestructor() != null) {
            throw new DinaException("В программе уже есть деструктор", DinaException.COMPILATION_ERROR);
        }
        program.setDestructor(destructor);
    }

    public static void forwardBody() {
        if (getKeyWord(nextToken().getName()) == FORWARD) {
            skipSeparator(LBRACE);
            findForwadrFunction();
            skipSeparator(RBRACE);
        } else {
            back();
        }
    }

    public static void findForwadrFunction() {
        while (true) {
            if (getKeyWord(nextToken().getName()) == FUNCTION) {
                getForwardFunction();
                continue;
            }
            back();
            return;
        }
    }

    public static void getForwardFunction() {
        String functionName = getFunctionName();
        Function function = new Function();
        function.setName(functionName);
        function.setForward(true);
        getFunctionHead(function);
        program.addFunction(function);
    }

    public static void findFunction() {
        while (true) {
            switch (getKeyWord(nextToken().getName())) {
                case FUNCTION: {
                    getFunction();
                    continue;
                }
                case CONSTRUCTOR: {
                    getConstructor();
                    continue;
                }
                case DESTRUCTOR: {
                    getDestructor();
                    continue;
                }
            }
            back();
            return;
        }
    }

    public static void getFunctionHead(Function function) {
        getFunctionArguments(function);
        if (nextToken().getName().equals(";")) {
            function.setFunctionType(NodeType.TYPE_VOID);
        } else {
            back();
            function.setFunctionType(getFunctionType(function));
            skipSeparator(SEMICOLON);
        }
        function.setStatement(new Block());
        function.getStatement().setFunction(function);
        function.createSignature();
    }

    public static void getFunction() {
        String functionName = getFunctionName();
        Function function = new Function();
        function.setName(functionName);
        getFunctionHead(function);
        Function otherFunction = program.getFunction(function.getSignatureLabel());
        if (otherFunction != null) {
            if (!otherFunction.isForward()) {
                program.addFunction(function);
            }
            if (!otherFunction.getInternalName().equals(function.getInternalName())) {
                throw new DinaException("Заголовок функции '"
                        + function.getSignature()
                        + "' не соответствует предыдущему определению",
                        DinaException.COMPILATION_ERROR);
            }
            otherFunction.setCompleted(true);
            function = otherFunction;
        } else {
            program.addFunction(function);
        }
        getFunctionBody(function);
    }

    public static void getFunctionBody(Function function) {
        constantBody(function);
        currentBody = function;
        varBody(function);
        skipSeparator(LBRACE);
        back();
        findStatements(function.getStatement());
        checkVariables(function);
        Function.checkLabels(function);
    }

    public static void checkVariables(Function function) {
        int variablesWithValue = 0;
        int variablesAmount = function.getVariablesAmount();
        for (int variableIndex = 0; variableIndex < variablesAmount; variableIndex++) {
            Variable variable = function.getVariable(variableIndex);
            if (variable.getValue() != null) {
                Assignment assignment = new Assignment(variable, variable.getValue());
                function.getStatement().insertNode(assignment, variablesWithValue);
                variablesWithValue++;
            }
        }
    }

    public static void findStatements(StatementContainer statement) {
        if (getSeparator(nextToken().getName()) == RBRACE) {
            back();
            return;
        } else {
            back();
        }
        getStatement(statement);
    }

    public static NodeType getFunctionType(Function function) {
        skipSeparator(COLON);
        NodeType functionType = getTypeFull();
        if (functionType == null) {
            throw new DinaException("Некорректная декларация типа", DinaException.COMPILATION_ERROR);
        }
        return functionType;
    }

    public static void getFunctionArguments(Function function) {
        skipSeparator(LPAREN);
        getArgumentVariable(function);
        skipSeparator(RPAREN);
    }

    public static void getArgumentVariable(Function function) {
        Token token = null;
        String duplicateVariableName = null;
        while (true) {
            token = nextToken();
            if (getSeparator(token.getName()) != RPAREN) {
                back();
                NodeType type = getTypeFull();
                if (type == null) {
                    token = nextToken();
                    throw new DinaException("Некорректный тип аргумента: '" + token.getName() + "'",
                            DinaException.COMPILATION_ERROR);
                }
                String varialbeName = getVariableName();
                if (function.getVariable(varialbeName) != null) {
                    duplicateVariableName = varialbeName;
                }
                int arrayDepth = getDimension();
                if (type == NodeType.TYPE_VOID) {
                    throw new DinaException("Тип аргумента не может быть 'void'",
                            DinaException.COMPILATION_ERROR);
                }
                Variable variable = null;
                if (type.isArray() || arrayDepth != 0) {
                    variable = new ArrayVariable(varialbeName, function, NodeType.getType(type.getId()));
                    addArgument(function, type.getId(), 1);
                } else {
                    variable = new SingleVariable(varialbeName, function, type.getId());
                    addArgument(function, type.getId(), 0);
                }
                function.addVariable(variable);
                function.addArgumentVariable(variable);
                if (getSeparator(nextToken().getName()) == COMMA) {
                    switch (nextToken().getType()) {
                        case Token.ttUnknown:
                        case Token.ttSeparator:
                        case Token.ttOperator: {
                            throw new DinaException("Некорректное начало выражения",
                                    DinaException.COMPILATION_ERROR);
                        }
                    }
                    back();
                    continue;
                } else {
                    back();
                    break;
                }
            } else {
                back();
                break;
            }
        }
        if (duplicateVariableName != null) {
            throw new DinaException("Переменная '"
                    + duplicateVariableName + "' в фунции '"
                    + Function.getFullName(function.getSignatureLabel())
                    + "' уже существует", DinaException.COMPILATION_ERROR);
        }
        int argumentsAmount = function.getArgumentsAmount();
        for (int argumentIndex = 0; argumentIndex < argumentsAmount; argumentIndex++) {
            Variable variable = function.getVariable(argumentIndex);
            String variableName = variable.getName();
            Node node = ConstantPool.getConstant(variableName);
            if (node != null) {
                throw new DinaException("Глобальная константа '" + variableName + "' уже существует",
                        DinaException.COMPILATION_ERROR);
            }
        }
    }

    public static void addArgument(Function function, int argumentType, int arrayDepth) {
        Argument argument = new Argument(NodeType.getType(argumentType), arrayDepth);
        Function.addArgument(function, argument);
    }

    public static String getVariableName() {
        Token token = nextToken();
        if (token.getType() == Token.ttIdentifier) {
            return token.getName();
        }
        throw new DinaException("Ожидается идентификатор, но '" + token.getName() + "' обнаружено",
                DinaException.COMPILATION_ERROR);
    }

    public static String getFunctionName() {
        Token token = nextToken();
        if (token.getType() == Token.ttIdentifier) {
            return token.getName();
        }
        throw new DinaException("Ожидается идентификатор, но '" + token.getName() + "' обнаружено",
                DinaException.COMPILATION_ERROR);
    }

    public static String getProgramName() {
        skipKeyWord(PROGRAM);
        Token token = nextToken();
        if (token.getType() == Token.ttIdentifier) {
            return token.getName();
        } else {
            throw new DinaException("Ожидается идентификатор, но '" + token.getName() + "' обнаружено",
                    DinaException.COMPILATION_ERROR);
        }
    }

    public static void varBody(Body body) {
        if (getKeyWord(nextToken().getName()) == VAR) {
            skipSeparator(LBRACE);
            getVariables(body);
            skipSeparator(RBRACE);
        } else {
            back();
        }
    }

    public static void getVariables(Body body) {
        Token token = null;
        while (true) {
            token = nextToken();
            if (getSeparator(token.getName()) != RBRACE) {
                back();
                NodeType type = getTypeFull();
                if (type == null) {
                    token = nextToken();
                    throw new DinaException("Некорректный тип переменной: '" + token.getName() + "'",
                            DinaException.COMPILATION_ERROR);
                }
                if (type.getId() != NodeType.T_VOID) {
                    getVar(body, type);
                    if (getSeparator(nextToken().getName()) == SEMICOLON) {
                        continue;
                    }
                } else {
                    throw new DinaException("Некорректный тип переменной: '" + token.getName() + "'",
                            DinaException.COMPILATION_ERROR);
                }
            } else {
                back();
                return;
            }
        }
    }

    public static void nameChecking(Object body, String name) {
        Object object = ConstantPool.getConstant(name);
        if (object != null) {
            throw new DinaException("Глобальная константа '" + name + "' уже существует",
                    DinaException.COMPILATION_ERROR);
        }
        if (body instanceof Program) {
            Program program = (Program) body;
            object = program.getVariable(name);
            if (object != null) {
                throw new DinaException("Глобальная переменная '" + name + "' уже существует",
                        DinaException.COMPILATION_ERROR);
            }
        }
        if (body instanceof Function) {
            Function function = (Function) body;
            object = function.getArgumentVariable(name);
            if (object != null) {
                throw new DinaException("Аргумент '" + name + "' уже существует",
                        DinaException.COMPILATION_ERROR);
            }
            object = function.getVariable(name);
            if (object != null) {
                throw new DinaException("Переменная '" + name + "' уже существует",
                        DinaException.COMPILATION_ERROR);
            }
        }
    }

    public static void getVar(Body body, NodeType type) {
        Token token = null;
        while (true) {
            token = nextToken();
            if (token.getType() == Token.ttIdentifier) {
                nameChecking(body, token.getName());
                boolean isArray = false;
                if (nextToken().getName().equals("[")) {
                    skipSeparator(RBRACKET);
                    isArray = true;
                } else {
                    back();
                }
                Variable variable = null;
                if (type.isArray() || isArray) {
                    variable = new ArrayVariable(token.getName(), body, NodeType.getPrototype(type.getId(), 1));
                } else {
                    variable = new SingleVariable(token.getName(), body, type.getId());
                }
                body.addVariable(variable);
                if (nextToken().getName().equals("=")) {
                    Node value = getConditionalExpression();
                    if (value == null) {
                        throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
                    }
                    if (!NodeType.compare(variable, value)) {
                        throw new DinaException("Несовместимые типы данных: '"
                                + variable.getNodeType()
                                + "' и '" + value.getNodeType() + "'", DinaException.COMPILATION_ERROR);
                    }
                    variable.setValue(value);
                } else {
                    back();
                }
                if (getSeparator(nextToken().getName()) == COMMA) {
                    continue;
                } else {
                    back();
                }
                skipSeparator(SEMICOLON);
                back();
                return;
            } else {
                throw new DinaException("Ожидается идентификатор, но '" + token.getName() + "' обнаружено",
                        DinaException.COMPILATION_ERROR);
            }
        }
    }

    public static int getOperator(String operatorName) {
        return DinaParser.getOperator(operatorName);
    }

    public static int getKeyWord(String keyWordName) {
        return DinaParser.getKeyWord(keyWordName);
    }

    public static int getSeparator(String separatorName) {
        return DinaParser.getSeparator(separatorName);
    }

    public static void skipSeparator(int separatorIndex) {
        Token token = nextToken();
        if (getSeparator(token.getName()) != separatorIndex) {
            back();
            throw new DinaException("Ожидается '" + ParserConstants.separators[separatorIndex]
                    + "', но '" + token.getName() + "' обнаружено", DinaException.COMPILATION_ERROR);
        }
    }

    public static void skipOperator(int operatorIndex) {
        Token token = nextToken();
        if (getOperator(token.getName()) != operatorIndex) {
            back();
            throw new DinaException("Ожидается '" + ParserConstants.operators[operatorIndex]
                    + "', но '" + token.getName() + "' обнаружено", DinaException.COMPILATION_ERROR);
        }
    }

    public static void skipKeyWord(int keyWordIndex) {
        Token token = nextToken();
        if (getKeyWord(token.getName()) != keyWordIndex) {
            back();
            throw new DinaException("Ожидается '" + ParserConstants.keyWords[keyWordIndex]
                    + "', но '" + token.getName() + "' обнаружено", DinaException.COMPILATION_ERROR);
        }
    }

    public static void skipSemiColonAfterExpression() {
        Token token = nextToken();
        if (!token.getName().equals(";")) {
            throw new DinaException("Незакрытое выражение, ожидается ';', но '"
                    + token.getName() + "' обнаружено", DinaException.COMPILATION_ERROR);
        }
    }

    public static Node createShell(Node node) {
        if (node instanceof FunctionPointer || node instanceof InternalFunctionPointer) {
            return new StackInspector(node);
        }
        return node;
    }

    public static void checkStatement(Node expression) {
        if (!(expression instanceof Assignment
                || expression instanceof FunctionPointer
                || expression instanceof InternalFunctionPointer)) {
            throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
        }
    }

    public static Node getExpression() {
        Node expression = getParenthesisExpression();
        if (expression != null) {
            checkStatement(expression);
            return createShell(expression);
        }
        return null;
    }

    public static boolean getExpression(StatementContainer statement) {
        Node expression = getParenthesisExpression();
        if (expression != null) {
            checkStatement(expression);
            skipSemiColonAfterExpression();
            statement.addNode(createShell(expression));
            return true;
        }
        throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
    }

    public static boolean getStatement(StatementContainer statement) {
        Token token = nextToken();
        if (token.getName().equals("{")) {
            while (!nextToken().getName().equals("}")) {
                back();
                getStatement(statement);
            }
            return true;
        } else {
            Function parentFunction = statement.getFunction();
            int keyWordId = getKeyWord(token.getName());
            switch (keyWordId) {
                case -1: {
                    if (nextToken().getName().equals(":")) {
                        if (token.getType() != Token.ttIdentifier) {
                            throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
                        }
                        String labelName = token.getName();
                        Label label = parentFunction.getLabel(labelName);
                        if (label == null) {
                            label = new Label(token.getName());
                            parentFunction.addLabel(label);
                        }
                        if (!label.isInitialized()) {
                            label.setInitialized();
                        } else {
                            throw new DinaException("Метка '" + token.getName() + "' уже установлена",
                                    DinaException.COMPILATION_ERROR);
                        }
                        label.setParentContainer(statement);
                        LabelStatement labelStmt = new LabelStatement(label);
                        initStatement(statement, labelStmt);
                        statement.addNode(labelStmt);
                        return true;
                    } else {
                        back();
                    }
                    back();
                    return getExpression(statement);
                }
                case NEW: {
                    back();
                    return getExpression(statement);
                }
                case THROW: {
                    skipSeparator(LPAREN);
                    Node exceptionMessage = getConditionalExpression();
                    if (exceptionMessage == null) {
                        throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
                    }
                    if (exceptionMessage.getNodeType().getId() != NodeType.T_STRING
                            || exceptionMessage.getNodeType().getDimension() != 0) {
                        throw new DinaException("Несовместимые типы данных. Нужно: 'string', найдено: '"
                                + exceptionMessage.getNodeType() + "'",
                                DinaException.COMPILATION_ERROR);
                    }
                    skipSeparator(RPAREN);
                    skipSemiColonAfterExpression();
                    statement.addNode(new Throw(exceptionMessage));
                    return true;
                }
                case FOR: {
                    skipSeparator(LPAREN);
                    Node forInit = getExpression();
                    skipSeparator(SEMICOLON);
                    Node expression = getConditionalExpression();
                    skipSeparator(SEMICOLON);
                    Node forUpdate = getExpression();
                    skipSeparator(RPAREN);
                    For forStmt = new For(forInit, expression, forUpdate);
                    initStatement(statement, forStmt);
                    getStatement(forStmt);
                    statement.addNode(forStmt);
                    return true;
                }
                case IF: {
                    skipSeparator(LPAREN);
                    Node expression = getConditionalExpression();
                    skipSeparator(RPAREN);
                    If ifStmt = new If(expression);
                    initStatement(statement, ifStmt);
                    getStatement(ifStmt);
                    if (getKeyWord(nextToken().getName()) == ELSE) {
                        initStatement(statement, ifStmt.getElseBlock());
                        getStatement(ifStmt.getElseBlock());
                    } else {
                        back();
                    }
                    statement.addNode(ifStmt);
                    return true;
                }
                case WHILE: {
                    skipSeparator(LPAREN);
                    Node expression = getConditionalExpression();
                    skipSeparator(RPAREN);
                    While whileStmt = new While(expression);
                    initStatement(statement, whileStmt);
                    if (!nextToken().getName().equals(";")) {
                        back();
                        getStatement(whileStmt);
                    }
                    statement.addNode(whileStmt);
                    return true;
                }
                case TRY: {
                    StatementContainer tryContainer = new Block(TRY);
                    StatementContainer catchContainer = new Block();
                    checkBrace();
                    initStatement(statement, tryContainer);
                    getStatement(tryContainer);
                    skipKeyWord(CATCH);
                    checkBrace();
                    initStatement(statement, catchContainer);
                    getStatement(catchContainer);
                    TryCatch throwBlock = new TryCatch(tryContainer, catchContainer);
                    initStatement(statement, throwBlock);
                    statement.addNode(throwBlock);
                    return true;
                }
                case DO: {
                    Do doStmt = new Do();
                    initStatement(statement, doStmt);
                    getStatement(doStmt);
                    skipKeyWord(WHILE);
                    skipSeparator(LPAREN);
                    Node expression = getConditionalExpression();
                    skipSeparator(RPAREN);
                    skipSeparator(SEMICOLON);
                    Do.setExpression(doStmt, expression);
                    statement.addNode(doStmt);
                    return true;
                }
                case SWITCH: {
                    skipSeparator(LPAREN);
                    Node expression = getConditionalExpression();
                    if (expression == null) {
                        throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
                    }
                    skipSeparator(RPAREN);
                    if (expression.getNodeType().isInteger()
                            && expression.getNodeType().getDimension() == 0) {
                        Switch switchStmt = new Switch(expression);
                        initStatement(statement, switchStmt);
                        skipSeparator(LBRACE);
                        while (true) {
                            token = nextToken();
                            if (token.getName().equals("}")) {
                                statement.addNode(switchStmt);
                                return true;
                            }
                            switch (getKeyWord(token.getName())) {
                                default: {
                                    throw new DinaException("Ожидается 'case', 'default' или '}', но '"
                                            + token.getName() + "' обнаружено", DinaException.COMPILATION_ERROR);
                                }
                                case CASE: {
                                    Block caseContainer = new Block();
                                    initStatement(switchStmt, caseContainer);
                                    while (true) {
                                        Node caseLabel = getConditionalExpression();
                                        if (caseLabel == null) {
                                            throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
                                        }
                                        if (!caseLabel.getNodeType().isInteger()) {
                                            throw new DinaException("Несовместимые типы данных. Нужно: 'integer', найдено: '"
                                                    + caseLabel.getNodeType() + "'",
                                                    DinaException.COMPILATION_ERROR);
                                        }
                                        if (caseLabel.getNodeClass() == NodeClass.CONSTANT) {
                                            switchStmt.addCaseLabel(caseLabel.getIntegerValue(), caseContainer);
                                            token = nextToken();
                                            if (token.getName().equals(",")) {
                                                continue;
                                            } else if (token.getName().equals(":")) {
                                                break;
                                            } else {
                                                throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
                                            }
                                        }
                                        throw new DinaException("Ожидается константное выражение",
                                                DinaException.COMPILATION_ERROR);
                                    }
                                    getStatement(caseContainer);
                                    break;
                                }
                                case DEFAULT: {
                                    skipSeparator(COLON);
                                    Block defaultContainer = new Block();
                                    initStatement(statement, defaultContainer);
                                    getStatement(defaultContainer);
                                    switchStmt.setDefaultContainer(defaultContainer);
                                    break;
                                }
                            }
                        }
                    }
                    throw new DinaException("Несовместимые типы данных. Нужно: 'integer', найдено: '"
                            + expression.getNodeType() + "'",
                            DinaException.COMPILATION_ERROR);
                }
                case GOTO: {
                    GotoStatement gotoStmt = new GotoStatement();
                    initStatement(statement, gotoStmt);
                    token = nextToken();
                    switch (token.getType()) {
                        case Token.ttIdentifier: {
                            String labelName = token.getName();
                            Label label = parentFunction.getLabel(labelName);
                            skipSemiColonAfterExpression();
                            if (label == null) {
                                label = new Label(labelName);
                                parentFunction.addLabel(label);
                            }
                            label.setCalled();
                            gotoStmt.setLabel(label);
                            label.getReferencing().push(gotoStmt);
                            statement.addNode(gotoStmt);
                            return true;
                        }
                        default: {
                            throw new DinaException("Некорректное начало выражения: '" + token.getName() + "'",
                                    DinaException.COMPILATION_ERROR);
                        }
                    }
                }
                case RETURN: {
                    Node expression = getConditionalExpression();
                    skipSemiColonAfterExpression();
                    if (parentFunction.getNodeType().getId() == NodeType.T_VOID && expression != null) {
                        String bodyName = "Функция '" + parentFunction.getSignature() + "'";
                        switch (parentFunction.getBlockId()) {
                            case CONSTRUCTOR: {
                                bodyName = "Конструктор";
                                break;
                            }
                            case DESTRUCTOR: {
                                bodyName = "Деструктор";
                                break;
                            }
                        }
                        throw new DinaException(bodyName + " не может вернуть значение", DinaException.COMPILATION_ERROR);
                    } else {
                        if (parentFunction.getNodeType().getId() != NodeType.T_VOID) {
                            if (expression == null) {
                                throw new DinaException("Функция '"
                                        + parentFunction.getSignature()
                                        + "' должна вернуть значение типа '"
                                        + parentFunction.getNodeType() + "'",
                                        DinaException.COMPILATION_ERROR);
                            }
                            if (!NodeType.compare(parentFunction, expression)) {
                                Common.incomparableTypesException(parentFunction, expression);
                            }
                        }
                    }
                    Return returnStmt = new Return(expression);
                    initStatement(statement, returnStmt);
                    statement.addNode(returnStmt);
                    return true;
                }
                case BREAK:
                case CONTINUE: {
                    skipSemiColonAfterExpression();
                    CycleManagement cm = new CycleManagement(keyWordId);
                    initStatement(statement, cm);
                    //check cycle
                    StatementContainer cycle = null;
                    int[] cycles = {WHILE, DO, FOR};
                    for (int index = 0; index < cycles.length; index++) {
                        cycle = AbstractStatement.forStatement(cycles[index], cm);
                        if (cycle != null) {
                            break;
                        }
                    }
                    if (cycle == null) {
                        throw new DinaException("Оператор '" + ParserConstants.keyWords[cm.getCycleManagementType()]
                                + "' за предемами цикла", DinaException.COMPILATION_ERROR);
                    }
                    cm.setCycleContainer(cycle);
                    statement.addNode(cm);
                    return true;
                }
                case EXIT: {
                    skipSemiColonAfterExpression();
                    Exit exit = new Exit();
                    initStatement(statement, exit);
                    StatementContainer switchContainer = null;
                    switchContainer = AbstractStatement.forStatement(SWITCH, exit);
                    if (switchContainer == null) {
                        throw new DinaException("Оператор 'exit' за предемами блока 'switch'", DinaException.COMPILATION_ERROR);
                    }
                    exit.setSwitchContainer((Switch) switchContainer);
                    statement.addNode(exit);
                    return true;
                }
                default: {
                    throw new DinaException("Некорректное начало выражения: '" + token.getName() + "'",
                            DinaException.COMPILATION_ERROR);
                }
            }
        }
    }

    public static void initStatement(StatementContainer parent, AbstractStatement child) {
        child.setParentContainer(parent);
        child.setFunction(parent.getFunction());
    }

    public static Function forFunction(String signatureLabel, SetTable<String, Function> functionTable) {
        Signature signature = Signature.getSignature(signatureLabel);
        return forFunction(signature, functionTable);
    }

    public static Function forFunction(Signature signature, SetTable<String, Function> functionTable) {
        Argument[] arguments = signature.getFunctionArguments();
        int length = arguments.length;
        if (length != 0) {
            String functionName = signature.getFunctionName();
            Vector rivalFunctions = exclude(functionName, functionTable, length);
            int count = rivalFunctions.size();
            if (count == 0) {
                return null;
            }
            NodeType[] types = new NodeType[length];
            Enumeration functions = rivalFunctions.elements();
            Argument[] other;
            Function nextFunction;
            while (functions.hasMoreElements()) {
                nextFunction = (Function) functions.nextElement();
                other = nextFunction.getSignature().getFunctionArguments();
                for (int index = 0; index < length; index++) {
                    NodeType a1 = other[index].getNodeType();
                    NodeType a2 = arguments[index].getNodeType();
                    if (a1.getDimension() != a2.getDimension()) {
                        rivalFunctions.removeElement(nextFunction);
                        functions = rivalFunctions.elements();
                        break;
                    }
                    if (a1.getId() == a2.getId()) {
                        types[index] = arguments[index].getNodeType();
                        continue;
                    } else if (Common.isNumeric(a1) && Common.isNumeric(a2)) {
                    } else {
                        rivalFunctions.removeElement(nextFunction);
                        functions = rivalFunctions.elements();
                        break;
                    }
                }
            }
            StringBuilder functionSignatureLabel = new StringBuilder();
            functionSignatureLabel.append(functionName).append('(');
            for (int index = 0; index < length; index++) {
                functionSignatureLabel.append(types[index].getSignature());
            }
            functionSignatureLabel.append(')');
            return functionTable.getValue(functionSignatureLabel.toString());
        }
        return functionTable.getValue(signature.toString());
    }

    public static Vector exclude(String functionName, SetTable<String, Function> functionTable, int argumentsCount) {
        Vector result = new Vector();
        Enumeration<Function> arguments = functionTable.elements();
        Function function;
        while (arguments.hasMoreElements()) {
            function = arguments.nextElement();
            if (function.getArguments().length == argumentsCount) {
                if (function.getName().equalsIgnoreCase(functionName)) {
                    result.addElement(function);
                }
            }
        }
        return result;
    }

    public static void checkBrace() {
        skipSeparator(LBRACE);
        back();
    }

    public static NodeType getTypeQuick() {
        Token token = nextToken();
        int tokenType = token.getType();
        NodeType nodeType = null;
        if (tokenType == Token.ttIdentifier || tokenType == Token.ttKeyWord) {
            String typeName = token.getName();
            nodeType = NodeType.forNodeType(typeName);
        }
        if (nodeType == null) {
            back();
        }
        return nodeType;
    }

    public static NodeType getTypeFull() {
        NodeType nodeType = getTypeQuick();
        if (nodeType != null) {
            int dimension = getDimension();
            if (nodeType == NodeType.TYPE_VOID && dimension != 0) {
                throw new DinaException("Некорректная декларация типа", DinaException.COMPILATION_ERROR);
            }
            return NodeType.getPrototype(nodeType.getId(), dimension);
        }
        return null;
    }

    public static int getDimension() {
        int dimension = 0;
        Token token = nextToken();
        switch (getSeparator(token.getName())) {
            case LBRACKET: {
                skipSeparator(RBRACKET);
                dimension++;
                break;
            }
            default: {
                back();
            }
        }
        return dimension;
    }
}
