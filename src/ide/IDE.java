package ide;

import ide.console.Console;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import dina.DinaException;
import dina.compiler.*;
import dina.disassembler.*;

public class IDE extends JFrame implements Runnable {

    public static IDE instance;
    public static Thread builder;

    public IDE() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
        } catch (Exception Ex) {
        }
        initComponents();
        Logger.initLog(logTextArea);
        Dimension ss = toolkit.getScreenSize();
        setLocation(new Point((ss.width - getWidth()) / 2, (ss.height - getHeight()) / 2));
        setIconImage(toolkit.createImage(getClass().getResource("/core/logo.png")));
        setTitle("Dina 1.0.0");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane1 = new javax.swing.JScrollPane();
        editor = new javax.swing.JTextArea();
        run = new javax.swing.JButton();
        scrollPane2 = new javax.swing.JScrollPane();
        byteCode = new javax.swing.JTextArea();
        stop = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        editor.setColumns(20);
        editor.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        editor.setRows(5);
        editor.setTabSize(4);
        editor.setText("program HelloWorld\n{\n\n    function getMessage():string;\n    {\n        return \"Hello, world!\";\n    }\n\n    function main():void;\n    {\n        writeLn(getMessage());\n    }\n}");
        editor.setDoubleBuffered(true);
        editor.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        editor.setSelectionColor(new java.awt.Color(176, 197, 227));
        scrollPane1.setViewportView(editor);

        run.setFont(new java.awt.Font("Consolas", 0, 12));
        run.setText("Run");
        run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runActionPerformed(evt);
            }
        });

        byteCode.setColumns(20);
        byteCode.setEditable(false);
        byteCode.setFont(new java.awt.Font("Consolas", 0, 14));
        byteCode.setRows(5);
        byteCode.setDoubleBuffered(true);
        byteCode.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        byteCode.setSelectionColor(new java.awt.Color(176, 197, 227));
        scrollPane2.setViewportView(byteCode);

        stop.setFont(new java.awt.Font("Consolas", 0, 12));
        stop.setText("Stop");
        stop.setEnabled(false);
        stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopActionPerformed(evt);
            }
        });

        logTextArea.setColumns(20);
        logTextArea.setEditable(false);
        logTextArea.setFont(new java.awt.Font("Consolas", 0, 12));
        logTextArea.setRows(5);
        jScrollPane1.setViewportView(logTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(run, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stop, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(scrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                    .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(run)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stop))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runActionPerformed
        synchronized (this) {
            if (builder == null) {
                setCursor(Cursor.WAIT_CURSOR);
                builder = new Thread(this, "program_builder");
                builder.start();
            }
        }
    }//GEN-LAST:event_runActionPerformed

    private void stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopActionPerformed
        synchronized (this) {
            if (builder == null) {
                Console.getConsole().halt();
            }
        }
    }//GEN-LAST:event_stopActionPerformed

    public static void installEnabled(boolean value) {
        if (!value) {
            instance.run.setEnabled(false);
            instance.stop.setEnabled(true);
        } else {
            instance.run.setEnabled(true);
            instance.stop.setEnabled(false);
        }
    }

    public static JTextComponent getEditor() {
        return instance.editor;
    }

    public static JTextArea getByteCode() {
        return instance.byteCode;
    }

    public static void main(String args[]) {
        instance = new IDE();
        instance.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea byteCode;
    private javax.swing.JTextArea editor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JButton run;
    private javax.swing.JScrollPane scrollPane1;
    private javax.swing.JScrollPane scrollPane2;
    private javax.swing.JButton stop;
    // End of variables declaration//GEN-END:variables

    public static void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(instance, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void run() {
        Logger.clearLog();
        installEnabled(false);
        try {
            DinaCompiler.buildProject(getEditor().getText());
            try {
                File currentPath = new File("output");
                currentPath.mkdir();
                DataOutputStream unit = new DataOutputStream(new FileOutputStream(new File("output/" + DinaCompiler.getProgram().getName() + ".dp")));
                unit.write(Output.executeFile.toByteArray());
                unit.close();
            } catch (Exception ex) {
                showErrorMessage("Ошибка при записи файла", "Причина: " + ex.getMessage());
                ex.printStackTrace();
            }
            try {
                byteCode.setText("");
                Disassembler.decompile(Output.executeFile);
            } catch (Exception ex) {
                showErrorMessage("Ошибка при декомпиляции", "Причина: " + ex.getMessage());
                ex.printStackTrace();
            }
            try {
                Console.showConsole(DinaCompiler.getProgram().getName());
            } catch (Exception ex) {
                showErrorMessage("Ошибка при запуске консоли", "Причина: " + ex.getMessage());
                ex.printStackTrace();
            }
        } catch (DinaException se) {
            showErrorMessage(se.getErrorName(), se.getMessage());
            se.printStackTrace();
            installEnabled(true);
            //se.showError();
        }
        setCursor(Cursor.DEFAULT_CURSOR);
        builder = null;
    }
}
