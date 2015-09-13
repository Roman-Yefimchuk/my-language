package dina.compiler.utils;

import java.io.*;

public class IO {

    public static String readFile(String fileName) {
        try {
            File file = new File(fileName);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            String text = new String(data, "CP1251");
            return text;
        } catch (Exception ex) {
        }
        return null;
    }

}
