package development;

import dina.compiler.parser.*;
import dina.compiler.utils.*;
import java.io.*;
import java.util.*;

public class UnitCompiler {

    private TextTokenStream tokenStream;
    private ArrayList<String> stringConstantPool;
    private ArrayList<Float> floatConstantPool;

    public UnitCompiler() {
        stringConstantPool = new ArrayList<String>();
        floatConstantPool = new ArrayList<Float>();
    }

    private void writeString(String s, DataOutputStream dos) throws Exception {
        int index = stringConstantPool.indexOf(s);
        if (index == -1) {
            stringConstantPool.add(s);
            dos.writeShort(stringConstantPool.size() - 1);
        } else {
            dos.writeShort(index);
        }
    }

    private void writeFloat(float f, DataOutputStream dos) throws Exception {
        int index = floatConstantPool.indexOf(f);
        if (index == -1) {
            floatConstantPool.add(f);
            dos.writeShort(floatConstantPool.size() - 1);
        } else {
            dos.writeShort(index);
        }
    }

    public void compileUnit(String unitName, String compileUnitName) {
        String source = IO.readFile(unitName + ".u");
        tokenStream = new TextTokenStream(source);
        try {
            File currentPath = new File("output");
            currentPath.mkdir();
            File file = new File("output/" + compileUnitName + ".cu");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(arrayOutputStream);
            while (true) {
                Token token = tokenStream.nextToken();
                if (token == null) {
                    break;
                }
                int tokenType = token.getType();
                dos.write(tokenType);
                switch (tokenType) {
                    case Token.ttCharConstant: {
                        dos.writeChar(Token.getChar(token.getName()));
                        break;
                    }
                    case Token.ttUnknown: {
                        dos.writeChar(token.getName().charAt(0));
                        break;
                    }
                    case Token.ttStringConstant: {
                        writeString(Token.getString(token.getName()), dos);
                        break;
                    }
                    case Token.ttIdentifier: {
                        writeString(token.getName(), dos);
                        break;
                    }
                    case Token.ttFloatConstant: {
                        writeFloat(Float.parseFloat(token.getName()), dos);
                        break;
                    }
                    case Token.ttIntConstant: {
                        dos.writeInt(Integer.parseInt(token.getName()));
                        break;
                    }
                    case Token.ttKeyWord: {
                        dos.write(DinaParser.getKeyWord(token.getName()));
                        break;
                    }
                    case Token.ttOperator: {
                        dos.write(DinaParser.getOperator(token.getName()));
                        break;
                    }
                    case Token.ttSeparator: {
                        dos.write(DinaParser.getSeparator(token.getName()));
                        break;
                    }
                }
            }

            int stringsAmount = stringConstantPool.size();
            dataOutputStream.writeShort(stringsAmount);
            for (int constantIndex = 0; constantIndex < stringsAmount; constantIndex++) {
                dataOutputStream.writeUTF(stringConstantPool.get(constantIndex));
            }

            int floatAmount = floatConstantPool.size();
            dataOutputStream.writeShort(floatAmount);
            for (int constantIndex = 0; constantIndex < floatAmount; constantIndex++) {
                dataOutputStream.writeFloat(floatConstantPool.get(constantIndex));
            }

            byte[] b = arrayOutputStream.toByteArray();
            int length = b.length;
            fileOutputStream.write((length >>> 24) & 0xFF);
            fileOutputStream.write((length >>> 16) & 0xFF);
            fileOutputStream.write((length >>> 8) & 0xFF);
            fileOutputStream.write(length & 0xFF);

            fileOutputStream.write(b);

            dos.close();
            arrayOutputStream.close();
            dataOutputStream.close();
            fileOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
