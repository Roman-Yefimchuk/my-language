package dina.compiler.builder;

import java.util.*;
import dina.compiler.*;
import dina.compiler.builder.objects.records.*;
import dina.compiler.parser.ParserConstants;

public class NodeType {

    public static final int K_PRIMITIVE = 0;
    public static final int K_OBJECT = 1;
    //
    public static final int T_VOID = -1;
    public static final int T_BOOLEAN = 0;
    public static final int T_CHAR = 1;
    public static final int T_INT = 2;
    public static final int T_FLOAT = 3;
    public static final int T_STRING = 4;
    public static final int T_OBJECT = 5;
    public static final NodeType TYPE_VOID = new NodeType();
    public static final NodeType TYPE_BOOLEAN = new NodeType(T_BOOLEAN, 0, new int[]{});
    public static final NodeType TYPE_CHAR = new NodeType(T_CHAR, 0, new int[]{T_INT, T_FLOAT});
    public static final NodeType TYPE_INT = new NodeType(T_INT, 0, new int[]{T_FLOAT});
    public static final NodeType TYPE_FLOAT = new NodeType(T_FLOAT, 0, new int[]{});
    public static final NodeType TYPE_STRING = new NodeType(T_STRING, 0, new int[]{T_OBJECT});
    public static final NodeType TYPE_OBJECT = new NodeType(T_OBJECT, 0, new int[]{T_OBJECT});
    public static final NodeType[] BASIC_TYPE_TABLE = {
        TYPE_BOOLEAN,
        TYPE_CHAR,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_OBJECT
    };
    public static final String[] BASIC_TYPE_NAMES = {
        ParserConstants.keyWords[ParserConstants.BOOLEAN],
        ParserConstants.keyWords[ParserConstants.CHAR],
        ParserConstants.keyWords[ParserConstants.INT],
        ParserConstants.keyWords[ParserConstants.FLOAT],
        ParserConstants.keyWords[ParserConstants.STRING],
        ParserConstants.keyWords[ParserConstants.OBJECT]
    };
    private int id;
    private int dimension;
    private int[] hierarchy;
    private int kind;
    private RecordContainer recordContainer;
    private static ArrayList<NodeType> typesList;
    private static Hashtable<String, NodeType> typesTable;

    static {
        typesList = new ArrayList<NodeType>();
        typesTable = new Hashtable<String, NodeType>();
    }

    private NodeType() {
        this.id = T_VOID;
        kind = K_PRIMITIVE;
    }

    private NodeType(RecordContainer recordContainer) {
        id = typesList.size();
        kind = K_OBJECT;
        this.recordContainer = recordContainer;
        hierarchy = new int[]{T_OBJECT};
    }

    private NodeType(int id, int dimension, int[] hierarchy) {
        this.id = id;
        this.dimension = dimension;
        this.hierarchy = hierarchy;
        if (id > T_OBJECT) {
            recordContainer = typesList.get(id).recordContainer;
            kind = K_OBJECT;
        } else {
            kind = K_PRIMITIVE;
        }
        if (dimension != 0 || id == T_STRING || id == T_OBJECT) {
            kind = K_OBJECT;
        }
    }

    public static NodeType getPrototype(int id, int dimension) {
        if (id == T_VOID) {
            return TYPE_VOID;
        }
        if (dimension != 0 || (id > T_OBJECT)) {
            return new NodeType(id, dimension, new int[]{T_OBJECT});
        }
        return new NodeType(id, dimension, forNodeType(id).hierarchy);
    }

    public static int getTypesAmount() {
        return typesList.size();
    }

    public static NodeType getType(int index) {
        return typesList.get(index);
    }

    private static void addPrimitiveTypes() {
        for (int typeIndex = 0; typeIndex < BASIC_TYPE_TABLE.length; typeIndex++) {
            typesTable.put(BASIC_TYPE_NAMES[typeIndex], BASIC_TYPE_TABLE[typeIndex]);
            typesList.add(BASIC_TYPE_TABLE[typeIndex]);
        }
    }

    public static boolean addRecord(RecordContainer recordContainer) {
        if (typesTable.get(recordContainer.getName()) != null) {
            return false;
        }
        NodeType record = new NodeType(recordContainer);
        typesList.add(record);
        typesTable.put(recordContainer.getName(), record);
        return true;
    }

    public static NodeType forNodeType(String name) {
        if (name.equals("void")) {
            return TYPE_VOID;
        }
        NodeType type = typesTable.get(name);
        if (type == null) {
            return DefaultRecords.getDefault(name);
        }
        return type;
    }

    public static NodeType forNodeType(int id) {
        return typesList.get(id);
    }

    public static void reset() {
        typesList.clear();
        typesTable.clear();
        addPrimitiveTypes();
    }

    private static String getSignature0(NodeType type) {
        StringBuilder signature = new StringBuilder();
        for (int index = 0; index < type.dimension; index++) {
            signature.append('[');
        }
        switch (type.id) {
            case T_VOID: {
                signature.append('V');
                break;
            }
            case T_BOOLEAN: {
                signature.append('B');
                break;
            }
            case T_CHAR: {
                signature.append('C');
                break;
            }
            case T_INT: {
                signature.append('I');
                break;
            }
            case T_FLOAT: {
                signature.append('F');
                break;
            }
            case T_STRING: {
                signature.append('S');
                break;
            }
            case T_OBJECT: {
                signature.append('O');
                break;
            }
            default: {
                signature.append('@').append(type.recordContainer.getName()).append(';');
                break;
            }
        }
        return signature.toString();
    }

    public String getSignature() {
        return getSignature0(this);
    }

    @Override
    public String toString() {
        if (id == T_VOID) {
            return "void";
        }
        StringBuilder result = new StringBuilder();
        result.append(forName(id));
        for (int i = 0; i < dimension; i++) {
            result.append("[]");
        }
        return result.toString();
    }

    public static String forName(int typeId) {
        if (typeId >= T_BOOLEAN && typeId <= T_OBJECT) {
            return BASIC_TYPE_NAMES[typeId];
        }
        return typesList.get(typeId).recordContainer.getName();
    }

    public static boolean isNumeric(Node node) {
        return isNumeric(node.getNodeType());
    }

    public static boolean isNumeric(NodeType type) {
        return type.id == NodeType.T_CHAR
                || type.id == NodeType.T_INT
                || type.id == NodeType.T_FLOAT;
    }

    public static NodeType getMaxType(NodeType type1, NodeType type2) {
        if (type1.id == T_CHAR && type2.id == T_CHAR) {
            return TYPE_INT;
        }
        return type1.id >= type2.id ? type1 : type2;
    }

    public boolean isInteger() {
        return id == T_INT || id == T_CHAR;
    }

    public boolean isRecord() {
        return recordContainer != null;
    }

    public RecordContainer getRecordContainer() {
        return recordContainer;
    }

    public int getId() {
        return id;
    }

    public int getDimension() {
        return dimension;
    }

    public boolean isArray() {
        return dimension != 0;
    }

    public int getKind() {
        return kind;
    }

    public String kindToString() {
        if (kind == K_OBJECT) {
            return "OBJECT";
        }
        return "PRIMITIVE";
    }

    public static boolean hardCompare(NodeType standart, NodeType input) {
        return standart.id == input.id && standart.dimension == input.dimension;
    }

    public static boolean compare(Node standart, Node input) {
        return compare(standart.getNodeType(), input.getNodeType());
    }

    public static boolean compare(NodeType standart, NodeType input) {
        if (standart.id == T_OBJECT && !standart.isArray() && input.kind == K_OBJECT) {
            return true;
        }
        if (hardCompare(standart, input)) {
            return true;
        }
        if (standart.dimension != input.dimension) {
            return false;
        }
        int id = standart.id;
        int[] hierarchy = input.hierarchy;
        for (int index = 0; index < hierarchy.length; index++) {
            if (hierarchy[index] == id) {
                return true;
            }
        }
        return false;
    }
}
