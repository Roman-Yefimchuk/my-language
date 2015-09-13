package dina.library.libs;

import dina.library.*;
import dina.runtime.*;
import ide.console.*;
import ide.console.terminal.*;

public class ConsoleLibrary extends Library {

    private AbstractConsole console;

    @Override
    public void initLibrary() {
        console = Console.getConsole();
    }

    @Override
    public void invoke(int functionID) {
        switch (functionID) {
            case FunctionsID.GET_STATE: {
                int x = console.getConsoleState();
                DinaVM.operands[++DinaVM.pointer] = new int[]{x};
                return;
            }
            case FunctionsID.SET_STATE: {
                int state = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.setConsoleState(state);
                return;
            }
            case FunctionsID.SET_TITLE: {
                String title = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.setConsoleTitle(title);
                return;
            }
            case FunctionsID.SET_FULL_SCREEN_MODE: {
                int mode = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.setFullScreenMode(mode == TRUE);
                return;
            }
        }
        if (Terminal.state != Terminal.STATE_TEXT) {
            invalidConsoleState();
        }
        switch (functionID) {
            case FunctionsID.WRITELN_BOOLEAN: {
                int x = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.writeLn(x == TRUE);
                return;
            }
            case FunctionsID.WRITELN_CHAR: {
                char x = (char) ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.writeLn(x);
                return;
            }
            case FunctionsID.WRITELN_INTEGER: {
                int x = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.writeLn(x);
                return;
            }
            case FunctionsID.WRITELN_FLOAT: {
                float x = ((float[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.writeLn(x);
                return;
            }
            case FunctionsID.WRITELN_STRING: {
                String x = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.writeLn(x);
                return;
            }
            case FunctionsID.WRITELN: {
                console.writeLn();
                return;
            }
            case FunctionsID.WRITE_BOOLEAN: {
                int x = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.write(x == TRUE);
                return;
            }
            case FunctionsID.WRITE_CHAR: {
                char x = (char) ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.write(x);
                return;
            }
            case FunctionsID.WRITE_INTEGER: {
                int x = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.write(x);
                return;
            }
            case FunctionsID.WRITE_FLOAT: {
                float x = ((float[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.write(x);
                return;
            }
            case FunctionsID.WRITE_STRING: {
                String x = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                console.write(x);
                return;
            }
            case FunctionsID.NEXT_INT: {
                int x = console.nextInteger();
                DinaVM.operands[++DinaVM.pointer] = new int[]{x};
                return;
            }
            case FunctionsID.NEXT_FLOAT: {
                float x = console.nextFloat();
                DinaVM.operands[++DinaVM.pointer] = new float[]{x};
                return;
            }
            case FunctionsID.NEXT_STRING: {
                String x = console.nextString();
                DinaVM.operands[++DinaVM.pointer] = new String[]{x};
                return;
            }
            case FunctionsID.CLS: {
                console.cls();
                return;
            }
            case FunctionsID.GET_KEY: {
                int x = console.getKey();
                DinaVM.operands[++DinaVM.pointer] = new int[]{x};
                return;
            }
        }
    }

    public String getLibratyName() {
        return "bios";
    }
}
