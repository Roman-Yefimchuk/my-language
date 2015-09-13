package dina.disassembler;

public interface DasmConstants {

    public static final String[] opcodes = {
        "iconst",//ICONST
        "fconst",//FCONST
        "sconst",//SCONST
        "bconst",//BCONST
        "zconst",//ZCONST
        "cconst",//CCONST
        "siconst",//SICONST
        "new",//NEW
        "lload",//LLOAD
        "gload",//GLOAD
        "lstore",//LSTORE
        "gstore",//GSTORE
        "iaload",//IALOAD
        "faload",//FALOAD
        "saload",//SALOAD
        "raload",//RALOAD
        "oaload",//OALOAD
        "iastore_0",//IASTORE_0
        "fastore_0",//FASTORE_0
        "sastore_0",//SASTORE_0
        "rastore_0",//RASTORE_0
        "oastore_0",//OASTORE_0
        "iastore_1",//IASTORE_1
        "fastore_1",//FASTORE_1
        "sastore_1",//SASTORE_1
        "rastore_1",//RASTORE_1
        "oastore_1",//OASTORE_1
        "pop",//POP
        "dup",//DUP
        "iadd",//IADD
        "fadd",//FADD
        "idiv",//IDIV
        "fdiv",//FDIV
        "isub",//ISUB
        "fsub",//FSUB
        "imul",//IMUL
        "fmul",//FMUL
        "imod",//IMOD
        "fmod",//FMOD
        "ineg",//INEG
        "fneg",//FNEG
        "i2f",//I2F
        "f2s",//F2S
        "s2f",//S2F
        "i2s",//I2S
        "s2i",//S2I
        "c2s",//C2S
        "i2c",//I2C
        "ifeq",//IFEQ
        "ifne",//IFNE
        "or",//OR
        "and",//AND
        "not",//NOT
        "if_icmpeq",//IF_ICMPEQ
        "if_icmpne",//IF_ICMPNE
        "if_icmplt",//IF_ICMPLT
        "if_icmple",//IF_ICMPLE
        "if_icmpgt",//IF_ICMPGT
        "if_icmpge",//IF_ICMPGE
        "if_fcmpeq",//IF_FCMPEQ
        "if_fcmpne",//IF_FCMPNE
        "if_fcmplt",//IF_FCMPLT
        "if_fcmple",//IF_FCMPLE
        "if_fcmpgt",//IF_FCMPGT
        "if_fcmpge",//IF_FCMPGE
        "if_scmpeq",//IF_SCMPEQ
        "if_scmpne",//IF_SCMPNE
        "if_acmpeq",//IF_ACMPEQ
        "if_acmpne",//IF_ACMPNE
        "goto",//GOTO
        "tableswitch",//TABLESWITCH
        "return",//RETURN
        "putfield",//PUTFIELD
        "getfield",//GETFIELD
        "invokenative",//INVOKENATIVE
        "invoke",//INVOKE
        "sconcat",//SCONCAT
        "aconcat",//ACONCAT
        "newarray",//NEWARRAY
        "arraylength",//ARRAYLENGTH
        "stringlength",//STRINGLENGTH
        "try",//TRY
        "break_try",//BREAK_TRY
        "throw",//THROW
        "quit",//QUIT
        "typeof",//TYPEOF
        "checkcast",//CHECKCAST
    };
    public static final Object[] librarys = {
        new Object[]{
            "System",
            new String[]{
                "system(I)I",
                "free()V",
                "halt()V",
                "sleep(I)V",
                "getErrorMessage()S",
                "yield()V",
                "trace(S)V"
            }
        },
        new Object[]{
            "Graphics",
            new String[]{
                "drawArc(IIIIII)V",
                "drawImage(@Image;II)V",
                "drawLine(IIII)V",
                "drawRect(IIII)V",
                "drawRoundRect(IIIIII)V",
                "drawString(SII)V",
                "drawSubString(SIIII)V",
                "fillArc(IIIIII)V",
                "fillRect(IIII)V",
                "fillRoundRect(IIIIII)V",
                "getWidth()I",
                "getHeight()I",
                "getImageWidth(@Image;)I",
                "getImageHeight(@Image;)I",
                "getStringWidth(S)I",
                "getSubstringWidth(SII)I",
                "getFontHeight()I",
                "loadImage(S)@Image;",
                "plot(II)V",
                "repaint()V",
                "repaint(IIII)V",
                "setClip(IIII)V",
                "getClipX()I",
                "getClipY()I",
                "getClipWidth()I",
                "getClipHeight()I",
                "setColor(III)V",
                "setDefaultFont()V",
                "setFont(@Font;)V",
                "drawOval(IIII)V",
                "fillOval(IIII)V",
                "drawPolygon([I[I)V",
                "drawPolyline([I[I)V",
                "fillPolygon([I[I)V",
                "getFont(SII)@Font;",
                "getFontHeight(@Font;)I;"
            }
        },
        new Object[]{
            "Math",
            new String[]{
                "abs(I)I",
                "abs(F)F",
                "cos(F)F",
                "sin(F)F",
                "sqr(I)I",
                "sqr(F)F",
                "sqrt(F)F",
                "tan(F)F",
                "round(F)I",
                "random(I)I",
                "randomize()V"
            }
        },
        new Object[]{
            "Console",
            new String[]{
                "write(B)V",
                "write(C)V",
                "write(I)V",
                "write(F)V",
                "write(S)V",
                "writeLn(B)V",
                "writeLn(C)V",
                "writeLn(I)V",
                "writeLn(F)V",
                "writeLn(S)V",
                "writeLn()V",
                "cls()V",
                "nextInteger()I",
                "nextFloat()F",
                "nextString()S",
                "getKey()I",
                "setState(I)V",
                "getState()I",
                "setTitle(S)V",
                "setFullScreenMode(B)V"
            }
        },
        new Object[]{
            "String",
            new String[]{
                "charAt(SI)C",
                "toCharArray(S)[C",
                "toLowerCase(S)S",
                "toUpperCase(S)S",
                "trim(S)S",
                "length(S)I",
                "subString(SII)S",
                "indexOf(SSI)I",
                "toString([C)S"
            }
        },
        new Object[]{
            "IO",
            new String[]{
                "openFile(@File;S)V",
                "resetFile(@File;)V",
                "rewriteFile(@File;)V",
                "closeFile(@File;)V",
                "write(@File;I)V",
                "writeChar(@File;C)V",
                "writeInteger(@File;I)V",
                "writeFloat(@File;F)V",
                "read(@File;)I",
                "readChar(@File;)C",
                "readInteger(@File;)I",
                "readFloat(@File;)F",
                "eof(@File;)B",
                "getRoots()[S"
            }
        },
        new Object[]{
            "Events",
            new String[]{
                "getX()I",
                "getY()I",
                "getMouseAction()I",
                "keyToAction(I)B"
            }
        },
        new Object[]{
            "UI",
            new String[]{}
        },
        new Object[]{
            "Date",
            new String[]{
                "getYear()I",
                "getMonth()I",
                "getDay()I",
                "getHour()I",
                "getMinute()I",
                "getSecond()I",
                "getMillisecond()I"
            }
        }
    };
}
