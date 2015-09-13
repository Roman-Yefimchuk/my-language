package dina.compiler;

import java.io.*;

public class Output {

    public static DataOutputStream outputStream;
    public static ByteArrayOutputStream executeFile;
    //
    public static final int BUFFER = 2048;
    private static byte[] out = new byte[0];
    private static int size;
    private static int currentAddress = -1;

    public static int getCurrentAddress() {
        return currentAddress;
    }

    public static int getSize() {
        return size;
    }

    public static void writeInteger(int value) {
        writeByte((value >>> 24) & 0xFF);
        writeByte((value >>> 16) & 0xFF);
        writeByte((value >>> 8) & 0xFF);
        writeByte(value & 0xFF);
    }

    public static void writeShort(int value) {
        writeByte((value >>> 8) & 0xFF);
        writeByte(value & 0xFF);
    }

    private static void write(int value, int buffer) {
        if (out.length == size) {
            byte[] tempOut = new byte[out.length + buffer];
            System.arraycopy(out, 0, tempOut, 0, out.length);
            tempOut[out.length] = (byte) value;
            out = tempOut;
            tempOut = null;
            System.gc();
            size++;
        } else {
            out[size++] = (byte) value;
        }
    }

    public static void writeByte(int value) {
        write(value, BUFFER);
        ++currentAddress;
    }

    public static void writeSystemInformation(int value) {
        write(value, 1);
    }

    public static void setAddress(int positionInOutput, int value) {
        out[++positionInOutput] = (byte) ((value >>> 24) & 0xFF);
        out[++positionInOutput] = (byte) ((value >>> 16) & 0xFF);
        out[++positionInOutput] = (byte) ((value >>> 8) & 0xFF);
        out[++positionInOutput] = (byte) (value & 0xFF);
    }

    public static void flush() {
        out = new byte[0];
        size = 0;
        currentAddress = -1;
        executeFile = new ByteArrayOutputStream();
        outputStream = new DataOutputStream(executeFile);
        Runtime.getRuntime().gc();
    }

    public static byte[] toByteArray() {
        byte[] result = new byte[size];
        System.arraycopy(out, 0, result, 0, size);
        return result;
    }
}
