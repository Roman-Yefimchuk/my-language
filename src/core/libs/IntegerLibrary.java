package core.libs;

public class IntegerLibrary implements AbstractLibrary {

    public static final int TO_STRING = 1510820566;//"Integer.toString(II)S"
    public static final int PARSE_INT = -632904014;//"Integer.parseInt(SI)I"

    public void constructor() {
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case TO_STRING: {
                int i = ((int[]) args[0])[0];
                int radix = ((int[]) args[1])[0];
                return new String[]{Integer.toString(i, radix)};
            }
            case PARSE_INT: {
                String s = ((String[]) args[0])[0];
                int radix = ((int[]) args[1])[0];
                return new int[]{Integer.parseInt(s, radix)};
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "Integer";
    }

    public void destructor() {
    }
}
