package dina.library.libs;

import java.io.*;
import dina.library.*;
import dina.runtime.*;
import dina.runtime.variables.*;

public class IOLibrary extends Library {

    public static final String fileVariableNotFound = "К файловой переменной не присоединен файл";

    private class FileStream {

        public static final int SIZE_OF_BYTE = 1;
        public static final int SIZE_OF_CHAR = 2;
        public static final int SIZE_OF_INTEGER = 4;
        public static final int SIZE_OF_FLOAT = 4;
        public static final int NO_OPEN = 0;
        public static final int READ_ONLY = 1;
        public static final int WRITE_ONLY = 2;
        public int mode = NO_OPEN;
        public DataInputStream dataInputStream;
        public DataOutputStream dataOutputStream;
        public String fileName;
        public int itIsRead;
        private int available;

        private String readOnlyException() {
            return "Файл '" + fileName + "' доступен только для чтения";
        }

        private String writeOnlyException() {
            return "Файл '" + fileName + "' доступен только для записи";
        }

        public void setMode(int mode) {
            if (this.mode == NO_OPEN || this.mode == mode) {
                this.mode = mode;
            } else {
                switch (this.mode) {
                    case READ_ONLY: {
                        throw new RuntimeException(readOnlyException());
                    }
                    case WRITE_ONLY: {
                        throw new RuntimeException(writeOnlyException());
                    }
                }
            }
            try {
                switch (mode) {
                    case READ_ONLY: {
                        dataInputStream = new DataInputStream(new FileInputStream(new File(fileName)));
                        available = dataInputStream.available();
                        break;
                    }
                    case WRITE_ONLY: {
                        dataOutputStream = new DataOutputStream(new FileOutputStream(new File(fileName)));
                        break;
                    }
                }
            } catch (Exception ex) {
                dataInputStream = null;
                dataOutputStream = null;
                throw new RuntimeException("Ошибка открытия файла '" + fileName + "'. Причина: " + ex.getMessage());
            }
        }

        private FileStream(String fileName) {
            this.fileName = fileName;
        }

        public int read() throws IOException {
            if (mode == READ_ONLY) {
                int value = dataInputStream.read();
                itIsRead += SIZE_OF_BYTE;
                return value;
            }
            throw new RuntimeException(readOnlyException());
        }

        public char readChar() throws IOException {
            if (mode == READ_ONLY) {
                char value = dataInputStream.readChar();
                itIsRead += SIZE_OF_CHAR;
                return value;
            }
            throw new RuntimeException(readOnlyException());
        }

        public int readInteger() throws IOException {
            if (mode == READ_ONLY) {
                int value = dataInputStream.readInt();
                itIsRead += SIZE_OF_INTEGER;
                return value;
            }
            throw new RuntimeException(readOnlyException());
        }

        public float readFloat() throws IOException {
            if (mode == READ_ONLY) {
                float value = dataInputStream.readFloat();
                itIsRead += SIZE_OF_FLOAT;
                return value;
            }
            throw new RuntimeException(readOnlyException());
        }

        public void write(int b) throws IOException {
            if (mode == WRITE_ONLY) {
                dataOutputStream.write(b);
            } else {
                throw new RuntimeException(writeOnlyException());
            }
        }

        public void writeChar(int v) throws IOException {
            if (mode == WRITE_ONLY) {
                dataOutputStream.writeChar(v);
            } else {
                throw new RuntimeException(writeOnlyException());
            }
        }

        public void writeInteger(int v) throws IOException {
            if (mode == WRITE_ONLY) {
                dataOutputStream.writeInt(v);
            } else {
                throw new RuntimeException(writeOnlyException());
            }
        }

        public void writeFloat(float v) throws IOException {
            if (mode == WRITE_ONLY) {
                dataOutputStream.writeFloat(v);
            } else {
                throw new RuntimeException(writeOnlyException());
            }
        }

        public void close() throws IOException {
            switch (mode) {
                case READ_ONLY: {
                    dataInputStream.close();
                    break;
                }
                case WRITE_ONLY: {
                    dataOutputStream.close();
                    break;
                }
            }
        }

        public int eof() throws Exception {
            if (mode == READ_ONLY) {
                dataInputStream.mark(mode);
                if (itIsRead == available) {
                    return TRUE;
                }
                return FALSE;
            }
            throw new RuntimeException(readOnlyException());
        }
    }

//    @Override
//    public void invoke(int functionID) {
//        switch (functionID) {
//            case FunctionsID.WRITE: {
//                int value = DinaRuntime.integerOperands[DinaRuntime.integerOperandsPointer--];
//                try {
//                    getStream().write(value);
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex.getMessage());
//                }
//                return;
//            }
//            case FunctionsID.READ: {
//                try {
//                    int value = getStream().read();
//                    DinaRuntime.integerOperands[++DinaRuntime.integerOperandsPointer] = value;
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex.getMessage());
//                }
//                return;
//            }
//            case FunctionsID.WRITE_CHAR_TO_FILE: {
//                int value = DinaRuntime.integerOperands[DinaRuntime.integerOperandsPointer--];
//                try {
//                    getStream().writeChar(value);
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex.getMessage());
//                }
//                return;
//            }
//            case FunctionsID.READ_CHAR_FROM_FILE: {
//                try {
//                    int value = getStream().readChar();
//                    DinaRuntime.integerOperands[++DinaRuntime.integerOperandsPointer] = value;
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex.getMessage());
//                }
//                return;
//            }
//            case FunctionsID.WRITE_INTEGER_TO_FILE: {
//                int value = DinaRuntime.integerOperands[DinaRuntime.integerOperandsPointer--];
//                try {
//                    getStream().writeInteger(value);
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex.getMessage());
//                }
//                return;
//            }
//            case FunctionsID.READ_INTEGER_FROM_FILE: {
//                try {
//                    int value = getStream().readInteger();
//                    DinaRuntime.integerOperands[++DinaRuntime.integerOperandsPointer] = value;
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex.getMessage());
//                }
//                return;
//            }
//            case FunctionsID.WRITE_FLOAT_TO_FILE: {
//                float value = DinaRuntime.floatOperands[DinaRuntime.floatOperandsPointer--];
//                try {
//                    getStream().writeFloat(value);
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex.getMessage());
//                }
//                return;
//            }
//            case FunctionsID.READ_FLOAT_FROM_FILE: {
//                try {
//                    float value = getStream().readFloat();
//                    DinaRuntime.floatOperands[++DinaRuntime.floatOperandsPointer] = value;
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex.getMessage());
//                }
//                return;
//            }
//            case FunctionsID.EOF: {
//                RecordContainer fileContainer = (RecordContainer) DinaRuntime.objectOperands[DinaRuntime.objectOperandsPointer--];
//                FileStream stream = (FileStream) fileContainer.getObject();
//                if (stream != null) {
//                    try {
//                        int eof = stream.eof();
//                        DinaRuntime.integerOperands[++DinaRuntime.integerOperandsPointer] = eof;
//                    } catch (Exception ex) {
//                        throw new RuntimeException(ex.getMessage());
//                    }
//                } else {
//                    throw new RuntimeException(fileVariableNotFound);
//                }
//                return;
//            }
//            case FunctionsID.OPEN_FILE: {
//                RecordContainer fileContainer = (RecordContainer) DinaRuntime.objectOperands[DinaRuntime.objectOperandsPointer--];
//                String fileName = DinaRuntime.stringOperands[DinaRuntime.stringOperandsPointer--];
//                FileStream stream = (FileStream) fileContainer.getObject();
//                if (stream != null) {
//                    if (stream.mode != FileStream.NO_OPEN) {
//                        throw new RuntimeException("Файл '" + fileName + "' не закрыт");
//                    }
//                }
//                fileContainer.setObject(new FileStream(fileName));
//                return;
//            }
//            case FunctionsID.RESET_FILE: {
//                RecordContainer fileContainer = (RecordContainer) DinaRuntime.objectOperands[DinaRuntime.objectOperandsPointer--];
//                FileStream stream = (FileStream) fileContainer.getObject();
//                if (stream != null) {
//                    stream.setMode(FileStream.READ_ONLY);
//                } else {
//                    throw new RuntimeException(fileVariableNotFound);
//                }
//                return;
//            }
//            case FunctionsID.REWRITE_FILE: {
//                RecordContainer fileContainer = (RecordContainer) DinaRuntime.objectOperands[DinaRuntime.objectOperandsPointer--];
//                FileStream stream = (FileStream) fileContainer.getObject();
//                if (stream != null) {
//                    stream.setMode(FileStream.WRITE_ONLY);
//                } else {
//                    throw new RuntimeException(fileVariableNotFound);
//                }
//                return;
//            }
//            case FunctionsID.CLOSE_FILE: {
//                RecordContainer fileContainer = (RecordContainer) DinaRuntime.objectOperands[DinaRuntime.objectOperandsPointer--];
//                FileStream stream = (FileStream) fileContainer.getObject();
//                if (stream != null) {
//                    try {
//                        stream.close();
//                        fileContainer.setObject(null);
//                    } catch (Exception ex) {
//                        throw new RuntimeException("Невозможно закрыть файл '" + stream.fileName + "'. Причина: " + ex.getMessage());
//                    }
//                } else {
//                    throw new RuntimeException(fileVariableNotFound);
//                }
//                return;
//            }
//            case FunctionsID.GET_ROOTS: {
//                File[] listRoots = File.listRoots();
//                String[] roots = new String[listRoots.length];
//                for (int rootIndex = 0; rootIndex < roots.length; rootIndex++) {
//                    roots[rootIndex] = listRoots[rootIndex].getPath();
//                }
//                DinaRuntime.operands[++DinaRuntime.pointer] = roots;
//                return;
//            }
//        }
//    }

//    private static FileStream getStream() {
//        RecordContainer fileContainer = (RecordContainer) DinaRuntime.objectOperands[DinaRuntime.objectOperandsPointer--];
//        FileStream stream = (FileStream) fileContainer.getObject();
//        if (stream != null) {
//            return stream;
//        }
//        throw new RuntimeException(fileVariableNotFound);
//    }

    public String getLibratyName() {
        return "io";
    }
}
