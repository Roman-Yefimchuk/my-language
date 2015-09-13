package dina.compiler.builder.operators.arithmetic;

import dina.*;
import dina.compiler.*;
import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;
import dina.compiler.builder.objects.constants.*;

public class Add extends PrimitiveExpression {

    private int size = -1;

    private static boolean compare(int leftType, int rightType) {
        return ((leftType >= NodeType.T_CHAR && leftType <= NodeType.T_STRING)
                && (rightType >= NodeType.T_CHAR && rightType <= NodeType.T_STRING));
    }

    private static void init(Node leftOperand, Node rightOperand) {
        if (leftOperand == null || rightOperand == null) {
            throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
        }
        int leftType = leftOperand.getNodeType().getId();
        int rightType = rightOperand.getNodeType().getId();
        int leftArrayDepth = leftOperand.getNodeType().getDimension();
        int rightArrayDepth = rightOperand.getNodeType().getDimension();
        if (compare(leftType, rightType) && leftArrayDepth == 0 && rightArrayDepth == 0) {
            return;
        } else if (leftType == rightType) {
            if (leftArrayDepth == 1 && rightArrayDepth == 1) {//конкатенация массивов
                return;
            } else if (!leftOperand.getNodeType().isRecord() && !rightOperand.getNodeType().isRecord()) {
                if (compare(leftType, rightType)) {
                    return;
                }
            }
        }
        Common.canNotBeApplied("+", leftOperand, rightOperand);
    }

    private static void init(Add add, Node leftOperand, Node rightOperand) {
        init(leftOperand, rightOperand);
        add.leftOperand = leftOperand;
        add.rightOperand = rightOperand;
        int leftType = leftOperand.getNodeType().getId();
        int rightType = rightOperand.getNodeType().getId();
        int arrayDepth = leftOperand.getNodeType().getDimension();
        if (leftType == rightType || Common.isInteger(add.leftOperand, add.rightOperand)) {
            switch (arrayDepth) {
                case 0: {
                    if (leftType == NodeType.T_FLOAT) {
                        if (rightOperand.getNodeClass() == NodeClass.CONSTANT && rightType != NodeType.T_FLOAT) {
                            add.rightOperand = new FloatConstant(rightOperand.getIntegerValue());
                        } else {
                            add.rightOperand = add.rightOperand.optimize();
                        }
                    } else if (rightType == NodeType.T_FLOAT) {
                        if (leftOperand.getNodeClass() == NodeClass.CONSTANT && leftType != NodeType.T_FLOAT) {
                            add.leftOperand = new FloatConstant(leftOperand.getIntegerValue());
                        } else {
                            add.leftOperand = add.leftOperand.optimize();
                        }
                    }
                    break;
                }
            }
        } else {
            if (leftType == NodeType.T_STRING) {
                if (rightOperand.getNodeClass() == NodeClass.CONSTANT && Common.isNumeric(rightOperand.getNodeType())) {
                    add.rightOperand = new StringConstant(rightOperand.getStringValue());
                } else {
                    add.rightOperand = add.rightOperand.optimize();
                }
            } else if (rightType == NodeType.T_STRING) {
                if (leftOperand.getNodeClass() == NodeClass.CONSTANT && Common.isNumeric(leftOperand.getNodeType())) {
                    add.leftOperand = new StringConstant(leftOperand.getStringValue());
                } else {
                    add.leftOperand = add.leftOperand.optimize();
                }
            }
        }
    }

    public Add(Node leftOperand, Node rightOperand) {
        init(this, leftOperand, rightOperand);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.getMaxType(leftOperand.getNodeType(), rightOperand.getNodeType());
    }

    @Override
    public String trace() {
        return leftOperand.trace() + " + " + rightOperand.trace();
    }

    private static void compile0(Add add) {
        int leftType = add.leftOperand.getNodeType().getId();
        int rightType = add.rightOperand.getNodeType().getId();
        int arrayDepth = add.leftOperand.getNodeType().getDimension();
        if (leftType == rightType || Common.isInteger(add.leftOperand, add.rightOperand)) {
            add.leftOperand.compile();
            add.rightOperand.compile();
            switch (arrayDepth) {
                case 0: {
                    switch (leftType) {
                        case NodeType.T_CHAR:
                        case NodeType.T_INT: {
                            Output.writeByte(Constants.IADD);
                            break;
                        }
                        case NodeType.T_FLOAT: {
                            Output.writeByte(Constants.FADD);
                            break;
                        }
                        case NodeType.T_STRING: {
                            Output.writeByte(Constants.SCONCAT);
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    Output.writeByte(Constants.ACONCAT);
                    Output.writeShort(add.leftOperand.getNodeType().getId());
                    break;
                }
            }
        } else {
            switch (leftType) {
                case NodeType.T_STRING: {
                    add.leftOperand.compile();
                    add.rightOperand.compile();
                    switch (rightType) {
                        case NodeType.T_FLOAT: {
                            Output.writeByte(Constants.F2S);
                            break;
                        }
                        case NodeType.T_CHAR: {
                            Output.writeByte(Constants.C2S);
                            break;
                        }
                        case NodeType.T_INT: {
                            Output.writeByte(Constants.I2S);
                            break;
                        }
                    }
                    Output.writeByte(Constants.SCONCAT);
                    break;
                }
                case NodeType.T_FLOAT: {
                    add.leftOperand.compile();
                    switch (rightType) {
                        case NodeType.T_STRING: {
                            Output.writeByte(Constants.F2S);
                            add.rightOperand.compile();
                            Output.writeByte(Constants.SCONCAT);
                            break;
                        }
                        case NodeType.T_CHAR:
                        case NodeType.T_INT: {
                            add.rightOperand.compile();
                            Output.writeByte(Constants.I2F);
                            Output.writeByte(Constants.FADD);
                            break;
                        }
                    }
                    break;
                }
                case NodeType.T_CHAR:
                case NodeType.T_INT: {
                    add.leftOperand.compile();
                    switch (rightType) {
                        case NodeType.T_STRING: {
                            if (leftType == NodeType.T_CHAR) {
                                Output.writeByte(Constants.C2S);
                            } else {
                                Output.writeByte(Constants.I2S);
                            }
                            add.rightOperand.compile();
                            Output.writeByte(Constants.SCONCAT);
                            break;
                        }
                        case NodeType.T_FLOAT: {
                            Output.writeByte(Constants.I2F);
                            add.rightOperand.compile();
                            Output.writeByte(Constants.FADD);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void compile() {
        compile0(this);
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static int getSize0(Add add) {
        int value = 0;
        int leftType = add.leftOperand.getNodeType().getId();
        int rightType = add.rightOperand.getNodeType().getId();
        int arrayDepth = add.leftOperand.getNodeType().getDimension();
        value += add.leftOperand.getSize();
        value += add.rightOperand.getSize();
        if (leftType == rightType || Common.isInteger(add.leftOperand, add.rightOperand)) {
            switch (arrayDepth) {
                case 0: {
                    value += SizeOf.SIZEOF_BYTE;
                    break;
                }
                case 1: {
                    value += SizeOf.SIZEOF_BYTE;
                    value += SizeOf.SIZEOF_SHORT;
                    break;
                }
            }
        } else {
            value += SizeOf.SIZEOF_BYTE;
            value += SizeOf.SIZEOF_BYTE;
        }
        return value;
    }

    @Override
    public Node optimize() {
        if (getNodeClass() == NodeClass.CONSTANT) {
            int leftId = leftOperand.getNodeType().getId();
            int rightId = rightOperand.getNodeType().getId();
            if ((leftId == NodeType.T_CHAR || leftId == NodeType.T_INT)
                    && (rightId == NodeType.T_CHAR || rightId == NodeType.T_INT)) {
                return new IntegerConstant(leftOperand.getIntegerValue() + rightOperand.getIntegerValue());
            }
            if (leftId == NodeType.T_FLOAT && rightId == NodeType.T_FLOAT) {
                float left = leftOperand.getFloatValue();
                float right = rightOperand.getFloatValue();
                return new FloatConstant(left + right);
            }
            if (leftId == NodeType.T_STRING && rightId == NodeType.T_STRING) {
                return new StringConstant(leftOperand.getStringValue() + rightOperand.getStringValue());
            }
        }
        return this;
    }
}
