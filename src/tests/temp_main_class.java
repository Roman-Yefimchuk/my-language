package tests;

import dina.compiler.parser.*;
import development.UnitCompiler;
import java.util.Random;

public class temp_main_class {

    public static void printTokens(String source) {
        AbstractTokenStream tokenStream = new TextTokenStream(source);
        while (true) {
            try {
                Token token = tokenStream.nextToken();
                System.out.println(token);
            } catch (Exception ex) {
                //ex.printStackTrace();
                return;
            }
        }
    }

    private static void println(String[][] s) {
        for (int i = 0; i < s.length; i++) {
            System.out.println("public static final int " + s[i][0] + " = " + s[i][1].hashCode() + ";//\"" + s[i][1] + "\"");
        }
        System.out.println("public void init(){}");
        System.out.println("public Object invoke(int functionId, Object[] args) {");
        System.out.println("switch(functionId){");
        for (int i = 0; i < s.length; i++) {
            System.out.println("case " + s[i][0] + ":{}");
        }
        System.out.println("} return null; }");
        System.out.println("public String getName() {");
        System.out.println("return \"" + s[0][1].substring(0, s[0][1].indexOf('.')) + "\";");
        System.out.println("}");
    }
    private static String[][] $console = {
        {"WRITE_BOOLEAN", "Console.write(B)V"},
        {"WRITE_CHAR", "Console.write(C)V"},
        {"WRITE_INT", "Console.write(I)V"},
        {"WRITE_FLOAT", "Console.write(F)V"},
        {"WRITE_STRING", "Console.write(S)V"},
        {"CLS", "Console.cls()V"},
        {"NEXT_INT", "Console.nextInt()I"},
        {"NEXT_FLOAT", "Console.nextFloat()F"},
        {"NEXT_STRING", "Console.nextString()S"},
        {"GET_KEY", "Console.getKey()I"},
        {"SET_TITLE", "Console.setTitle(S)V"},
        {"SET_STATE", "Console.setState(I)V"},
        {"GET_STATE", "Console.getState()I"}
    };
    private static String[][] $date = {
        {"GET_YEAR", "Date.getYear()I"},
        {"GET_MONTH", "Date.getMonth()I"},
        {"GET_DAY", "Date.getDay()I"},
        {"GET_HOUR", "Date.getHour()I"},
        {"GET_MINUTE", "Date.getMinute()I"},
        {"GET_SECOND", "Date.getSecond()I"},
        {"GET_MILLISECOND", "Date.getMillisecond()I"}
    };
    private static String[][] $display = {
        {"SET_FULL_SCREEN_MODE", "Display.setFullScreenMode(B)V"},
        {"GET_WIDTH", "Display.getWidth()I"},
        {"GET_HEIGHT", "Display.getHeight()I"}
    };
    private static String[][] $events = {
        {"GET_X", "Events.getX()I"},
        {"GET_Y", "Events.getY()I"},
        {"GET_MOUSE_ACTION", "Events.getMouseAction()I"},
        {"KEY_TO_ACTION", "Events.keyToAction(I)B"}
    };
    private static String[][] $float = {
        {"TO_STRING", "Float.toString(F)S"},
        {"TO_HEX_STRING", "Float.toHexString(F)S"},
        {"PARSE_FLOAT", "Float.parseFloat(S)F"},
        {"FLOAT_TO_INT_BITS", "Float.floatToIntBits(F)I"},
        {"INT_BITS_TO_FLOAT", "Float.intBitsToFloat(I)F"}
    };
    private static String[][] $graphics = {
        {"DRAW_ARC", "Graphics.drawArc(IIIIII)V"},
        {"DRAW_IMAGE", "Graphics.drawImage(#Graphics.Image;II)V"},
        {"DRAW_LINE", "Graphics.drawLine(IIII)V"},
        {"DRAW_ROUND_RECT", "Graphics.drawRoundRect(IIIIII)V"},
        {"DRAW_STRING", "Graphics.drawString(SII)V"},
        {"DRAW_POLYGON", "Graphics.drawPolygon([I[I)V"},
        {"DRAW_POLYLINE", "Graphics.drawPolyline([I[I)V"},
        {"FILL_ARC", "Graphics.fillArc(IIIIII)V"},
        {"FILL_RECT", "Graphics.fillRect(IIII)V"},
        {"FILL_ROUND_RECT", "Graphics.fillRoundRect(IIIIII)V"},
        {"FILL_OVAL", "Graphics.fillOval(IIII)V"},
        {"FILL_POLYGON", "Graphics.fillPolygon([I[I)V"},
        {"LOAD_IMAGE", "Graphics.loadImage(S)#Graphics.Image;"},
        {"REPAINT", "Graphics.repaint(IIII)V"},
        {"SET_CLIP", "Graphics.setClip(IIII)V"},
        {"SET_COLOR", "Graphics.setColor(III)V"},
        {"SET_DEFAULT_FONT", "Graphics.setDefaultFont()V"},
        {"SET_FONT", "Graphics.setFont(#Graphics.Font;)V"},
        {"GET_IMAGE_WIDTH", "Graphics.getImageWidth(#Graphics.Image;)I"},
        {"GET_IMAGE_HEIGHT", "Graphics.getImageHeight(#Graphics.Image;)I"},
        {"GET_STRING_WIDTH", "Graphics.getStringWidth(S)I"},
        {"GET_CLIP_X", "Graphics.getClipX()I"},
        {"GET_CLIP_Y", "Graphics.getClipY()I"},
        {"GET_CLIP_WIDTH", "Graphics.getClipWidth()I"},
        {"GET_CLIP_HEIGHT", "Graphics.getClipHeight()I"},
        {"GET_FONT", "Graphics.getFont(SII)#Graphics.Font;"},
        {"GET_FONT_HEIGHT", "Graphics.getFontHeight(#Graphics.Font;)I"},
        {"GET_DEFAULT_FONT", "Graphics.getDefaultFont()#Graphics.Font;"}
    };
    private static String[][] $integer = {
        {"TO_STRING", "Integer.toString(II)S"},
        {"PARSE_INT", "Integer.parseInt(SI)I"}
    };
    private static String[][] $math = {
        {"COS", "Math.cos(F)F"},
        {"SIN", "Math.sin(F)F"},
        {"TAN", "Math.tan(F)F"},
        {"ASIN", "Math.asin(F)F"},
        {"ACOS", "Math.acos(F)F"},
        {"ATAN", "Math.atan(F)F"},
        {"EXP", "Math.exp(F)F"},
        {"LOG", "Math.log(F)F"},
        {"LOG10", "Math.log10(F)F"},
        {"SQRT", "Math.sqrt(F)F"},
        {"ATAN2", "Math.atan2(FF)F"},
        {"POW", "Math.pow(FF)F"},
        {"FLOOR", "Math.floor(F)F"},
        {"RANDOM", "Math.random(I)I"},
        {"RANDOMIZE", "Math.randomize()V"},
        {"GET_EXPONENT", "Math.getExponent(F)I"},
        {"RANDOM_MIN_MAX", "Math.random(II)I"}
    };
    private static String[][] $runtime = {
        {"FREE_MEMORY", "Runtime.freeMemory()I"},
        {"TOTAL_MEMORY", "Runtime.totalMemory()I"},
        {"MAX_MEMORY", "Runtime.maxMemory()I"},
        {"GC", "Runtime.gc()V"},
        {"HALT", "Runtime.halt()V"},
        {"TRACE", "Runtime.trace(S)V"},
        {"GET_ERROR_MESSAGE", "Runtime.getErrorMessage()S"}
    };
    private static String[][] $string = {
        {"CHAR_AT", "String.charAt(CI)C"},
        {"TO_CHAR_ARRAY", "String.toCharArray(S)[C"},
        {"TO_LOWER_CASE", "String.toLowerCase(S)S"},
        {"TO_UPPER_CASE", "String.toUpperCase(S)S"},
        {"TRIM", "String.trim(S)S"},
        {"SUB_STRING", "String.subString(SII)S"},
        {"INDEX_OF", "String.indexOf(SSI)I"},
        {"TO_STRING", "String.toString([C)S"},};
    private static String[][] $system = {
        {"SLEEP", "System.sleep(I)V"},
        {"YIELD", "System.yield()V"},
        {"GC", "System.gc()V"},
        {"CURRENT_TIME_MILLIS", "System.currentTimeMillis()V"},
        {"ARRAY_COPY", "System.arrayCopy(OIOII)V"}
    };

    static class A {

        String s;

        A(String s) {
            this.s = s;
        }
    }

    static class B {

        A a;
    }

    static void a() {
        b();
    }

    static void b() {
        a();
    }

    public static void main(String[] args) {
////        UnitCompiler uc = new UnitCompiler();
////        uc.compileUnit("C:/Documents and Settings/foldel/Рабочий стол/MyLanguage/src/core/units/Graphics", "Graphics");
////        printBinaryTokens("output/Graphics.cu");
//        //println($system);
//        int[] i = null;
//        //int[] i = new int[]{25};
//        i[0] = 25;
    }

    public static void printBinaryTokens(String unitName) {
        AbstractTokenStream tokenStream = new BinaryTokenStream(unitName);
        while (true) {
            try {
                Token token = tokenStream.nextToken();
                if (token == null) {
                    break;
                }
                System.out.println(token);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
    }
}
