package tests;

import java.io.*;
import java.util.zip.*;

public class ZipTest {

    public static void main(String[] args) {
        try {
            FileOutputStream zip = new FileOutputStream("A.zip");
            FileInputStream in = new FileInputStream("1.txt");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(zip));
            out.setLevel(Deflater.BEST_COMPRESSION);
            ZipEntry entry = new ZipEntry("1.txt");
            out.putNextEntry(entry);
            out.finish();
            out.close();
        } catch (Exception ex) {
        }
    }
}
