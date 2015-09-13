package dina.library.libs;

import dina.library.*;
import dina.runtime.*;

public class StringLibrary extends Library {

    @Override
    public void invoke(int functionID) {
        switch (functionID) {
            case FunctionsID.CHARAT: {
                String string = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                int index = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new int[]{string.charAt(index)};
                return;
            }
            case FunctionsID.LENGTH: {
                int length = ((String[]) DinaVM.operands[DinaVM.pointer--])[0].length();
                DinaVM.operands[++DinaVM.pointer] = new int[]{length};
                return;
            }
            case FunctionsID.TO_CHAR_ARRAY: {
                String string = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                char[] c = string.toCharArray();
                int[] i = new int[c.length];
                for (int index = 0; index < i.length; index++) {
                    i[index] = c[index];
                }
                DinaVM.operands[++DinaVM.pointer] = i;
                return;
            }
            case FunctionsID.TRIM: {
                DinaVM.operands[DinaVM.pointer] = new String[]{(((String[]) DinaVM.operands[DinaVM.pointer])[0]).trim()};
                return;
            }
            case FunctionsID.TO_LOWER_CASE: {
                DinaVM.operands[DinaVM.pointer] = new String[]{(((String[]) DinaVM.operands[DinaVM.pointer])[0]).toLowerCase()};
                return;
            }
            case FunctionsID.TO_UPPER_CASE: {
                DinaVM.operands[DinaVM.pointer] = new String[]{(((String[]) DinaVM.operands[DinaVM.pointer])[0]).toUpperCase()};
                return;
            }
            case FunctionsID.SUBSTRING: {
                String string = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                int beginIndex = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int endIndex = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new String[]{string.substring(beginIndex, endIndex)};
                return;
            }
            case FunctionsID.INDEX_OF: {
                String string = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                String str = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                int fromIndex = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new int[]{string.indexOf(str, fromIndex)};
                return;
            }
            case FunctionsID.TO_STRING: {
                int[] charArray = (int[]) DinaVM.operands[DinaVM.pointer--];
                StringBuffer sBuf = new StringBuffer();
                for (int charIndex = 0; charIndex < charArray.length; charIndex++) {
                    sBuf.append((char) charArray[charIndex]);
                }
                DinaVM.operands[++DinaVM.pointer] = new String[]{sBuf.toString()};
                return;
            }
        }
    }

    public String getLibratyName() {
        return "string";
    }
}
