package dina.compiler.builder;

import dina.compiler.parser.ParserConstants;

public class Signature {

    private String functionName;
    private NodeType functionType;
    private Argument[] functionArguments = new Argument[0];

    private Signature() {
    }

    public static Signature getSignature(String signatureLabel) {
        Signature instance = new Signature();
        createSignature(instance, signatureLabel);
        return instance;
    }

    private static void addArgument(Signature instance, NodeType argumentType) {
        Argument argument = new Argument(argumentType, argumentType.getDimension());
        Argument[] tempArguments = new Argument[instance.functionArguments.length + 1];
        System.arraycopy(instance.functionArguments, 0, tempArguments, 0, instance.functionArguments.length);
        tempArguments[instance.functionArguments.length] = argument;
        instance.functionArguments = tempArguments;
        tempArguments = null;
        System.gc();
    }

    private static NodeType getNodeType(String name, int dimension) {
        NodeType result = NodeType.forNodeType(name);
        if (dimension != 0) {
            result = NodeType.getPrototype(result.getId(), dimension);
        }
        return result;
    }

    public String getFunctionName() {
        return functionName;
    }

    public NodeType getFunctionType() {
        return functionType;
    }

    public Argument[] getFunctionArguments() {
        return functionArguments;
    }

    private static void createSignature(Signature instance, String signatureLabel) {
        instance.functionName = signatureLabel.substring(0, signatureLabel.indexOf('('));
        int seek = instance.functionName.length();
        seek++;
        int dimension = 0;
        String name = "";
        boolean exit = false;
        while (true) {
            if (signatureLabel.charAt(seek) == ')') {
                seek++;
                exit = true;
                continue;
            }
            switch (signatureLabel.charAt(seek)) {
                case '[': {
                    dimension++;
                    seek++;
                    continue;
                }
                case 'V': {
                    name = "void";
                    seek++;
                    break;
                }
                case 'B': {
                    name = "boolean";
                    seek++;
                    break;
                }
                case 'C': {
                    name = "char";
                    seek++;
                    break;
                }
                case 'I': {
                    name = "int";
                    seek++;
                    break;
                }
                case 'F': {
                    name = "float";
                    seek++;
                    break;
                }
                case 'S': {
                    name = "string";
                    seek++;
                    break;
                }
                case 'O': {
                    name = "object";
                    seek++;
                    break;
                }
                case '@': {
                    seek++;
                    name = signatureLabel.substring(seek, signatureLabel.indexOf(';', seek));
                    seek += name.length();
                    seek++;
                    break;
                }
            }
            if (exit) {
                instance.functionType = getNodeType(name, dimension);
                break;
            } else {
                addArgument(instance, getNodeType(name, dimension));
            }
            dimension = 0;
        }
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(functionName).append('(');
        for (int argumentIndex = 0; argumentIndex < functionArguments.length; argumentIndex++) {
            result.append(functionArguments[argumentIndex].getNodeType());
            result.append((argumentIndex == functionArguments.length - 1) ? "" : ", ");
        }
        result.append(')');
        return result.toString();
    }
}
