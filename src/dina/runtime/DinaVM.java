package dina.runtime;

import dina.runtime.variables.record.*;
import ide.*;
import ide.console.AbstractConsole;
import ide.console.Console;
import java.io.*;
import dina.runtime.variables.*;
import dina.*;
import dina.library.*;
import dina.library.libs.*;
import ide.console.terminal.*;
import java.util.*;

public class DinaVM implements Runnable, SystemConstants {

    public static final int TRUE = 1;
    public static final int FALSE = 0;
    public static boolean successfulExecution;
    public static DinaVM instance = getInstance();
    public static String lastErrorMessage;
    private static boolean work;
    //
    public static Library[] librarys;
    //
    private static Thread thread;
    private static DataInputStream input;
    //
    public static final int OFFSET = 4;//
    public static final int MEMOTY_SIZE = 1024;//размер стека
    private static int entryPoint;//точка входа в программу, адрес функции main()V
    private static int constructorAddress = -1;//конструктор
    private static int destructorAddress = -1;//деструктор
    private static int functionCount;//количество функций
    private static int[] instructions;//набор инструций
    //
    private static Object[] floatConstants;//дробные константы
    private static Object[] stringConstants;//строковые константы
    //
    private static int[] callBack;
    private static int callBackPointer = -1;
    //
    public static Object[] operands;
    public static int[] types;
    public static int pointer = -1;
    //
    private static Variable[] global;
    public static Variable[] prototypes;
    public static String[] names;
    private static Variable[] basicTypes = {
        new BooleanVariable(),
        new CharVariable(),
        new IntVariable(),
        new FloatVariable(),
        new StringVariable(),
        new ObjectVariable()
    };
    private static String[] basicTypesName = {
        "boolean",
        "char",
        "int",
        "float",
        "string",
        "object"
    };
    //
    private static Debugger debugContext;
    private static Stack<Integer> debugStack;

    private DinaVM() {
        initLibrarys();
    }

    public static DinaVM getInstance() {
        if (instance == null) {
            instance = new DinaVM();
        }
        return instance;
    }

    private static void initLibrarys() {
        librarys = new Library[]{
                    new SystemLibrary(),
                    new GraphicsLibrary(),
                    new MathLibrary(),
                    new ConsoleLibrary(),
                    new StringLibrary(),
                    new IOLibrary(),
                    new EventsLibrary(),
                    new UILibrary(),
                    new DateLibrary()
                };
        for (int libIndex = 0; libIndex < librarys.length; libIndex++) {
            librarys[libIndex].initLibrary();
        }
    }

    public static void run(String unitName) {
        InputStream inputStream = unitName.getClass().getResourceAsStream(unitName);
        boot(inputStream);
    }

    public static void run(byte[] byteArray, Debugger debugContext) {
        DinaVM.debugContext = debugContext;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        boot(byteArrayInputStream);
    }

    private static void boot(InputStream inputStream) {
        resetPointers();
        clearMemory();
        input = new DataInputStream(inputStream);
        runThread();
    }

    public static void runThread() {
        work = true;
        thread = new Thread(instance, "runtime");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public static void haltThread() {
        if (thread != null) {
            work = false;
            thread = null;
        }
    }

    private static void readFloatConstants() throws Exception {
        int count = input.readShort();
        if (count != 0) {
            floatConstants = new float[count][1];
            for (int index = 0; index < count; index++) {
                floatConstants[index] = new float[]{input.readFloat()};
            }
        }
    }

    private static void readStringConstants() throws Exception {
        int count = input.readShort();
        if (count != 0) {
            stringConstants = new String[count][1];
            for (int index = 0; index < count; index++) {
                stringConstants[index] = new String[]{input.readUTF()};
            }
        }
    }

    private static void readBootInfo() throws Exception {
        functionCount = input.readShort();
        entryPoint = input.readInt();
        constructorAddress = input.readInt();
        destructorAddress = input.readInt();
    }

    private static void readConstants() throws Exception {
        readFloatConstants();
        readStringConstants();
    }

    private static void decode() throws Exception {
        int length = input.readInt();
        if (length != 0) {
            int position = 0;
            short opcode = -1;
            int functionIndex = 0;
            instructions = new int[length];
            while (functionIndex++ != functionCount) {
                opcode = input.readByte();
                switch (opcode) {
                    case Constants.BEGIN_FUNCTION: {
                        int varCount = 0;
                        {//variables count
                            int b1 = instructions[position++] = input.read();
                            int b2 = instructions[position++] = input.read();
                            varCount = (b1 << 8) + b2;
                        }
                        {//arguments count
                            instructions[position++] = input.read();
                        }
                        {//get variables
                            if (varCount != 0) {
                                for (int varIndex = 0; varIndex < varCount; varIndex++) {
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.readByte();
                                }
                            }
                        }
                        boolean breakLoop = false;
                        while (!breakLoop) {
                            opcode = input.readByte();
                            switch (opcode) {
                                case Constants.OALOAD:
                                case Constants.OASTORE_0:
                                case Constants.OASTORE_1:
                                case Constants.STRINGLENGTH:
                                case Constants.DUP:
                                case Constants.THROW:
                                case Constants.S2F:
                                case Constants.C2S:
                                case Constants.F2S:
                                case Constants.SASTORE_1:
                                case Constants.IASTORE_1:
                                case Constants.FASTORE_1:
                                case Constants.RASTORE_1:
                                case Constants.IADD:
                                case Constants.FADD:
                                case Constants.ISUB:
                                case Constants.FSUB:
                                case Constants.IDIV:
                                case Constants.FDIV:
                                case Constants.IMUL:
                                case Constants.FMUL:
                                case Constants.IMOD:
                                case Constants.FMOD:
                                case Constants.RETURN:
                                case Constants.I2F:
                                case Constants.I2S:
                                case Constants.OR:
                                case Constants.AND:
                                case Constants.NOT:
                                case Constants.IF_SCMPEQ:
                                case Constants.IF_SCMPNE:
                                case Constants.SCONCAT:
                                case Constants.INEG:
                                case Constants.FNEG:
                                case Constants.IF_ICMPEQ:
                                case Constants.IF_ICMPNE:
                                case Constants.IF_ICMPLT:
                                case Constants.IF_ICMPLE:
                                case Constants.IF_ICMPGT:
                                case Constants.IF_ICMPGE:
                                case Constants.IF_FCMPEQ:
                                case Constants.IF_FCMPNE:
                                case Constants.IF_FCMPLT:
                                case Constants.IF_FCMPLE:
                                case Constants.IF_FCMPGT:
                                case Constants.IF_FCMPGE:
                                case Constants.POP:
                                case Constants.IALOAD:
                                case Constants.IASTORE_0:
                                case Constants.FALOAD:
                                case Constants.FASTORE_0:
                                case Constants.SALOAD:
                                case Constants.SASTORE_0:
                                case Constants.RALOAD:
                                case Constants.RASTORE_0:
                                case Constants.I2C:
                                case Constants.QUIT:
                                case Constants.S2I: {
                                    instructions[position++] = opcode;
                                    continue;
                                }
                                case Constants.ZCONST:
                                case Constants.BCONST: {
                                    instructions[position++] = opcode;
                                    instructions[position++] = input.readByte();
                                    continue;
                                }
                                case Constants.NEW: {
                                    instructions[position++] = opcode;
                                    instructions[position++] = input.readByte();
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    continue;
                                }
                                case Constants.CCONST:
                                case Constants.SICONST:
                                case Constants.ARRAYLENGTH:
                                case Constants.ACONCAT:
                                case Constants.IF_ACMPEQ:
                                case Constants.IF_ACMPNE:
                                case Constants.NEWARRAY:
                                case Constants.PUTFIELD:
                                case Constants.GETFIELD:
                                case Constants.FCONST:
                                case Constants.SCONST:
                                case Constants.LLOAD:
                                case Constants.LSTORE:
                                case Constants.GLOAD:
                                case Constants.GSTORE: {
                                    instructions[position++] = opcode;
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    continue;
                                }
                                case Constants.INVOKENATIVE: {
                                    instructions[position++] = opcode;
                                    instructions[position++] = input.readByte();
                                    instructions[position++] = input.readByte();
                                    continue;
                                }
                                case Constants.INVOKE:
                                case Constants.ICONST:
                                case Constants.IFEQ:
                                case Constants.IFNE:
                                case Constants.GOTO:
                                case Constants.BREAK_TRY:
                                case Constants.TRY: {
                                    instructions[position++] = opcode;
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    continue;
                                }
                                case Constants.TABLESWITCH: {
                                    instructions[position++] = opcode;
                                    int casesAmount = input.readShort();
                                    instructions[position++] = ((casesAmount >>> 8) & 0xFF);
                                    instructions[position++] = (casesAmount & 0xFF);
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    for (int caseIndex = 0; caseIndex < casesAmount; caseIndex++) {
                                        for (int index = 0; index < 8; index++) {
                                            instructions[position++] = input.read();
                                        }
                                    }
                                    continue;
                                }
                                case Constants.CHECKCAST:
                                case Constants.TYPEOF: {
                                    instructions[position++] = opcode;
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.read();
                                    instructions[position++] = input.readByte();
                                    continue;
                                }
                                case Constants.END_FUNCTION: {
                                    breakLoop = true;
                                    continue;
                                }
                                default: {
                                    throw new RuntimeException("opcode: " + opcode);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } else {
            throw new RuntimeException("Структура файла повреждена");
        }
    }

    private static void crealeGlobalVariables() throws Exception {
        int varCount = input.readShort();
        if (varCount != 0) {
            global = new Variable[varCount];
            for (int varIndex = 0; varIndex < varCount; varIndex++) {
                int id = input.readShort();
                int dimension = input.readByte();
                global[varIndex] = prototypes[id].newInstance(dimension);
            }
        }
    }

    private static void readHeader() throws Exception {
        int header = input.readInt();
        if (header != HEADER) {
            throw new RuntimeException("Некорректный заголовок файла");
        }
        float version = input.readFloat();
        if (version != VERSION) {
            throw new RuntimeException("Несовместимые версии: '" + version + "' и '" + VERSION + "'");
        }
    }

    private static void initBasicTypes() {
        System.arraycopy(basicTypes, 0, prototypes, 0, basicTypes.length);
        System.arraycopy(basicTypesName, 0, names, 0, basicTypesName.length);
    }

    private static void readRecords() throws Exception {
        int recordsAmount = input.readShort();
        prototypes = new Variable[basicTypes.length + recordsAmount];
        names = new String[prototypes.length];
        initBasicTypes();
        if (recordsAmount != 0) {
            for (int recordIndex = 0; recordIndex < recordsAmount; recordIndex++) {
                short id = input.readShort();
                names[id] = input.readUTF();
                if (Debugger.DEBUG) {
                    System.out.println("RECORD NAME: " + names[id]);
                }
                short fieldsAmount = input.readShort();
                int[] fieldsDimension = new int[fieldsAmount];
                int[] fieldsId = new int[fieldsAmount];
                if (fieldsAmount != 0) {
                    for (int fieldIndex = 0; fieldIndex < fieldsAmount; fieldIndex++) {
                        fieldsId[fieldIndex] = input.readShort();
                        fieldsDimension[fieldIndex] = input.readByte();
                        if (Debugger.DEBUG) {
                            System.out.println("FIELD #" + fieldIndex + ": " + getClassName(fieldsId[fieldIndex], fieldsDimension[fieldIndex]));
                        }
                    }
                }
                RecordContext recordContext = new RecordContext();
                recordContext.id = id;
                recordContext.fieldsArrayDimension = fieldsDimension;
                recordContext.fieldsId = fieldsId;
                recordContext.fieldsAmount = fieldsAmount;
                prototypes[id] = new Record(recordContext);
                if (Debugger.DEBUG) {
                    System.out.println("--------------------------------");
                }
            }
        }
    }

    private static void initialization() throws Exception {
        readHeader();
        readConstants();
        readRecords();
        crealeGlobalVariables();
        readBootInfo();
        decode();
        callBack = new int[MEMOTY_SIZE];
        operands = new Object[MEMOTY_SIZE];
        types = new int[MEMOTY_SIZE];
        if (Debugger.DEVELOPMENT_MODE) {
            debugStack = new Stack<Integer>();
        }
        input = null;
    }
    private static int codePosition;

    private static void execute(int address, Variable[] variables, boolean callSub, int[] instructions) {
        codePosition = address;
        Object[] operands = DinaVM.operands;
        int[] types = DinaVM.types;
        Variable[] local = null;
        if (callSub) {//если вызов подпрограммы...
            if (Debugger.DEVELOPMENT_MODE) {
                debugStack.push(codePosition);
                successfulExecution = false;
            }
            {//инициализация аргументов
                int varCount = (instructions[codePosition++] << 8) + instructions[codePosition++];//Количество переменных
                int argumentsCount = instructions[codePosition++];//Количество аргументов
                if (varCount != 0) {
                    local = new Variable[varCount];
                    for (int variableIndex = 0; variableIndex < varCount; variableIndex++) {
                        int id = (instructions[codePosition++] << 8) + instructions[codePosition++];
                        int dimension = instructions[codePosition++];
                        local[variableIndex] = prototypes[id].newInstance(dimension);
                    }
                    Variable localVariable;
                    for (int argumentIndex = 0; argumentIndex < argumentsCount; argumentIndex++) {
                        localVariable = local[argumentIndex];
                        localVariable.value = operands[pointer];
                        localVariable.id = (types[pointer] >> 16) & 0xff;
                        localVariable.dimension = types[pointer] & 0xff;
                        pointer--;
                    }
                }
            }
        } else {//...иначе вход в блок try
            local = variables;
        }
        int opcode;
        start:
        do {
            opcode = instructions[codePosition++];
            switch (opcode) {
                case Constants.LLOAD: {
                    int varIndex = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    ++pointer;
                    operands[pointer] = local[varIndex].value;
                    types[pointer] = (local[varIndex].id << 16) + local[varIndex].dimension;
                    continue;
                }
                case Constants.GLOAD: {
                    int varIndex = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    ++pointer;
                    operands[pointer] = global[varIndex].value;
                    types[pointer] = (global[varIndex].id << 16) + global[varIndex].dimension;
                    continue;
                }
                case Constants.LSTORE: {
                    int varIndex = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    local[varIndex].value = operands[pointer];
                    local[varIndex].id = (types[pointer] >> 16) & 0xff;
                    local[varIndex].dimension = types[pointer] & 0xff;
                    pointer--;
                    continue;
                }
                case Constants.GSTORE: {
                    int varIndex = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    global[varIndex].value = operands[pointer];
                    global[varIndex].id = (types[pointer] >> 16) & 0xff;
                    global[varIndex].dimension = types[pointer] & 0xff;
                    pointer--;
                    continue;
                }
                case Constants.INVOKE: {
                    callBack[++callBackPointer] = codePosition + 4;
                    execute((instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++], null, true, instructions);
                    continue;
                }
                case Constants.INVOKENATIVE: {
                    if (Debugger.DEVELOPMENT_MODE) {
                        int libraryId = instructions[codePosition++];
                        int functionId = instructions[codePosition++];
                        int currentNativeFunction = ((libraryId + 1) << 8) + (functionId + 1);
                        debugStack.push(-currentNativeFunction);
                        librarys[libraryId].invoke(functionId);
                        debugStack.pop();
                        continue;
                    } else {
                        librarys[instructions[codePosition++]].invoke(instructions[codePosition++]);
                    }
                    continue;
                }
                case Constants.RETURN: {
                    if (callBackPointer > -1) {
                        codePosition = callBack[callBackPointer--];
                        if (Debugger.DEVELOPMENT_MODE) {
                            debugStack.pop();
                        }
                        return;
                    }
                    if (Debugger.DEVELOPMENT_MODE) {
                        successfulExecution = true;
                    }
                    return;
                }
                case Constants.GOTO: {
                    codePosition = (instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++];
                    continue;
                }
                case Constants.PUTFIELD: {
                    RecordContainer container = ((RecordContainer) ((Variable[]) ((Object[]) operands[pointer--])[Record.OBJECT])[0]);
                    Variable field = container.fields[(instructions[codePosition++] << 8) + instructions[codePosition++]];
                    field.value = operands[pointer];
                    field.id = (types[pointer] >> 16) & 0xff;
                    field.dimension = types[pointer] & 0xff;
                    pointer--;
                    continue;
                }
                case Constants.GETFIELD: {
                    RecordContainer container = ((RecordContainer) ((Variable[]) ((Object[]) operands[pointer--])[Record.OBJECT])[0]);
                    Variable field = container.fields[(instructions[codePosition++] << 8) + instructions[codePosition++]];
                    ++pointer;
                    operands[pointer] = field.value;
                    types[pointer] = (field.id << 16) + field.dimension;
                    continue;
                }
                case Constants.TABLESWITCH: {
                    int value = ((int[]) operands[pointer--])[0];
                    int casesAmount = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    int defaultAddress = (instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++];
                    for (int caseIndex = 0; caseIndex < casesAmount; caseIndex++) {
                        if (value == (instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++]) {
                            codePosition = (instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++];
                            continue start;
                        } else {
                            codePosition += OFFSET;
                            continue;
                        }
                    }
                    codePosition = defaultAddress;
                    continue;
                }
                case Constants.NEWARRAY: {
                    int size = ((int[]) operands[pointer--])[0];
                    int id = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    ++pointer;
                    switch (id) {
                        case TypeContext.BOOLEAN:
                        case TypeContext.CHAR:
                        case TypeContext.INT: {
                            operands[pointer] = new int[size];
                            break;
                        }
                        case TypeContext.FLOAT: {
                            operands[pointer] = new float[size];
                            break;
                        }
                        case TypeContext.STRING: {
                            operands[pointer] = new String[size];
                            break;
                        }
                        case TypeContext.OBJECT: {
                            if (size < 0) {
                                throw new NegativeArraySizeException();
                            }
                            operands[pointer] = new Object[size * 2];
                            break;
                        }
                        default: {
                            operands[pointer] = new Object[]{new Variable[size], new int[]{id}};
                            break;
                        }
                    }
                    types[pointer] = ((id << 16) + TypeContext.ARRAY);
                    continue;
                }
                case Constants.ARRAYLENGTH: {
                    int length;
                    int id = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    switch (id) {
                        case TypeContext.BOOLEAN:
                        case TypeContext.CHAR:
                        case TypeContext.INT: {
                            length = ((int[]) operands[pointer--]).length;
                            break;
                        }
                        case TypeContext.FLOAT: {
                            length = ((float[]) operands[pointer--]).length;
                            break;
                        }
                        case TypeContext.STRING: {
                            length = ((String[]) operands[pointer--]).length;
                            break;
                        }
                        case TypeContext.OBJECT: {
                            length = ((Object[]) operands[pointer--]).length / 2;
                            break;
                        }
                        default: {
                            length = ((Variable[]) ((Object[]) operands[pointer--])[Record.OBJECT]).length;
                            break;
                        }
                    }
                    ++pointer;
                    operands[pointer] = new int[]{length};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.STRINGLENGTH: {
                    String value = ((String[]) operands[pointer--])[0];
                    int length = value.length();
                    ++pointer;
                    operands[pointer] = new int[]{length};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IALOAD: {
                    int index = ((int[]) operands[pointer--])[0];
                    int[] array = (int[]) operands[pointer--];
                    ++pointer;
                    operands[pointer] = new int[]{array[index]};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.FALOAD: {
                    int index = ((int[]) operands[pointer--])[0];
                    float[] array = (float[]) operands[pointer--];
                    ++pointer;
                    operands[pointer] = new float[]{array[index]};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.SALOAD: {
                    int index = ((int[]) operands[pointer--])[0];
                    String[] array = (String[]) operands[pointer--];
                    ++pointer;
                    operands[pointer] = array[index] == null ? StringVariable.EMPTY_STRING : new String[]{array[index]};
                    types[pointer] = ((TypeContext.STRING << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.RALOAD: {
                    int index = ((int[]) operands[pointer--])[0];
                    Object[] obj = (Object[]) operands[pointer--];
                    Variable[] array = (Variable[]) obj[Record.OBJECT];
                    int id = ((int[]) obj[Record.ID])[Record.ID_VALUE];
                    if (array[index] == null) {
                        array[index] = new RecordContainer(((Record) prototypes[id]).recordContext);
                    }
                    ++pointer;
                    operands[pointer] = new Object[]{new Variable[]{array[index]}, new int[]{id}};
                    types[pointer] = ((id << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.OALOAD: {
                    int index = ((int[]) operands[pointer--])[0] * 2;
                    Object[] array = (Object[]) operands[pointer--];
                    ++pointer;
                    operands[pointer] = array[index] == null ? null : array[index];
                    types[pointer] = (((int[]) array[index + 1]))[0];
                    continue;
                }
                case Constants.ACONCAT: {
                    int id = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    switch (id) {
                        case TypeContext.OBJECT: {
                            Object[] src2 = (Object[]) operands[pointer--];
                            Object[] src1 = (Object[]) operands[pointer--];
                            Object[] dest = new Object[src1.length + src2.length];
                            System.arraycopy(src1, 0, dest, 0, src1.length);
                            System.arraycopy(src2, 0, dest, src1.length, src2.length);
                            ++pointer;
                            operands[pointer] = dest;
                            types[pointer] = ((TypeContext.OBJECT << 16) + TypeContext.ARRAY);
                            continue;
                        }
                        case TypeContext.STRING: {
                            String[] src2 = (String[]) operands[pointer--];
                            String[] src1 = (String[]) operands[pointer--];
                            String[] dest = new String[src1.length + src2.length];
                            System.arraycopy(src1, 0, dest, 0, src1.length);
                            System.arraycopy(src2, 0, dest, src1.length, src2.length);
                            ++pointer;
                            operands[pointer] = dest;
                            types[pointer] = ((TypeContext.STRING << 16) + TypeContext.ARRAY);
                            continue;
                        }
                        case TypeContext.FLOAT: {
                            float[] src2 = (float[]) operands[pointer--];
                            float[] src1 = (float[]) operands[pointer--];
                            float[] dest = new float[src1.length + src2.length];
                            System.arraycopy(src1, 0, dest, 0, src1.length);
                            System.arraycopy(src2, 0, dest, src1.length, src2.length);
                            ++pointer;
                            operands[pointer] = dest;
                            types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.ARRAY);
                            continue;
                        }
                        case TypeContext.BOOLEAN:
                        case TypeContext.CHAR:
                        case TypeContext.INT: {
                            int[] src2 = (int[]) operands[pointer--];
                            int[] src1 = (int[]) operands[pointer--];
                            int[] dest = new int[src1.length + src2.length];
                            System.arraycopy(src1, 0, dest, 0, src1.length);
                            System.arraycopy(src2, 0, dest, src1.length, src2.length);
                            ++pointer;
                            operands[pointer] = dest;
                            types[pointer] = ((id << 16) + TypeContext.ARRAY);
                            continue;
                        }
                        default: {
                            Object[] recordShell1 = (Object[]) operands[pointer--];
                            Object[] recordShell2 = (Object[]) operands[pointer--];
                            Variable[] src2 = (Variable[]) recordShell1[Record.OBJECT];
                            Variable[] src1 = (Variable[]) recordShell2[Record.OBJECT];
                            Variable[] dest = new Variable[src1.length + src2.length];
                            System.arraycopy(src1, 0, dest, 0, src1.length);
                            System.arraycopy(src2, 0, dest, src1.length, src2.length);
                            ++pointer;
                            operands[pointer] = new Object[]{dest, new int[]{id}};
                            types[pointer] = ((id << 16) + TypeContext.ARRAY);
                            continue;
                        }
                    }
                }
                case Constants.IF_ACMPEQ: {
                    int id = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    switch (id) {
                        case TypeContext.STRING: {
                            String[] a1 = (String[]) operands[pointer--];
                            String[] a2 = (String[]) operands[pointer--];
                            ++pointer;
                            operands[pointer] = new int[]{equals(a1, a2)};
                            break;
                        }
                        case TypeContext.FLOAT: {
                            float[] a1 = (float[]) operands[pointer--];
                            float[] a2 = (float[]) operands[pointer--];
                            ++pointer;
                            operands[pointer] = new int[]{equals(a1, a2)};
                            break;
                        }
                        case TypeContext.BOOLEAN:
                        case TypeContext.CHAR:
                        case TypeContext.INT: {
                            int[] a1 = (int[]) operands[pointer--];
                            int[] a2 = (int[]) operands[pointer--];
                            ++pointer;
                            operands[pointer] = new int[]{equals(a1, a2)};
                            break;
                        }
                    }
                    types[pointer] = ((TypeContext.BOOLEAN << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_ACMPNE: {
                    int id = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    switch (id) {
                        case TypeContext.STRING: {
                            String[] a1 = (String[]) operands[pointer--];
                            String[] a2 = (String[]) operands[pointer--];
                            ++pointer;
                            operands[pointer] = new int[]{equals(a1, a2) == 1 ? 0 : 1};
                            break;
                        }
                        case TypeContext.FLOAT: {
                            float[] a1 = (float[]) operands[pointer--];
                            float[] a2 = (float[]) operands[pointer--];
                            ++pointer;
                            operands[pointer] = new int[]{equals(a1, a2) == 1 ? 0 : 1};
                            break;
                        }
                        case TypeContext.BOOLEAN:
                        case TypeContext.CHAR:
                        case TypeContext.INT: {
                            int[] a1 = (int[]) operands[pointer--];
                            int[] a2 = (int[]) operands[pointer--];
                            ++pointer;
                            operands[pointer] = new int[]{equals(a1, a2) == 1 ? 0 : 1};
                            break;
                        }
                    }
                    types[pointer] = ((TypeContext.BOOLEAN << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.ICONST: {
                    ++pointer;
                    operands[pointer] = new int[]{(instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++]};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.FCONST: {
                    ++pointer;
                    operands[pointer] = floatConstants[(instructions[codePosition++] << 8) + instructions[codePosition++]];
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.SCONST: {
                    ++pointer;
                    operands[pointer] = stringConstants[(instructions[codePosition++] << 8) + instructions[codePosition++]];
                    types[pointer] = ((TypeContext.STRING << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.BCONST: {
                    ++pointer;
                    operands[pointer] = new int[]{instructions[codePosition++]};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.ZCONST: {
                    ++pointer;
                    operands[pointer] = new int[]{instructions[codePosition++]};
                    types[pointer] = ((TypeContext.BOOLEAN << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.CCONST: {
                    ++pointer;
                    operands[pointer] = new int[]{(instructions[codePosition++] << 8) + instructions[codePosition++]};
                    types[pointer] = ((TypeContext.CHAR << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.SICONST: {
                    ++pointer;
                    operands[pointer] = new int[]{(instructions[codePosition++] << 8) + instructions[codePosition++]};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IADD: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand + rightOperand};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.FADD: {
                    float rightOperand = ((float[]) operands[pointer--])[0];
                    float leftOperand = ((float[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new float[]{leftOperand + rightOperand};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IDIV: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand / rightOperand};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.FDIV: {
                    float rightOperand = ((float[]) operands[pointer--])[0];
                    float leftOperand = ((float[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new float[]{leftOperand / rightOperand};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.ISUB: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand - rightOperand};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.FSUB: {
                    float rightOperand = ((float[]) operands[pointer--])[0];
                    float leftOperand = ((float[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new float[]{leftOperand - rightOperand};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IMUL: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand * rightOperand};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.FMUL: {
                    float rightOperand = ((float[]) operands[pointer--])[0];
                    float leftOperand = ((float[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new float[]{leftOperand * rightOperand};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IMOD: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand % rightOperand};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.FMOD: {
                    float rightOperand = ((float[]) operands[pointer--])[0];
                    float leftOperand = ((float[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new float[]{leftOperand % rightOperand};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.I2F: {
                    int x = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new float[]{x};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.F2S: {
                    float x = ((float[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new String[]{Float.toString(x)};
                    types[pointer] = ((TypeContext.STRING << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.S2F: {
                    String x = ((String[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new float[]{Float.parseFloat(x)};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.I2S: {
                    int x = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new String[]{Integer.toString(x, 10)};
                    types[pointer] = ((TypeContext.STRING << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.S2I: {
                    String x = ((String[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{Integer.parseInt(x, 10)};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.OR: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand == TRUE || rightOperand == TRUE ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.BOOLEAN << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.AND: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand * rightOperand};
                    types[pointer] = ((TypeContext.BOOLEAN << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.NOT: {
                    int operand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{operand == FALSE ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.BOOLEAN << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_SCMPEQ: {
                    String rightOperand = ((String[]) operands[pointer--])[0];
                    String leftOperand = ((String[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand.compareTo(rightOperand) == 0 ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.BOOLEAN << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_SCMPNE: {
                    String rightOperand = ((String[]) operands[pointer--])[0];
                    String leftOperand = ((String[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand.compareTo(rightOperand) == 0 ? FALSE : TRUE};
                    types[pointer] = ((TypeContext.BOOLEAN << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.SCONCAT: {
                    String rightOperand = ((String[]) operands[pointer--])[0];
                    String leftOperand = ((String[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new String[]{leftOperand + rightOperand};
                    types[pointer] = ((TypeContext.STRING << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.INEG: {
                    ((int[]) operands[pointer])[0] = -((int[]) operands[pointer])[0];
                    continue;
                }
                case Constants.FNEG: {
                    ((float[]) operands[pointer])[0] = -((float[]) operands[pointer])[0];
                    continue;
                }
                case Constants.IFEQ: {
                    if (((int[]) operands[pointer--])[0] == TRUE) {
                        codePosition = (instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++];
                    } else {
                        codePosition += OFFSET;
                    }
                    continue;
                }
                case Constants.IFNE: {
                    if (((int[]) operands[pointer--])[0] == FALSE) {
                        codePosition = (instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++];
                    } else {
                        codePosition += OFFSET;
                    }
                    continue;
                }
                case Constants.IF_ICMPEQ: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand == rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_ICMPNE: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand != rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_ICMPLT: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand < rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_ICMPLE: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand <= rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_ICMPGT: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand > rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_ICMPGE: {
                    int rightOperand = ((int[]) operands[pointer--])[0];
                    int leftOperand = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand >= rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.INT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_FCMPEQ: {
                    int rightOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    int leftOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand == rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_FCMPNE: {
                    int rightOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    int leftOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand != rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_FCMPLT: {
                    int rightOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    int leftOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand < rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_FCMPLE: {
                    int rightOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    int leftOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand <= rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_FCMPGT: {
                    int rightOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    int leftOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand > rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.IF_FCMPGE: {
                    int rightOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    int leftOperand = Float.floatToIntBits(((float[]) operands[pointer--])[0]);
                    ++pointer;
                    operands[pointer] = new int[]{leftOperand >= rightOperand ? TRUE : FALSE};
                    types[pointer] = ((TypeContext.FLOAT << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.DUP: {
                    Object operand = operands[pointer];
                    int type = types[pointer];
                    ++pointer;
                    operands[pointer] = operand;
                    types[pointer] = type;
                    continue;
                }
                case Constants.I2C: {
                    int value = ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new int[]{(char) value};
                    types[pointer] = ((TypeContext.CHAR << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.POP: {
                    operands[pointer--] = null;
                    continue;
                }
                case Constants.C2S: {
                    char c = (char) ((int[]) operands[pointer--])[0];
                    ++pointer;
                    operands[pointer] = new String[]{String.valueOf(c)};
                    types[pointer] = ((TypeContext.STRING << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.TRY: {
                    int offset = (instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++];
                    int operandsPointer = pointer;
                    int stackPointer = callBackPointer;
                    int debugStackPointer;
                    if (Debugger.DEVELOPMENT_MODE) {
                        debugStackPointer = debugStack.size();
                    }
                    try {
                        execute(codePosition++, local, false, instructions);
                    } catch (RuntimeException runtimeException) {
                        //printStackTrace(runtimeException);
                        lastErrorMessage = runtimeException.getMessage();
                        codePosition = offset;
                        pointer = operandsPointer;
                        callBackPointer = stackPointer;
                        if (Debugger.DEVELOPMENT_MODE) {
                            while (debugStack.size() != debugStackPointer) {
                                debugStack.pop();
                            }
                        }
                    }
                    continue;
                }
                case Constants.BREAK_TRY: {
                    codePosition = (instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++];
                    return;
                }
                case Constants.QUIT: {
                    return;
                }
                case Constants.THROW: {
                    lastErrorMessage = ((String[]) operands[pointer--])[0];
                    throw new RuntimeException(lastErrorMessage);
                }
                case Constants.IASTORE_0: {
                    int index = ((int[]) operands[pointer--])[0];
                    int[] array = (int[]) operands[pointer--];
                    int value = ((int[]) operands[pointer--])[0];
                    array[index] = value;
                    continue;
                }
                case Constants.FASTORE_0: {
                    int index = ((int[]) operands[pointer--])[0];
                    float[] array = (float[]) operands[pointer--];
                    float value = ((float[]) operands[pointer--])[0];
                    array[index] = value;
                    continue;
                }
                case Constants.SASTORE_0: {
                    int index = ((int[]) operands[pointer--])[0];
                    String[] array = (String[]) operands[pointer--];
                    String value = ((String[]) operands[pointer--])[0];
                    array[index] = value;
                    continue;
                }
                case Constants.RASTORE_0: {
                    int index = ((int[]) operands[pointer--])[0];
                    Variable[] array = (Variable[]) ((Object[]) operands[pointer--])[Record.OBJECT];
                    Variable value = ((Variable[]) ((Object[]) operands[pointer--])[Record.OBJECT])[0];
                    array[index] = value;
                    continue;
                }
                case Constants.OASTORE_0: {
                    int index = ((int[]) operands[pointer--])[0] * 2;
                    Object[] array = (Object[]) operands[pointer--];
                    Object value = ((Object[]) operands[pointer])[0];
                    int id = types[pointer];
                    pointer--;
                    array[index] = value;
                    array[index + 1] = new int[]{id};
                    continue;
                }
                case Constants.IASTORE_1: {
                    int index = ((int[]) operands[pointer--])[0];
                    int value = ((int[]) operands[pointer--])[0];
                    int[] array = (int[]) operands[pointer];
                    array[index] = value;
                    continue;
                }
                case Constants.FASTORE_1: {
                    int index = ((int[]) operands[pointer--])[0];
                    float value = ((float[]) operands[pointer--])[0];
                    float[] array = (float[]) operands[pointer];
                    array[index] = value;
                    continue;
                }
                case Constants.SASTORE_1: {
                    int index = ((int[]) operands[pointer--])[0];
                    String value = ((String[]) operands[pointer--])[0];
                    String[] array = (String[]) operands[pointer];
                    array[index] = value;
                    continue;
                }
                case Constants.RASTORE_1: {
                    int index = ((int[]) operands[pointer--])[0];
                    Variable value = ((Variable[]) ((Object[]) operands[pointer--])[Record.OBJECT])[0];
                    Object[] obj = (Object[]) operands[pointer];
                    Variable[] array = (Variable[]) obj[Record.OBJECT];
                    array[index] = value;
                    continue;
                }
                case Constants.OASTORE_1: {
                    int index = ((int[]) operands[pointer--])[0] * 2;
                    Object value = operands[pointer];
                    int id = types[pointer];
                    pointer--;
                    Object[] array = (Object[]) operands[pointer];
                    array[index] = value;
                    array[index + 1] = new int[]{id};
                    continue;
                }
                case Constants.NEW: {
                    int operationType = instructions[codePosition++];
                    int id = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    RecordContainer container = new RecordContainer(((Record) prototypes[id]).recordContext);
                    if (operationType == TypeContext.FULL) {
                        int fieldsAmount = container.recordContext.fieldsAmount;
                        Variable field;
                        for (int fieldIndex = 0; fieldIndex < fieldsAmount; fieldIndex++) {
                            field = container.fields[fieldIndex];
                            field.value = operands[pointer];
                            field.id = (types[pointer] >> 16) & 0xff;
                            field.dimension = types[pointer] & 0xff;
                            pointer--;
                        }
                    }
                    ++pointer;
                    operands[pointer] = new Object[]{new RecordContainer[]{container}, new int[]{id}};
                    types[pointer] = ((id << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.TYPEOF: {
                    int operandType = types[pointer--];
                    int id = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    int dimension = instructions[codePosition++];
                    int operandId = (operandType >>> 16) & 0xFF;
                    int operandDimension = operandType & 0xFF;
                    ++pointer;
                    if (id == operandId && dimension == operandDimension) {
                        operands[pointer] = new int[]{TRUE};
                    } else {
                        operands[pointer] = new int[]{FALSE};
                    }
                    types[pointer] = ((TypeContext.BOOLEAN << 16) + TypeContext.VALUE);
                    continue;
                }
                case Constants.CHECKCAST: {
                    int id = (instructions[codePosition++] << 8) + instructions[codePosition++];
                    int dimension = instructions[codePosition++];
                    int operandId = (types[pointer] >>> 16) & 0xFF;
                    int operandDimension = types[pointer] & 0xFF;
                    if (id == operandId && dimension == operandDimension) {
                        continue;
                    }
                    throw new RuntimeException("Нельзя преобразовать '" + getClassName(operandId, operandDimension) + "' в '" + getClassName(id, dimension) + "'");
                }
                default:
                    throw new RuntimeException("opcode: " + opcode + ", position: " + (codePosition - 1));
            }
        } while (work);
    }

    private static int equals(int[] a, int[] a2) {
        if (a == a2) {
            return 1;
        }
        int length = a.length;
        if (a2.length != length) {
            return 0;
        }
        for (int i = 0; i < length; i++) {
            if (a[i] != a2[i]) {
                return 0;
            }
        }
        return 1;
    }

    private static int equals(float[] a, float[] a2) {
        if (a == a2) {
            return 1;
        }
        int length = a.length;
        if (a2.length != length) {
            return 0;
        }
        for (int i = 0; i < length; i++) {
            if (Float.floatToIntBits(a[i]) != Float.floatToIntBits(a2[i])) {
                return 0;
            }
        }
        return 1;
    }

    private static int equals(String[] a, String[] a2) {
        if (a == a2) {
            return 1;
        }
        int length = a.length;
        if (a2.length != length) {
            return 0;
        }
        for (int i = 0; i < length; i++) {
            if (!a[i].equals(a2[i])) {
                return 0;
            }
        }
        return 1;
    }

//    private static int readShort() {
//        return ((instructions[codePosition++] << 8) + instructions[codePosition++]);
//    }
//    private static int readInt() {
//        return ((instructions[codePosition++] << 24) + (instructions[codePosition++] << 16) + (instructions[codePosition++] << 8) + instructions[codePosition++]);
//    }

    /*---------------------------THREAD MANAGEMENT----------------------------*/
    public void run() {
        AbstractConsole console = Console.getConsole();
        try {
            /*инициализация приложения(загрузка глобальных переменных,
            констант, определение точки входа в программу,
            адрес конструктора и деструктора, чтение инструкций)*/
            initialization();
        } catch (Exception ex) {
            console.showErrorMessage(ex.getMessage(), "Ошибка при инициализации");
            terminateRuntime();
            console.halt();
            printStackTrace(ex);
        }
        try {
            double startTime = System.currentTimeMillis();
            invokeConstructor();//если есть конструктор, вызвать перед началом выполнения программы
            execute(entryPoint, null, true, instructions);//выполнить программу
            showLog();
            invokeDestructor();//если есть деструктор, вызвать по завершению программы...
            double estimatedTime = System.currentTimeMillis();
            haltThread();//остановить поток выполнения программы
            if (Debugger.DEVELOPMENT_MODE) {
                if (successfulExecution) {
                    console.showMessage("Программа успешно выполнена");
                } else {
                    console.showErrorMessage("Выполнение прервано пользователем", "Информация");
                }
                Logger.println("Время работы: " + ((estimatedTime - startTime) / (double) 1000) + " сек.");
                if (!successfulExecution) {
                    return;
                }
            }
            console.halt();
        } catch (RuntimeException rex) {
            callDebug(rex);
            invokeDestructor();//...или если произошла ошибка при выполнении
            console.showErrorMessage(rex.getMessage(), "Ошибка при выполнении");
            console.halt();
            printStackTrace(rex);
        }
    }

    private static void callDebug(RuntimeException exception) {
        if (Debugger.DEVELOPMENT_MODE) {
            if (debugContext != null) {
                debugContext.printStackTrace(exception, debugStack);
            }
        }
    }

    private static void printStackTrace(Throwable throwable) {
        if (Debugger.DEVELOPMENT_MODE && Debugger.DEBUG) {
            throwable.printStackTrace();
        }
    }

    private static void showLog() {
        if (Debugger.DEVELOPMENT_MODE && Debugger.DEBUG) {
            System.out.println("operands[" + pointer + "], " + "callBack[" + callBackPointer + "]");
        }
    }

    private static void invokeConstructor() {
        if (constructorAddress != -1) {
            try {
                execute(constructorAddress, null, true, instructions);
                if (Debugger.DEVELOPMENT_MODE) {
                    debugStack.clear();
                }
            } catch (RuntimeException rex) {
                printStackTrace(rex);
                throw new RuntimeException("Ошибка при инициализации программы: " + rex.getMessage());
            }
        }
    }

    private static void invokeDestructor() {
        resetPointers();
        if (destructorAddress != -1) {
            try {
                execute(destructorAddress, null, true, instructions);
            } catch (RuntimeException rex) {
                callDebug(rex);
                printStackTrace(rex);
            }
        }
        clearMemory();
        terminateRuntime();
    }

    private static void resetPointers() {
        pointer = -1;
        callBackPointer = -1;
    }

    private static void clearMemory() {
        operands = null;
        instructions = null;
        global = null;
        callBack = null;
        types = null;
        if (Debugger.DEVELOPMENT_MODE) {
            debugStack = null;
        }
        deleteConstants();
        Runtime.getRuntime().gc();
    }

    private static void deleteConstants() {
        stringConstants = null;
        floatConstants = null;
    }

    private static void terminateRuntime() {
        Terminal display = Console.getTerminal();
        display.setMode(Terminal.WRITE);
    }

    public static String getClassName(int id, int dimension) {
        String result = names[id];
        for (int dimensionIndex = 0; dimensionIndex < dimension; dimensionIndex++) {
            result += "[]";
        }
        return result;
    }
}
