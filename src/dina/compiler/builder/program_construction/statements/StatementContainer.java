package dina.compiler.builder.program_construction.statements;

import java.util.*;
import dina.compiler.builder.*;

public class StatementContainer extends AbstractStatement {

    private ArrayList<Node> nodes;

    public StatementContainer() {
        nodes = new ArrayList<Node>();
    }

    public void addNode(Node node) {
        nodes.add(node.optimize());
    }

    public void insertNode(Node node, int index) {
        nodes.add(index, node.optimize());
    }

    public int getNodesAmount() {
        return nodes.size();
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public int getBeginAddress() {
        return getAddress();
    }

    public int getEndAddress() {
        return getAddress() + getSize();
    }
}
