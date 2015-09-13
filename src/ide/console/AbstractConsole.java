package ide.console;

public interface AbstractConsole {

    public void write(boolean x);

    public void write(char x);

    public void write(int x);

    public void write(float x);

    public void write(String x);

    public void writeLn(boolean x);

    public void writeLn(char x);

    public void writeLn(int x);

    public void writeLn(float x);

    public void writeLn(String x);

    public void writeLn();

    public void cls();

    public int nextInteger();

    public float nextFloat();

    public String nextString();

    public int getKey();

    public void setConsoleState(int state);

    public int getConsoleState();

    public void setConsoleTitle(String title);

    public void setFullScreenMode(boolean fullScreen);

    public void halt();

    public void showErrorMessage(String message, String title);

    public void showMessage(String message);
}
