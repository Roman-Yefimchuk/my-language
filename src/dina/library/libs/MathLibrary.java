package dina.library.libs;

import java.util.*;
import dina.library.*;
import dina.runtime.*;

public class MathLibrary extends Library {

    private static Random random;

    @Override
    public void initLibrary() {
        random = new Random();
    }

    @Override
    public void invoke(int functionID) {
        switch (functionID) {
            case FunctionsID.SQRT: {
                float x = ((float[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new float[]{(float) Math.sqrt(x)};
                return;
            }
            case FunctionsID.SQR_FLOAT: {
                float x = ((float[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new float[]{x * x};
                return;
            }
            case FunctionsID.SQR_INTEGER: {
                int x = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new int[]{x * x};
                return;
            }
            case FunctionsID.COS: {
                float x = ((float[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new float[]{(float) Math.cos(x)};
                return;
            }
            case FunctionsID.SIN: {
                float x = ((float[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new float[]{(float) Math.sin(x)};
                return;
            }
            case FunctionsID.ABS_INTEGER: {
                int x = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new int[]{(x < 0) ? -x : x};
                return;
            }
            case FunctionsID.ABS_FLOAT: {
                float x = ((float[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new float[]{(x <= 0.0F) ? 0.0F - x : x};
                return;
            }
            case FunctionsID.RANDOM: {
                int x = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new int[]{(random.nextInt() >>> 1) % x};
                return;
            }
            case FunctionsID.ROUND: {
                float x = ((float[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new int[]{(int) Math.floor(x + 0.5f)};
                return;
            }
            case FunctionsID.RANDOMIZE: {
                random = new Random();
                return;
            }
        }
    }

    public String getLibratyName() {
        return "math";
    }
}
