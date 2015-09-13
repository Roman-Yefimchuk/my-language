package dina.compiler.builder;

public class NodeClass {

    public static final String[] NAMES = {"value", "variable", "assigment", "constant"};
    public static final int $VALUE = 0;
    public static final int $VARIABLE = 1;
    public static final int $ASSIGNMENT = 2;
    public static final int $CONSTANT = 3;
    private int classId;
    //
    public static final NodeClass VALUE = new NodeClass($VALUE);
    public static final NodeClass VARIABLE = new NodeClass($VARIABLE);
    public static final NodeClass ASSIGNMENT = new NodeClass($ASSIGNMENT);
    public static final NodeClass CONSTANT = new NodeClass($CONSTANT);

    public NodeClass(int classId) {
        this.classId = classId;
    }

    public int getNodeClass() {
        return classId;
    }

    @Override
    public String toString() {
        return NAMES[classId];
    }
}
