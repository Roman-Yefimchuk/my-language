package dina.library.libs;

import ide.*;
import ide.console.*;
import dina.*;
import dina.library.*;
import dina.runtime.*;

public class SystemLibrary extends Library {

    public static Runtime runtime = Runtime.getRuntime();

    @Override
    public void invoke(int functionID) {
        switch (functionID) {
            case FunctionsID.SLEEP: {
                try {
                    Thread.sleep(((int[]) DinaVM.operands[DinaVM.pointer--])[0]);
                } catch (Exception ex) {
                }
                return;
            }
            case FunctionsID.YIELD: {
                Thread.yield();
                return;
            }
            case FunctionsID.FREE: {
                runtime.gc();
                return;
            }
            case FunctionsID.SYSTEM: {
                switch (((int[]) DinaVM.operands[DinaVM.pointer--])[0]) {
                    case GlobalConstantsValue.FREE_MEMORY: {
                        DinaVM.operands[++DinaVM.pointer] = new int[]{(int) runtime.freeMemory()};
                        return;
                    }
                    case GlobalConstantsValue.TOTAL_MEMORY: {
                        DinaVM.operands[++DinaVM.pointer] = new int[]{(int) runtime.totalMemory()};
                        return;
                    }
                    case GlobalConstantsValue.CURRENT_TIME_MILLIS: {
                        DinaVM.operands[++DinaVM.pointer] = new int[]{(int) System.currentTimeMillis()};
                        return;
                    }
                }
                throw new IllegalArgumentException("Argument to functions 'System' must be one of: FREE_MEMORY, TOTAL_MEMORY or CURRENT_TIME_MILLIS");
            }
            case FunctionsID.HALT: {
                AbstractConsole console = Console.getConsole();
                console.halt();
                return;
            }
            case FunctionsID.GET_ERROR_MESSAGE: {
                String lastError = DinaVM.lastErrorMessage;
                DinaVM.operands[++DinaVM.pointer] = new String[]{lastError == null ? "" : lastError};
                return;
            }
            case FunctionsID.TRACE: {
                String string = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                System.out.println(string);
                if (Debugger.DEVELOPMENT_MODE) {
                    Logger.println(string);
                }
                return;
            }
        }
    }

    public String getLibratyName() {
        return "system";
    }
}
