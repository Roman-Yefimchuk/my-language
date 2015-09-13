package dina.compiler;

import java.util.*;
import dina.*;
import dina.compiler.builder.objects.*;
import dina.compiler.builder.objects.constants.*;

public class ConstantPool implements GlobalConstantsValue {

    private static Hashtable<String, Constant> constantPool = new Hashtable<String, Constant>();

    static {
        addConstant("FREE_MEMORY", new IntegerConstant(FREE_MEMORY));
        addConstant("TOTAL_MEMORY", new IntegerConstant(TOTAL_MEMORY));
        addConstant("CURRENT_TIME_MILLIS", new IntegerConstant(CURRENT_TIME_MILLIS));
        addConstant("E", new FloatConstant(E));
        addConstant("PI", new FloatConstant(PI));
        addConstant("MAX_FLOAT_VALUE", new FloatConstant(Float.MAX_VALUE));
        addConstant("MIN_FLOAT_VALUE", new FloatConstant(Float.MIN_VALUE));
        addConstant("MAX_INTEGER_VALUE", new IntegerConstant(Integer.MAX_VALUE));
        addConstant("MIN_INTEGER_VALUE", new IntegerConstant(Integer.MIN_VALUE));
        addConstant("MAX_CHAR_VALUE", new IntegerConstant(Character.MAX_VALUE));
        addConstant("MIN_CHAR_VALUE", new IntegerConstant(Character.MIN_VALUE));
        addConstant("STATE_TEXT", new IntegerConstant(STATE_TEXT));
        addConstant("STATE_VGA", new IntegerConstant(STATE_VGA));
        addConstant("MOUSE_UP", new IntegerConstant(MOUSE_UP));
        addConstant("MOUSE_DOWN", new IntegerConstant(MOUSE_DOWN));
        addConstant("MOUSE_MOVE", new IntegerConstant(MOUSE_MOVE));
        addConstant("MOUSE_DRAG", new IntegerConstant(MOUSE_DRAG));
        //keyboard keys
        addConstant("VK_LEFT", new IntegerConstant(0x25));
        addConstant("VK_UP", new IntegerConstant(0x26));
        addConstant("VK_RIGHT", new IntegerConstant(0x27));
        addConstant("VK_DOWN", new IntegerConstant(0x28));
        addConstant("VK_ENTER", new IntegerConstant('\n'));
        addConstant("VK_SPACE", new IntegerConstant(0x20));
        addConstant("VK_BACK_SPACE", new IntegerConstant('\b'));
    }

    private static void addConstant(String name, Constant value) {
        constantPool.put(name, value);
    }

    public static Constant getConstant(String name) {
        return constantPool.get(name);
    }
}
