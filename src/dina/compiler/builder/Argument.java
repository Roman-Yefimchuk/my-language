package dina.compiler.builder;

public class Argument {

    private NodeType nodeType;

    public Argument(NodeType nodeType, int dimension) {
        if (dimension != 0) {
            this.nodeType = NodeType.getPrototype(nodeType.getId(), dimension);
        } else {
            this.nodeType = nodeType;
        }
    }

    public NodeType getNodeType() {
        return nodeType;
    }
}
