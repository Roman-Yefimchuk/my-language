package core.libs;

public class ConsoleLibrary implements AbstractLibrary {

    public static final int WRITE_BOOLEAN = -152800177;//"Console.write(B)V"
    public static final int WRITE_CHAR = -152799216;//"Console.write(C)V"
    public static final int WRITE_INT = -152793450;//"Console.write(I)V"
    public static final int WRITE_FLOAT = -152796333;//"Console.write(F)V"
    public static final int WRITE_STRING = -152783840;//"Console.write(S)V"
    public static final int CLS = 1575992994;//"Console.cls()V"
    public static final int NEXT_INT = -1309243645;//"Console.nextInt()I"
    public static final int NEXT_FLOAT = 1740470003;//"Console.nextFloat()F"
    public static final int NEXT_STRING = 43382679;//"Console.nextString()S"
    public static final int GET_KEY = 1816168360;//"Console.getKey()I"
    public static final int SET_TITLE = 175436581;//"Console.setTitle(S)V"
    public static final int SET_STATE = 225291092;//"Console.setState(I)V"
    public static final int GET_STATE = -1843816330;//"Console.getState()I"

    public void constructor() {
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case WRITE_BOOLEAN: {
            }
            case WRITE_CHAR: {
            }
            case WRITE_INT: {
            }
            case WRITE_FLOAT: {
            }
            case WRITE_STRING: {
            }
            case CLS: {
            }
            case NEXT_INT: {
            }
            case NEXT_FLOAT: {
            }
            case NEXT_STRING: {
            }
            case GET_KEY: {
            }
            case SET_TITLE: {
            }
            case SET_STATE: {
            }
            case GET_STATE: {
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "Console";
    }

    public void destructor() {
    }
}
