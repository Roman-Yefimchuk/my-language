package dina.library.libs;

import dina.library.*;
import dina.runtime.*;
import ide.console.terminal.*;

public class EventsLibrary extends Library {

    @Override
    public void invoke(int functionID) {
        if (Terminal.state != Terminal.STATE_VGA) {
            invalidConsoleState();
        }
        switch (functionID) {
            case FunctionsID.GET_X: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{Terminal.CURRENT_X};
                return;
            }
            case FunctionsID.GET_Y: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{Terminal.CURRENT_Y};
                return;
            }
            case FunctionsID.GET_MOUSE_ACTION: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{Terminal.CURRENT_MOUSE_ACTION};
                return;
            }
            case FunctionsID.KEY_TO_ACTION: {
                int keyCode = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int result = Terminal.ACTION_KEY == keyCode ? TRUE : FALSE;
                DinaVM.operands[++DinaVM.pointer] = new int[]{result};
                if (result == TRUE) {
                    Terminal.ACTION_KEY = 0;
                }
                return;
            }
        }
    }

    public String getLibratyName() {
        return "events";
    }
}
