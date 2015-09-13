package dina.disassembler;

import ide.*;
import java.io.*;
import java.util.*;
import dina.*;
import java.util.ArrayList;

public class Disassembler implements Constants, SystemConstants, DasmConstants {

    private int entryPoint = -1;
    private int constructorAddress = -1;
    private int destructorAddress = -1;
    private DataInputStream input;
    private int functionCount;
    private float[] floatConstants;
    private String[] stringConstants;
    private ArrayList<OpcodeLabel> labels = new ArrayList<OpcodeLabel>();
    private ArrayList<String> names;

    private void readRealConstants() throws Exception {
        int count = input.readShort();
        if (count != 0) {
            floatConstants = new float[count];
            for (int index = 0; index < count; index++) {
                floatConstants[index] = input.readFloat();
            }
        }
    }

    private void readStringConstants() throws Exception {
        int count = input.readShort();
        if (count != 0) {
            stringConstants = new String[count];
            for (int index = 0; index < count; index++) {
                stringConstants[index] = input.readUTF();
            }
        }
    }

    private void readBootInfo() throws Exception {
        functionCount = input.readShort();
        entryPoint = input.readInt();
        constructorAddress = input.readInt();
        destructorAddress = input.readInt();
    }

    private void loadConstants() throws Exception {
        readRealConstants();
        readStringConstants();
    }

    public static void decompile(String unitName) throws Exception {
        Disassembler decompiler = new Disassembler();
        decompiler.input = new DataInputStream(unitName.getClass().getResourceAsStream(unitName));
        decompiler.start();
    }

    public static void decompile(ByteArrayOutputStream byteArray) throws Exception {
        Disassembler decompiler = new Disassembler();
        byte[] inputBytes = byteArray.toByteArray();
        decompiler.input = new DataInputStream(new ByteArrayInputStream(inputBytes));
        decompiler.start();
    }

    private void crealeGlobalVariables() throws Exception {
        int varCount = input.readShort();
        for (int varIndex = 0; varIndex < varCount; varIndex++) {
            int variableType = input.readShort();
            int variableEntity = input.readByte();
        }
    }

    private void readRecords() throws Exception {
        int recordsAmount = input.readShort();
        for (int recordIndex = 0; recordIndex < recordsAmount; recordIndex++) {
            int recordId = input.readShort();
            String name = input.readUTF();
            names.add(name);
            int fieldsAmount = input.readShort();
            if (fieldsAmount != 0) {
                for (int fieldIndex = 0; fieldIndex < fieldsAmount; fieldIndex++) {
                    int fieldTypeId = input.readShort();
                    int fieldEntity = input.readByte();
                }
            }
        }
    }

    public static String getFullFunctionName(int libraryId, int functionId) {
        final int LIBRARY_NAME = 0;
        final int SIGNATURES = 1;
        Object[] library = (Object[]) librarys[libraryId];
        String libraryName = (String) library[LIBRARY_NAME];
        String[] signatures = (String[]) library[SIGNATURES];
        return libraryName + "." + signatures[functionId];
    }

    private void decode() throws Exception {
        try {
            int length = input.readInt();
            if (length != 0) {
                int position = 0;
                int opcode = -1;
                int functionIndex = 0;
                while (functionIndex++ != functionCount) {
                    opcode = input.readByte();
                    switch (opcode) {
                        case BEGIN_FUNCTION: {
                            StringBuilder source = new StringBuilder();
                            int begin = position;
                            int varCount = input.readShort();
                            int argumentsCount = input.read();
                            int pos = position;
                            source.append(varCount).append(" ").append(argumentsCount);
                            position += 3;
                            if (varCount != 0) {
                                for (int varIndex = 0; varIndex < varCount; varIndex++) {
                                    source.append(" ").append(input.readShort());
                                    source.append(" ").append(input.readByte());
                                    position += 3;
                                }
                            }
                            if (begin == entryPoint) {
                                source.append(" //ENTRY POINT");
                            } else if (begin == constructorAddress) {
                                source.append(" //CONSTRUCTOR");
                            } else if (begin == destructorAddress) {
                                source.append(" //DESTRUCTOR");
                            }
                            addLabel(pos, source.toString());
                            boolean breakLoop = false;
                            while (!breakLoop) {
                                opcode = input.readByte();
                                switch (opcode) {
                                    case NEW: {
                                        StringBuilder builder = new StringBuilder(opcodes[opcode - 1]);
                                        builder.append(" <");
                                        input.readByte();
                                        builder.append(names.get(input.readShort()));
                                        builder.append(">");
                                        addLabel(position, builder.toString());
                                        position += 4;
                                        break;
                                    }
                                    case OALOAD:
                                    case OASTORE_0:
                                    case OASTORE_1:
                                    case STRINGLENGTH:
                                    case DUP:
                                    case THROW:
                                    case S2F:
                                    case C2S:
                                    case F2S:
                                    case SASTORE_1:
                                    case IASTORE_1:
                                    case FASTORE_1:
                                    case RASTORE_1:
                                    case IADD:
                                    case FADD:
                                    case ISUB:
                                    case FSUB:
                                    case IDIV:
                                    case FDIV:
                                    case IMUL:
                                    case FMUL:
                                    case IMOD:
                                    case FMOD:
                                    case RETURN:
                                    case I2F:
                                    case I2S:
                                    case OR:
                                    case AND:
                                    case NOT:
                                    case IF_SCMPEQ:
                                    case IF_SCMPNE:
                                    case SCONCAT:
                                    case INEG:
                                    case FNEG:
                                    case IF_ICMPEQ:
                                    case IF_ICMPNE:
                                    case IF_ICMPLT:
                                    case IF_ICMPLE:
                                    case IF_ICMPGT:
                                    case IF_ICMPGE:
                                    case IF_FCMPEQ:
                                    case IF_FCMPNE:
                                    case IF_FCMPLT:
                                    case IF_FCMPLE:
                                    case IF_FCMPGT:
                                    case IF_FCMPGE:
                                    case POP:
                                    case IALOAD:
                                    case IASTORE_0:
                                    case FALOAD:
                                    case FASTORE_0:
                                    case SALOAD:
                                    case SASTORE_0:
                                    case RALOAD:
                                    case RASTORE_0:
                                    case I2C:
                                    case QUIT:
                                    case S2I: {
                                        //source.append(position).append(": ").append(opcodes[opcode - 1]).append('\n');
                                        addLabel(position, opcodes[opcode - 1]);
                                        position++;
                                        break;
                                    }
                                    case ZCONST: {
                                        addLabel(position, opcodes[opcode - 1] + " <" + (input.readByte() == 1) + ">");
                                        position += 2;
                                        break;
                                    }
                                    case BCONST: {
                                        //source.append(position).append(": ").append(opcodes[opcode - 1]).append(" ").append(input.readByte()).append('\n');
                                        addLabel(position, opcodes[opcode - 1] + " " + input.readByte());
                                        position += 2;
                                        break;
                                    }
                                    case ARRAYLENGTH: {
                                        addLabel(position, opcodes[opcode - 1] + " <" + names.get(input.readShort()) + ">");
                                        position += 3;
                                        break;
                                    }
                                    case SICONST:
                                    case ACONCAT:
                                    case IF_ACMPEQ:
                                    case IF_ACMPNE:
                                    case PUTFIELD:
                                    case GETFIELD:
                                    case LLOAD:
                                    case LSTORE:
                                    case GLOAD:
                                    case GSTORE: {
                                        //source.append(position).append(": ").append(opcodes[opcode - 1]).append(" ").append(input.readShort()).append('\n');
                                        addLabel(position, opcodes[opcode - 1] + " " + input.readShort());
                                        position += 3;
                                        break;
                                    }
                                    case NEWARRAY: {
                                        addLabel(position, opcodes[opcode - 1] + " <" + names.get(input.readShort()) + ">");
                                        position += 3;
                                        break;
                                    }
                                    case SCONST: {
                                        int index = input.readShort();
                                        String stringConstant = stringConstants[index];
                                        //source.append(position).append(": ").append(opcodes[opcode - 1]).append(" \"").append(stringConstant).append("\"\n");
                                        addLabel(position, opcodes[opcode - 1] + " \"" + stringConstant + "\"");
                                        position += 3;
                                        break;
                                    }
                                    case FCONST: {
                                        int index = input.readShort();
                                        float floatConstant = floatConstants[index];
                                        //source.append(position).append(": ").append(opcodes[opcode - 1]).append(" ").append(floatConstant).append('\n');
                                        addLabel(position, opcodes[opcode - 1] + " " + String.valueOf(floatConstant));
                                        position += 3;
                                        break;
                                    }
                                    case INVOKENATIVE: {
                                        //source.append(position).append(": ").append(opcodes[opcode - 1]).append(" \"").append(getFullFunctionName(input.readByte(), input.readByte())).append("\"\n");
                                        addLabel(position, opcodes[opcode - 1] + " \"" + getFullFunctionName(input.readByte(), input.readByte()) + "\"");
                                        position += 3;
                                        break;
                                    }
                                    case INVOKE:
                                    case ICONST:
                                    case IFEQ:
                                    case IFNE:
                                    case GOTO:
                                    case BREAK_TRY:
                                    case TRY: {
                                        //source.append(position).append(": ").append(opcodes[opcode - 1]).append(" ").append(input.readInt()).append('\n');
                                        addLabel(position, opcodes[opcode - 1] + " " + input.readInt());
                                        position += 5;
                                        break;
                                    }
                                    case TABLESWITCH: {
                                        int casesAmount = input.readShort();
                                        int defaultAddress = input.readInt();
                                        addLabel(position, opcodes[opcode - 1] + " cases: " + casesAmount + ", default: " + defaultAddress);
                                        position += 7;
                                        for (int caseIndex = 0; caseIndex < casesAmount; caseIndex++) {
                                            addLabel(position, input.readInt() + ": " + input.readInt());
                                            position += 8;
                                        }
                                        break;
                                    }
                                    case CCONST: {
                                        addLabel(position, opcodes[opcode - 1] + " \'" + toChar(input.readChar()) + "\'");
                                        position += 3;
                                        break;
                                    }
                                    case CHECKCAST:
                                    case TYPEOF: {
                                        addLabel(position, opcodes[opcode - 1] + " <" + getClassName(input.readShort(), input.readByte()) + ">");
                                        position += 4;
                                        break;
                                    }
                                    case END_FUNCTION: {
                                        breakLoop = true;
                                        break;
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
                throw new RuntimeException("Ошибка при декомпиляции");
            }
        } catch (Exception ex) {
            if (Debugger.DEVELOPMENT_MODE) {
                IDE.getByteCode().setText(toString());
            }
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static String toChar(char charValue) {
        switch (charValue) {
            case '"': {
                return "\\";
            }
            case '\'': {
                return "\\'";
            }
            case '\\': {
                return "\\\\";
            }
            case '\b': {
                return "\\b";
            }
            case '\f': {
                return "\\f";
            }
            case '\n': {
                return "\\n";
            }
            case '\r': {
                return "\\r";
            }
            case '\t': {
                return "\\t";
            }
            default: {
                return String.valueOf(charValue);
            }
        }
    }

    private void readHeader() throws Exception {
        int header = input.readInt();
        if (header != HEADER) {
            throw new RuntimeException("Не коректный заголовок файла");
        }
        float version = input.readFloat();
        if (version != VERSION) {
            throw new RuntimeException("Не совместимые версии: '" + version + "' и '" + VERSION + "'");
        }
    }

    private void start() throws Exception {
        names = new ArrayList<String>();
        names.add("boolean");
        names.add("char");
        names.add("int");
        names.add("float");
        names.add("string");
        names.add("object");
        readHeader();
        loadConstants();
        readRecords();
        crealeGlobalVariables();
        readBootInfo();
        try {
            decode();
            IDE.getByteCode().setText(toString());
        } catch (Exception ex) {
        }
    }

    private void addLabel(int address, String label) {
        labels.add(new OpcodeLabel(address, label));
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        int size = labels.size();
        int maxAddressLength = String.valueOf(labels.get(size - 1).getAddress()).length();
        for (int labelIndex = 0; labelIndex < size; labelIndex++) {
            OpcodeLabel label = labels.get(labelIndex);
            sb.append(label.getAddress());
            sb.append(": ");
            sb.append(addSpace(maxAddressLength - String.valueOf(label.getAddress()).length()));
            sb.append(label.toString());
            sb.append('\n');
        }
        return sb.toString();
    }

    private String addSpace(int value) {
        String result = "";
        for (int index = 0; index < value; index++) {
            result += " ";
        }
        return result;
    }

    private String getClassName(int classId, int dimension) {
        String result = names.get(classId);
        for (int dimensionIndex = 0; dimensionIndex < dimension; dimensionIndex++) {
            result += "[]";
        }
        return result;
    }
}
