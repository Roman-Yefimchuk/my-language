package core.libs;

public class FloatLibrary implements AbstractLibrary {

    public static final int TO_STRING = -90377786;//"Float.toString(F)S"
    public static final int TO_HEX_STRING = -1876718201;//"Float.toHexString(F)S"
    public static final int PARSE_FLOAT = 474612867;//"Float.parseFloat(S)F"
    public static final int FLOAT_TO_INT_BITS = 1099659374;//"Float.floatToIntBits(F)I"
    public static final int INT_BITS_TO_FLOAT = -817493412;//"Float.intBitsToFloat(I)F"

    public void constructor() {
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case TO_STRING: {
                float f = ((float[]) args[0])[0];
                return new String[]{Float.toString(f)};
            }
            case TO_HEX_STRING: {
                float f = ((float[]) args[0])[0];
                return new String[]{Float.toHexString(f)};
            }
            case PARSE_FLOAT: {
                String s = ((String[]) args[0])[0];
                return new float[]{Float.parseFloat(s)};
            }
            case FLOAT_TO_INT_BITS: {
                float f = ((float[]) args[0])[0];
                return new int[]{Float.floatToIntBits(f)};
            }
            case INT_BITS_TO_FLOAT: {
                int i = ((int[]) args[0])[0];
                return new float[]{Float.intBitsToFloat(i)};
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "Float";
    }

    public void destructor() {
    }
}
