package ide;

import javax.swing.text.*;

public class Logger {

    private static JTextComponent logComponent;

    public static void initLog(JTextComponent logTextArea) {
        Logger.logComponent = logTextArea;
    }

    public static void clearLog() {
        logComponent.setText("");
        logComponent.validate();
    }

    public static void print(String x) {
        Document doc = logComponent.getDocument();
        if (doc != null) {
            try {
                doc.insertString(doc.getLength(), x, null);
            } catch (Exception ex) {
            }
        }
        logComponent.setCaretPosition(logComponent.getText().length());
        logComponent.validate();
    }

    public static void println(String x) {
        print(x + '\n');
    }

    public static void println() {
        print("\n");
    }
}
