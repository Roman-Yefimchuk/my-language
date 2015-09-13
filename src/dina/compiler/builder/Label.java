package dina.compiler.builder;

import java.util.*;
import dina.compiler.builder.program_construction.statements.*;

public class Label extends AbstractStatement {

    private int labelAddress = -1;
    private String name;
    private boolean initialized;
    private boolean called;
    private Stack gotoAddress = new Stack();
    private Stack referencing = new Stack();

    public Label(String name) {
        this.name = name;
    }

    public Stack getGotoAddress() {
        return gotoAddress;
    }

    public Stack getReferencing() {
        return referencing;
    }

    public int getLabelAddress() {
        return labelAddress;
    }

    public void setLabelAddress(int labelAddress) {
        this.labelAddress = labelAddress;
    }

    public String getName() {
        return name;
    }

    public boolean isCalled() {
        return called;
    }

    public void setCalled() {
        called = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized() {
        initialized = true;
    }
}
