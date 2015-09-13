package core.libs;

public class SystemLibrary implements AbstractLibrary {

    public static final int SLEEP = 2010287270;//"System.sleep(I)V"
    public static final int YIELD = 1769222535;//"System.yield()V"
    public static final int GC = -1595464518;//"System.gc()V"
    public static final int CURRENT_TIME_MILLIS = 1180183656;//"System.currentTimeMillis()V"
    public static final int ARRAY_COPY = 509195785;//"System.arrayCopy(OIOII)V"

    public void constructor() {
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case SLEEP: {
                try {
                    int time = ((int[]) args[0])[0];
                    Thread.sleep(time);
                } catch (Exception ex) {
                }
                return null;
            }
            case YIELD: {
                Thread.yield();
                return null;
            }
            case GC: {
                System.gc();
                return null;
            }
            case CURRENT_TIME_MILLIS: {
                return new int[]{(int) System.currentTimeMillis()};
            }
            case ARRAY_COPY: {
                Object[] src = (Object[]) args[0];
                int srcPos = ((int[]) args[1])[0];
                Object[] dest = (Object[]) args[2];
                int destPos = ((int[]) args[3])[0];
                int length = ((int[]) args[4])[0];
                System.arraycopy(src, srcPos, dest, destPos, length);
                return null;
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "System";
    }

    public void destructor() {
    }
}
