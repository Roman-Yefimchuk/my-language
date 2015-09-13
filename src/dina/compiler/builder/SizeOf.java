package dina.compiler.builder;

public interface SizeOf {

    byte SIZEOF_IADD = 1;
    byte SIZEOF_FADD = 1;
    byte SIZEOF_IDIV = 1;
    byte SIZEOF_FDIV = 1;
    byte SIZEOF_ISUB = 1;
    byte SIZEOF_FSUB = 1;
    byte SIZEOF_IMUL = 1;
    byte SIZEOF_FMUL = 1;
    byte SIZEOF_IMOD = 1;
    byte SIZEOF_FMOD = 1;
    byte SIZEOF_RETURN = 1;
    byte SIZEOF_INVOKENATIVE = 1;
    byte SIZEOF_INVOKE = 1;
    byte SIZEOF_I2F = 1;
    byte SIZEOF_F2S = 1;
    byte SIZEOF_S2F = 1;
    byte SIZEOF_I2S = 1;
    byte SIZEOF_S2I = 1;
    byte SIZEOF_ICONST = 1;
    byte SIZEOF_FCONST = 1;
    byte SIZEOF_SCONST = 1;
    byte SIZEOF_BCONST = 1;
    byte SIZEOF_LLOAD = 1;
    byte SIZEOF_GLOAD = 1;
    byte SIZEOF_LSTORE = 1;
    byte SIZEOF_GSTORE = 1;
    byte SIZEOF_OR = 1;
    byte SIZEOF_AND = 1;
    byte SIZEOF_NOT = 1;
    byte SIZEOF_IF_SCMPEQ = 1;
    byte SIZEOF_IF_SCMPNE = 1;
    byte SIZEOF_IF_ACMPEQ = 1;
    byte SIZEOF_IF_ACMPNE = 1;
    byte SIZEOF_SCONCAT = 1;
    byte SIZEOF_ACONCAT = 1;
    byte SIZEOF_INEG = 1;
    byte SIZEOF_FNEG = 1;
    byte SIZEOF_IFEQ = 1;
    byte SIZEOF_IFNE = 1;
    byte SIZEOF_GOTO = 1;
    byte SIZEOF_IF_ICMPEQ = 1;
    byte SIZEOF_IF_ICMPNE = 1;
    byte SIZEOF_IF_ICMPLT = 1;
    byte SIZEOF_IF_ICMPLE = 1;
    byte SIZEOF_IF_ICMPGT = 1;
    byte SIZEOF_IF_ICMPGE = 1;
    byte SIZEOF_IF_FCMPEQ = 1;
    byte SIZEOF_IF_FCMPNE = 1;
    byte SIZEOF_IF_FCMPLT = 1;
    byte SIZEOF_IF_FCMPLE = 1;
    byte SIZEOF_IF_FCMPGT = 1;
    byte SIZEOF_IF_FCMPGE = 1;
    byte SIZEOF_NEWARRAY = 1;
    byte SIZEOF_POP = 1;
    byte SIZEOF_ARRAYLENGTH = 1;
    byte SIZEOF_STRINGLENGTH = 1;
    byte SIZEOF_I2C = 1;
    byte SIZEOF_IALOAD = 1;
    byte SIZEOF_FALOAD = 1;
    byte SIZEOF_SALOAD = 1;
    byte SIZEOF_IASTORE_0 = 1;
    byte SIZEOF_FASTORE_0 = 1;
    byte SIZEOF_SASTORE_0 = 1;
    byte SIZEOF_IASTORE_1 = 1;
    byte SIZEOF_FASTORE_1 = 1;
    byte SIZEOF_SASTORE_1 = 1;
    byte SIZEOF_C2S = 1;
    byte SIZEOF_TRY = 1;
    byte SIZEOF_BREAK_TRY = 1;
    byte SIZEOF_THROW = 1;
    byte SIZEOF_QUIT = 1;
    byte SIZEOF_RALOAD = 1;
    byte SIZEOF_RASTORE_0 = 1;
    byte SIZEOF_RASTORE_1 = 1;
    byte SIZEOF_PUTFIELD = 1;
    byte SIZEOF_GETFIELD = 1;
    byte SIZEOF_NEW = 1;
    byte SIZEOF_DUP = 1;
    byte SIZEOF_SICONST = 1;
    byte SIZEOF_TABLESWITCH = 1;
    byte SIZEOF_ZCONST = 1;
    byte SIZEOF_CCONST = 1;
    byte SIZEOF_TYPEOF = 1;
    //
    int SIZEOF_BYTE = 1;
    int SIZEOF_SHORT = 2;
    int SIZEOF_INTEGER = 4;
}
