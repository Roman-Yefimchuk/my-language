package dina.compiler.parser;

import java.io.*;
import java.util.*;

public class BinaryTokenStream implements AbstractTokenStream {

    private ArrayList<Token> stream;
    private ArrayList<String> stringConstantPool;
    private ArrayList<Float> floatConstantPool;
    private DataInputStream dataInputStream;
    private int tokenIndex = -1;

    public BinaryTokenStream(String compileUnitName) {
        try {
            File file = new File(compileUnitName);
            FileInputStream fileInputStream = new FileInputStream(file);
            dataInputStream = new DataInputStream(fileInputStream);
            stringConstantPool = new ArrayList<String>();
            floatConstantPool = new ArrayList<Float>();
            int stringsAmount = dataInputStream.readShort();
            for (int constantIndex = 0; constantIndex < stringsAmount; constantIndex++) {
                stringConstantPool.add(dataInputStream.readUTF());
            }
            int floatAmount = dataInputStream.readShort();
            for (int constantIndex = 0; constantIndex < floatAmount; constantIndex++) {
                floatConstantPool.add(dataInputStream.readFloat());
            }
            int b1 = dataInputStream.read();
            int b2 = dataInputStream.read();
            int b3 = dataInputStream.read();
            int b4 = dataInputStream.read();
            int length = ((b1 << 24) + (b2 << 16) + (b3 << 8) + b4);
            byte[] b = new byte[length];
            dataInputStream.read(b);
            dataInputStream.close();
            fileInputStream.close();
            dataInputStream = new DataInputStream(new ByteArrayInputStream(b));
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        stream = new ArrayList<Token>();
    }

    public Token nextToken() {
        tokenIndex++;
        if (tokenIndex == stream.size()) {
            Token token = readToken();
            if (token == null) {
                return null;
            }
            stream.add(token);
            return token;
        }
        return stream.get(tokenIndex);
    }

    private Token readToken() {
        try {
            int tokenType = dataInputStream.read();
            switch (tokenType) {
                case Token.ttCharConstant: {
                    char c = dataInputStream.readChar();
                    return new Token("\'" + String.valueOf(c) + "\'", Token.ttCharConstant);
                }
                case Token.ttUnknown: {
                    char c = dataInputStream.readChar();
                    return new Token(String.valueOf(c), Token.ttUnknown);
                }
                case Token.ttStringConstant: {
                    int index = dataInputStream.readShort();
                    return new Token("\"" + stringConstantPool.get(index) + "\"", Token.ttStringConstant);
                }
                case Token.ttIdentifier: {
                    int index = dataInputStream.readShort();
                    return new Token(stringConstantPool.get(index), Token.ttIdentifier);
                }
                case Token.ttFloatConstant: {
                    int index = dataInputStream.readShort();
                    float value = floatConstantPool.get(index);
                    return new Token(String.valueOf(value), Token.ttFloatConstant);
                }
                case Token.ttKeyWord: {
                    int id = dataInputStream.read();
                    return new Token(ParserConstants.keyWords[id], Token.ttKeyWord);
                }
                case Token.ttSeparator: {
                    int id = dataInputStream.read();
                    return new Token(ParserConstants.separators[id], Token.ttSeparator);
                }
                case Token.ttOperator: {
                    int id = dataInputStream.read();
                    return new Token(ParserConstants.operators[id], Token.ttOperator);
                }
                case Token.ttIntConstant: {
                    int value = dataInputStream.readInt();
                    return new Token(String.valueOf(value), Token.ttIntConstant);
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
        return null;
    }

    public Token currentToken() {
        return stream.get(tokenIndex);
    }

    public void back() {
        tokenIndex--;
    }

    public void setCurrentToken(Token token) {
        tokenIndex = stream.indexOf(token);
    }
}
