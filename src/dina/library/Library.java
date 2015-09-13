package dina.library;

public class Library {

    public static final int TRUE = 1;
    public static final int FALSE = 0;

    public void invoke(int functionID) {
    }

    public void initLibrary() {
    }

    public static void invalidConsoleState() {
        throw new RuntimeException("Invalid console state");
    }
}
