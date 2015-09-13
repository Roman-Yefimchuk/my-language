package core.libs;

public class StringLibrary implements AbstractLibrary {

    public static final int CHAR_AT = 1969081730;//"String.charAt(CI)C"
    public static final int TO_CHAR_ARRAY = 649637083;//"String.toCharArray(S)[C"
    public static final int TO_LOWER_CASE = -1255393650;//"String.toLowerCase(S)S"
    public static final int TO_UPPER_CASE = 221786927;//"String.toUpperCase(S)S"
    public static final int TRIM = 449104884;//"String.trim(S)S"
    public static final int SUB_STRING = -263559159;//"String.subString(SII)S"
    public static final int INDEX_OF = 717738573;//"String.indexOf(SSI)I"
    public static final int TO_STRING = 47528145;//"String.toString([C)S"

    public void constructor() {
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case CHAR_AT: {
                String s = ((String[]) args[0])[0];
                int index = ((int[]) args[1])[0];
                return new int[]{s.charAt(index)};
            }
            case TO_CHAR_ARRAY: {
                String s = ((String[]) args[0])[0];
                char[] c = s.toCharArray();
                int[] charArray = new int[c.length];
                int length = charArray.length;
                for (int index = 0; index < length; index++) {
                    charArray[index] = c[index];
                }
                return charArray;
            }
            case TO_LOWER_CASE: {
                String s = ((String[]) args[0])[0];
                return new String[]{s.toLowerCase()};
            }
            case TO_UPPER_CASE: {
                String s = ((String[]) args[0])[0];
                return new String[]{s.toUpperCase()};
            }
            case TRIM: {
                String s = ((String[]) args[0])[0];
                return new String[]{s.trim()};
            }
            case SUB_STRING: {
                String s = ((String[]) args[0])[0];
                int beginIndex = ((int[]) args[1])[0];
                int endIndex = ((int[]) args[2])[0];
                return new String[]{s.substring(beginIndex, endIndex)};
            }
            case INDEX_OF: {
                String s1 = ((String[]) args[0])[0];
                String s2 = ((String[]) args[1])[0];
                int fromIndex = ((int[]) args[2])[0];
                return new int[]{s1.indexOf(s2, fromIndex)};
            }
            case TO_STRING: {
                int[] charArray = (int[]) args[0];
                StringBuilder sBuf = new StringBuilder();
                int length = charArray.length;
                for (int charIndex = 0; charIndex < length; charIndex++) {
                    sBuf.append((char) charArray[charIndex]);
                }
                return new String[]{sBuf.toString()};
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "String";
    }

    public void destructor() {
    }
}
