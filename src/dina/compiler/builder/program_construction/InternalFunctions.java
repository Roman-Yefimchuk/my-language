package dina.compiler.builder.program_construction;

import java.util.*;
import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;

public class InternalFunctions {

    private static final Hashtable functions = new Hashtable();
    //
    private static final InternalFunctionContainer[] containers = {
        new InternalFunctionContainer("inttostr", NodeType.TYPE_STRING) {

            public boolean check(Argument[] arguments) {
                if (arguments.length == 1) {
                    NodeType nodeType = arguments[0].getNodeType();
                    if (nodeType.isInteger() && nodeType.getDimension() == 0) {
                        return true;
                    }
                }
                return false;
            }

            public int getSize() {
                int value = 0;
                setArguments();
                value += getArgument(0).getSize();
                value += SizeOf.SIZEOF_I2S;
                return value;
            }

            public void compile() {
                setArguments();
                getArgument(0).compile();
                Output.writeByte(Constants.I2S);
            }
        },
        new InternalFunctionContainer("strtoint", NodeType.TYPE_INT) {

            public boolean check(Argument[] arguments) {
                if (arguments.length == 1) {
                    NodeType nodeType = arguments[0].getNodeType();
                    if (nodeType.getId() == NodeType.T_STRING && nodeType.getDimension() == 0) {
                        return true;
                    }
                }
                return false;
            }

            public int getSize() {
                int value = 0;
                setArguments();
                value += getArgument(0).getSize();
                value += SizeOf.SIZEOF_S2I;
                return value;
            }

            public void compile() {
                setArguments();
                getArgument(0).compile();
                Output.writeByte(Constants.S2I);
            }
        },
        new InternalFunctionContainer("floattostr", NodeType.TYPE_STRING) {

            public boolean check(Argument[] arguments) {
                if (arguments.length == 1) {
                    NodeType nodeType = arguments[0].getNodeType();
                    if (NodeType.isNumeric(nodeType) && nodeType.getDimension() == 0) {
                        return true;
                    }
                }
                return false;
            }

            public int getSize() {
                int value = 0;
                setArguments();
                Node node = getArgument(0);
                value += node.getSize();
                value += SizeOf.SIZEOF_BYTE;
                return value;
            }

            public void compile() {
                setArguments();
                Node node = getArgument(0);
                node.compile();
                if (node.getNodeType().isInteger()) {
                    Output.writeByte(Constants.I2S);
                } else {
                    Output.writeByte(Constants.F2S);
                }
            }
        },
        new InternalFunctionContainer("strtofloat", NodeType.TYPE_FLOAT) {

            public boolean check(Argument[] arguments) {
                if (arguments.length == 1) {
                    NodeType nodeType = arguments[0].getNodeType();
                    if (nodeType.getId() == NodeType.T_STRING && nodeType.getDimension() == 0) {
                        return true;
                    }
                }
                return false;
            }

            public int getSize() {
                int value = 0;
                setArguments();
                Node node = getArgument(0);
                value += node.getSize();
                value += SizeOf.SIZEOF_S2F;
                return value;
            }

            public void compile() {
                setArguments();
                Node node = getArgument(0);
                node.compile();
                Output.writeByte(Constants.S2F);
            }
        },
        new InternalFunctionContainer("chr", NodeType.TYPE_CHAR) {

            public boolean check(Argument[] arguments) {
                if (arguments.length == 1) {
                    NodeType nodeType = arguments[0].getNodeType();
                    if (nodeType.isInteger() && nodeType.getDimension() == 0) {
                        return true;
                    }
                }
                return false;
            }

            public int getSize() {
                int value = 0;
                setArguments();
                value += getArgument(0).getSize();
                value += SizeOf.SIZEOF_I2C;
                return value;
            }

            public void compile() {
                setArguments();
                Node node = getArgument(0);
                node.compile();
                Output.writeByte(Constants.I2C);
            }
        },
        new InternalFunctionContainer("ord", NodeType.TYPE_INT) {

            public boolean check(Argument[] arguments) {
                if (arguments.length == 1) {
                    NodeType nodeType = arguments[0].getNodeType();
                    if (nodeType.getId() == NodeType.T_CHAR && nodeType.getDimension() == 0) {
                        return true;
                    }
                }
                return false;
            }

            public int getSize() {
                int value = 0;
                setArguments();
                value += getArgument(0).getSize();
                return value;
            }

            public void compile() {
                setArguments();
                Node node = getArgument(0);
                node.compile();
            }
        },
    };

    static {
        for (int functionIndex = 0; functionIndex < containers.length; functionIndex++) {
            functions.put(containers[functionIndex].getName(), containers[functionIndex]);
        }
    }

    public static InternalFunctionContainer getFunctionContainer(String functionName) {
        return (InternalFunctionContainer) functions.get(functionName);
    }
}
