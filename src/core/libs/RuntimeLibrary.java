package core.libs;

public class RuntimeLibrary implements AbstractLibrary {

    public static final int FREE_MEMORY = -195529211;//"Runtime.freeMemory()I"
    public static final int TOTAL_MEMORY = 1164717273;//"Runtime.totalMemory()I"
    public static final int MAX_MEMORY = 96242745;//"Runtime.maxMemory()I"
    public static final int GC = -741098653;//"Runtime.gc()V"
    public static final int HALT = 1664001886;//"Runtime.halt()V"
    public static final int TRACE = -876632121;//"Runtime.trace(S)V"
    public static final int GET_ERROR_MESSAGE = -1556201997;//"Runtime.getErrorMessage()S"
    private static Runtime runtime;

    public void constructor() {
        runtime = Runtime.getRuntime();
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case FREE_MEMORY: {
                return new int[]{(int) runtime.freeMemory()};
            }
            case TOTAL_MEMORY: {
                return new int[]{(int) runtime.totalMemory()};
            }
            case MAX_MEMORY: {
                return new int[]{(int) runtime.maxMemory()};
            }
            case GC: {
                runtime.gc();
                return null;
            }
            case HALT: {
            }
            case TRACE: {
            }
            case GET_ERROR_MESSAGE: {
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "Runtime";
    }

    public void destructor() {
        runtime = null;
    }
}
