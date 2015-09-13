package dina.compiler.builder.program_construction;

import dina.library.*;

public class NativeFunctions {

    public static void init() {
        //MATH
        Function.addNativeFunction(new Function("abs(I)I"), LibratyID.MATH, FunctionsID.ABS_INTEGER);
        Function.addNativeFunction(new Function("abs(F)F"), LibratyID.MATH, FunctionsID.ABS_FLOAT);
        Function.addNativeFunction(new Function("cos(F)F"), LibratyID.MATH, FunctionsID.COS);
        Function.addNativeFunction(new Function("sin(F)F"), LibratyID.MATH, FunctionsID.SIN);
        Function.addNativeFunction(new Function("sqr(I)I"), LibratyID.MATH, FunctionsID.SQR_INTEGER);
        Function.addNativeFunction(new Function("sqr(F)F"), LibratyID.MATH, FunctionsID.SQR_FLOAT);
        Function.addNativeFunction(new Function("sqrt(F)F"), LibratyID.MATH, FunctionsID.SQRT);
        Function.addNativeFunction(new Function("tan(F)F"), LibratyID.MATH, FunctionsID.TAN);
        Function.addNativeFunction(new Function("round(F)I"), LibratyID.MATH, FunctionsID.ROUND);
        Function.addNativeFunction(new Function("random(I)I"), LibratyID.MATH, FunctionsID.RANDOM);
        Function.addNativeFunction(new Function("randomize()V"), LibratyID.MATH, FunctionsID.RANDOM);
        //CONSOLE
        Function.addNativeFunction(new Function("write(B)V"), LibratyID.CONSOLE, FunctionsID.WRITE_BOOLEAN);
        Function.addNativeFunction(new Function("write(C)V"), LibratyID.CONSOLE, FunctionsID.WRITE_CHAR);
        Function.addNativeFunction(new Function("write(I)V"), LibratyID.CONSOLE, FunctionsID.WRITE_INTEGER);
        Function.addNativeFunction(new Function("write(F)V"), LibratyID.CONSOLE, FunctionsID.WRITE_FLOAT);
        Function.addNativeFunction(new Function("write(S)V"), LibratyID.CONSOLE, FunctionsID.WRITE_STRING);
        Function.addNativeFunction(new Function("writeLn(B)V"), LibratyID.CONSOLE, FunctionsID.WRITELN_BOOLEAN);
        Function.addNativeFunction(new Function("writeLn(C)V"), LibratyID.CONSOLE, FunctionsID.WRITELN_CHAR);
        Function.addNativeFunction(new Function("writeLn(I)V"), LibratyID.CONSOLE, FunctionsID.WRITELN_INTEGER);
        Function.addNativeFunction(new Function("writeLn(F)V"), LibratyID.CONSOLE, FunctionsID.WRITELN_FLOAT);
        Function.addNativeFunction(new Function("writeLn(S)V"), LibratyID.CONSOLE, FunctionsID.WRITELN_STRING);
        Function.addNativeFunction(new Function("writeLn()V"), LibratyID.CONSOLE, FunctionsID.WRITELN);
        Function.addNativeFunction(new Function("cls()V"), LibratyID.CONSOLE, FunctionsID.CLS);
        Function.addNativeFunction(new Function("nextInt()I"), LibratyID.CONSOLE, FunctionsID.NEXT_INT);
        Function.addNativeFunction(new Function("nextFloat()F"), LibratyID.CONSOLE, FunctionsID.NEXT_FLOAT);
        Function.addNativeFunction(new Function("nextString()S"), LibratyID.CONSOLE, FunctionsID.NEXT_STRING);
        Function.addNativeFunction(new Function("getKey()I"), LibratyID.CONSOLE, FunctionsID.GET_KEY);
        Function.addNativeFunction(new Function("setState(I)V"), LibratyID.CONSOLE, FunctionsID.SET_STATE);
        Function.addNativeFunction(new Function("getState()I"), LibratyID.CONSOLE, FunctionsID.GET_STATE);
        Function.addNativeFunction(new Function("setTitle(S)V"), LibratyID.CONSOLE, FunctionsID.SET_TITLE);
        Function.addNativeFunction(new Function("setFullScreenMode(B)V"), LibratyID.CONSOLE, FunctionsID.SET_FULL_SCREEN_MODE);
        //SYSTEM
        Function.addNativeFunction(new Function("system(I)I"), LibratyID.SYSTEM, FunctionsID.SYSTEM);
        Function.addNativeFunction(new Function("free()V"), LibratyID.SYSTEM, FunctionsID.FREE);
        Function.addNativeFunction(new Function("halt()V"), LibratyID.SYSTEM, FunctionsID.HALT);
        Function.addNativeFunction(new Function("sleep(I)V"), LibratyID.SYSTEM, FunctionsID.SLEEP);
        Function.addNativeFunction(new Function("getErrorMessage()S"), LibratyID.SYSTEM, FunctionsID.GET_ERROR_MESSAGE);
        Function.addNativeFunction(new Function("yield()V"), LibratyID.SYSTEM, FunctionsID.YIELD);
        Function.addNativeFunction(new Function("trace(S)V"), LibratyID.SYSTEM, FunctionsID.TRACE);
        //DATE
        Function.addNativeFunction(new Function("getYear()I"), LibratyID.DATE, FunctionsID.GET_YEAR);
        Function.addNativeFunction(new Function("getMonth()I"), LibratyID.DATE, FunctionsID.GET_MONTH);
        Function.addNativeFunction(new Function("getDay()I"), LibratyID.DATE, FunctionsID.GET_DAY);
        Function.addNativeFunction(new Function("getHour()I"), LibratyID.DATE, FunctionsID.GET_HOUR);
        Function.addNativeFunction(new Function("getMinute()I"), LibratyID.DATE, FunctionsID.GET_MINUTE);
        Function.addNativeFunction(new Function("getSecond()I"), LibratyID.DATE, FunctionsID.GET_SECOND);
        Function.addNativeFunction(new Function("getMillisecond()I"), LibratyID.DATE, FunctionsID.GET_MILLISECOND);
        //STRING
        Function.addNativeFunction(new Function("charAt(SI)C"), LibratyID.STRING, FunctionsID.CHARAT);
        Function.addNativeFunction(new Function("toCharArray(S)[C"), LibratyID.STRING, FunctionsID.TO_CHAR_ARRAY);
        Function.addNativeFunction(new Function("toLowerCase(S)S"), LibratyID.STRING, FunctionsID.TO_LOWER_CASE);
        Function.addNativeFunction(new Function("toUpperCase(S)S"), LibratyID.STRING, FunctionsID.TO_UPPER_CASE);
        Function.addNativeFunction(new Function("trim(S)S"), LibratyID.STRING, FunctionsID.TRIM);
        Function.addNativeFunction(new Function("length(S)I"), LibratyID.STRING, FunctionsID.LENGTH);
        Function.addNativeFunction(new Function("subString(SII)S"), LibratyID.STRING, FunctionsID.SUBSTRING);
        Function.addNativeFunction(new Function("indexOf(SSI)I"), LibratyID.STRING, FunctionsID.INDEX_OF);
        Function.addNativeFunction(new Function("toString([C)S"), LibratyID.STRING, FunctionsID.TO_STRING);
        //GRAPHICS
        Function.addNativeFunction(new Function("drawArc(IIIIII)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_ARC);
        Function.addNativeFunction(new Function("drawImage(@Image;II)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_IMAGE);
        Function.addNativeFunction(new Function("drawLine(IIII)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_LINE);
        Function.addNativeFunction(new Function("drawRect(IIII)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_RECT);
        Function.addNativeFunction(new Function("drawRoundRect(IIIIII)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_ROUND_RECT);
        Function.addNativeFunction(new Function("drawString(SII)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_STRING);
        Function.addNativeFunction(new Function("drawSubString(SIIII)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_SUBSTRING);
        Function.addNativeFunction(new Function("fillArc(IIIIII)V"), LibratyID.GRAPHICS, FunctionsID.FILL_ARC);
        Function.addNativeFunction(new Function("fillRect(IIII)V"), LibratyID.GRAPHICS, FunctionsID.FILL_RECT);
        Function.addNativeFunction(new Function("fillRoundrect(IIIIII)V"), LibratyID.GRAPHICS, FunctionsID.FILL_ROUND_RECT);
        Function.addNativeFunction(new Function("getWidth()I"), LibratyID.GRAPHICS, FunctionsID.GET_WIDTH);
        Function.addNativeFunction(new Function("getHeight()I"), LibratyID.GRAPHICS, FunctionsID.GET_HEIGHT);
        Function.addNativeFunction(new Function("getImageWidth(@Image;)I"), LibratyID.GRAPHICS, FunctionsID.GET_IMAGE_WIDTH);
        Function.addNativeFunction(new Function("getImageHeight(@Image;)I"), LibratyID.GRAPHICS, FunctionsID.GET_IMAGE_HEIGHT);
        Function.addNativeFunction(new Function("getStringWidth(S)I"), LibratyID.GRAPHICS, FunctionsID.GET_STRING_WIDTH);
        Function.addNativeFunction(new Function("getSubStringWidth(SII)I"), LibratyID.GRAPHICS, FunctionsID.GET_SUBSTRING_WIDTH);
        Function.addNativeFunction(new Function("getFontHeight()I"), LibratyID.GRAPHICS, FunctionsID.GET_FONT_HEIGHT);
        Function.addNativeFunction(new Function("loadImage(S)@Image;"), LibratyID.GRAPHICS, FunctionsID.LOAD_IMAGE);
        Function.addNativeFunction(new Function("plot(II)V"), LibratyID.GRAPHICS, FunctionsID.PLOT);
        Function.addNativeFunction(new Function("repaint()V"), LibratyID.GRAPHICS, FunctionsID.REPAINT);
        Function.addNativeFunction(new Function("repaint(IIII)V"), LibratyID.GRAPHICS, FunctionsID.REPAINT_AREA);
        Function.addNativeFunction(new Function("setClip(IIII)V"), LibratyID.GRAPHICS, FunctionsID.SET_CLIP);
        Function.addNativeFunction(new Function("getClipX()I"), LibratyID.GRAPHICS, FunctionsID.GET_CLIP_X);
        Function.addNativeFunction(new Function("getClipY()I"), LibratyID.GRAPHICS, FunctionsID.GET_CLIP_Y);
        Function.addNativeFunction(new Function("getClipWidth()I"), LibratyID.GRAPHICS, FunctionsID.GET_CLIP_WIDTH);
        Function.addNativeFunction(new Function("getClipHeight()I"), LibratyID.GRAPHICS, FunctionsID.GET_CLIP_HEIGHT);
        Function.addNativeFunction(new Function("setColor(III)V"), LibratyID.GRAPHICS, FunctionsID.SET_COLOR);
        Function.addNativeFunction(new Function("setDefaultFont()V"), LibratyID.GRAPHICS, FunctionsID.SET_DEFAULT_FONT);
        Function.addNativeFunction(new Function("setFont(@Font;)V"), LibratyID.GRAPHICS, FunctionsID.SET_FONT);
        Function.addNativeFunction(new Function("drawOval(IIII)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_OVAL);
        Function.addNativeFunction(new Function("fillOval(IIII)V"), LibratyID.GRAPHICS, FunctionsID.FILL_OVAL);
        Function.addNativeFunction(new Function("drawPolygon([I[I)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_POLYGON);
        Function.addNativeFunction(new Function("drawPolyline([I[I)V"), LibratyID.GRAPHICS, FunctionsID.DRAW_POLYLINE);
        Function.addNativeFunction(new Function("fillPolygon([I[I)V"), LibratyID.GRAPHICS, FunctionsID.FILL_POLYGON);
        Function.addNativeFunction(new Function("getFont(SII)@Font;"), LibratyID.GRAPHICS, FunctionsID.GET_FONT);
        Function.addNativeFunction(new Function("getFontHeight(@Font;)I;"), LibratyID.GRAPHICS, FunctionsID.GET_FONT_HEIGHT_FROM_FONT);
        //EVENTS
        Function.addNativeFunction(new Function("getX()I"), LibratyID.EVENTS, FunctionsID.GET_X);
        Function.addNativeFunction(new Function("getY()I"), LibratyID.EVENTS, FunctionsID.GET_Y);
        Function.addNativeFunction(new Function("getMouseAction()I"), LibratyID.EVENTS, FunctionsID.GET_MOUSE_ACTION);
        Function.addNativeFunction(new Function("keyToAction(I)B"), LibratyID.EVENTS, FunctionsID.KEY_TO_ACTION);
        //IO
        Function.addNativeFunction(new Function("openFile(@File;S)V"), LibratyID.IO, FunctionsID.OPEN_FILE);
        Function.addNativeFunction(new Function("resetFile(@File;)V"), LibratyID.IO, FunctionsID.RESET_FILE);
        Function.addNativeFunction(new Function("reWriteFile(@File;)V"), LibratyID.IO, FunctionsID.REWRITE_FILE);
        Function.addNativeFunction(new Function("closeFile(@File;)V"), LibratyID.IO, FunctionsID.CLOSE_FILE);
        Function.addNativeFunction(new Function("write(@File;I)V"), LibratyID.IO, FunctionsID.WRITE);
        Function.addNativeFunction(new Function("writeChar(@File;C)V"), LibratyID.IO, FunctionsID.WRITE_CHAR_TO_FILE);
        Function.addNativeFunction(new Function("writeInt(@File;I)V"), LibratyID.IO, FunctionsID.WRITE_INTEGER_TO_FILE);
        Function.addNativeFunction(new Function("writeFloat(@File;F)V"), LibratyID.IO, FunctionsID.WRITE_FLOAT_TO_FILE);
        Function.addNativeFunction(new Function("read(@File;)I"), LibratyID.IO, FunctionsID.READ);
        Function.addNativeFunction(new Function("readChar(@File;)C"), LibratyID.IO, FunctionsID.READ_CHAR_FROM_FILE);
        Function.addNativeFunction(new Function("readInteger(@File;)I"), LibratyID.IO, FunctionsID.READ_INTEGER_FROM_FILE);
        Function.addNativeFunction(new Function("readFloat(@File;)F"), LibratyID.IO, FunctionsID.READ_FLOAT_FROM_FILE);
        Function.addNativeFunction(new Function("eof(@File;)B"), LibratyID.IO, FunctionsID.EOF);

        Function.addNativeFunction(new Function("getRoots()[S"), LibratyID.IO, FunctionsID.GET_ROOTS);
    }
}
