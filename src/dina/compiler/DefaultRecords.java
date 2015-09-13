package dina.compiler;

import java.util.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.records.*;
import dina.compiler.builder.objects.records.fields.*;
import dina.compiler.builder.program_construction.*;

public class DefaultRecords {

    private static Hashtable defaultRecords = new Hashtable();
    public static final Field object;
    public static final Object EMPTY_OBJECT = new Object();

    static {
        object = new Field();
        addDefault("Image");
        addDefault("File");
        addDefault("Font");
    }

    private static void addDefault(String name) {
        defaultRecords.put(name, EMPTY_OBJECT);
    }

    public static NodeType getDefault(String name) {
        if (defaultRecords.get(name) != null) {
            Program program = DinaCompiler.getProgram();
            RecordContainer defaultContainer = new RecordContainer(name);
            defaultContainer.addFields(object);
            program.addRecord(defaultContainer);
            return defaultContainer.getType();
        }
        return null;
    }
}
