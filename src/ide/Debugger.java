package ide;

import java.util.*;
import dina.disassembler.*;

public class Debugger {

    public static final boolean DEBUG = true;
    public static final boolean DEVELOPMENT_MODE = true;
    private Hashtable<Integer, String> functions = new Hashtable<Integer, String>();

    public void putFunction(String signature, int address) {
        functions.put(address, signature);
    }

    public void printStackTrace(RuntimeException exception, Stack<Integer> stack) {
        Logger.println("Ошибка при выполнении: \"" + exception.getMessage() + "\"");
        String space = "     ";
        while (!stack.empty()) {
            int i = stack.pop();
            if (i < 0) {
                i = -i;
                int libraryId = ((i >> 8) & 0xff) - 1;
                int functionId = (i & 0xff) - 1;
                Logger.println(space + "в " + Disassembler.getFullFunctionName(libraryId, functionId));
            } else {
                Logger.println(space + "в " + functions.get(i));
            }
        }
    }
}
