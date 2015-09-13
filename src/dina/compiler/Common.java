package dina.compiler;

import dina.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.*;
import dina.compiler.builder.objects.constants.*;
import dina.compiler.builder.operators.*;

public class Common {

    public static Node checkNode(Node node) {
        if (node.getNodeClass() == NodeClass.ASSIGNMENT) {
            Assignment assignment;
            boolean inBrackets = false;
            if (node instanceof Brackets) {
                Node expression = node;
                while (true) {
                    expression = ((Brackets) expression).getExpression();
                    if (expression instanceof Assignment) {
                        assignment = (Assignment) expression;
                        break;
                    }
                }
                inBrackets = true;
            } else {
                assignment = (Assignment) node;
            }
            Duplicate duplicate = new Duplicate(assignment.getExpression());
            Assignment result = new Assignment(assignment.getVariable(), duplicate, false);
            if (inBrackets) {
                return new Brackets(result);
            }
            return result;
        }
        return node;
    }

    public static Node extractVariable(Node expression) {
        while (true) {
            expression = ((Brackets) expression).getExpression();
            if (expression instanceof Variable) {
                return expression;
            }
        }
    }

    public static NumberConstant extractNumberConstant(Node expression) {
        while (true) {
            if (expression instanceof Brackets) {
                expression = ((Brackets) expression).getExpression();
                if (expression instanceof NumberConstant) {
                    return (NumberConstant) expression;
                }
            } else if (expression instanceof NumberConstant) {
                return (NumberConstant) expression;
            } else {
                return null;
            }
        }
    }

    public static Node extractConstant(Node expression) {
        while (true) {
            if (expression instanceof Brackets) {
                expression = ((Brackets) expression).getExpression();
                if (!(expression instanceof Brackets)) {
                    return expression;
                }
            } else if (!(expression instanceof Brackets)) {
                return expression;
            } else {
                return null;
            }
        }
    }

    //arithmetic
    public static void equalsOrNotEquasl(String op, Node leftOperand, Node rightOperand) {
        int leftId = leftOperand.getNodeType().getId();
        int rightId = rightOperand.getNodeType().getId();
        int leftArrayDepth = leftOperand.getNodeType().getDimension();
        int rightArrayDepth = rightOperand.getNodeType().getDimension();
        if (leftOperand == null || rightOperand == null) {
            throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
        }
        if (leftOperand.getNodeType().isRecord() || rightOperand.getNodeType().isRecord()) {
            canNotBeApplied(op, leftOperand, rightOperand);
        }
        if (NodeType.isNumeric(leftOperand) && NodeType.isNumeric(rightOperand)) {
            if (leftId == NodeType.T_FLOAT) {
                if (rightOperand.getNodeClass() == NodeClass.CONSTANT && rightId != NodeType.T_FLOAT) {
                    rightOperand = new FloatConstant(rightOperand.getIntegerValue());
                }
            } else if (rightId == NodeType.T_FLOAT) {
                if (leftOperand.getNodeClass() == NodeClass.CONSTANT && leftId != NodeType.T_FLOAT) {
                    leftOperand = new FloatConstant(leftOperand.getIntegerValue());
                }
            }
            return;
        }
        if ((leftId != rightId) || (leftArrayDepth != rightArrayDepth)) {
            Common.incomparableTypesException(leftOperand, rightOperand);
        }
    }

    public static void incomparableTypesException(Node leftOperand, Node rightOperand) {
        throw new DinaException("Несовместимые типы данных: '"
                + leftOperand.getNodeType() + "' и '"
                + rightOperand.getNodeType() + "'", DinaException.COMPILATION_ERROR);
    }

    public static void initArithmetic(String op, Node leftOperand, Node rightOperand) {
        if (leftOperand == null || rightOperand == null) {
            throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
        }
        if (NodeType.isNumeric(leftOperand) && NodeType.isNumeric(rightOperand)) {
            if (leftOperand.getNodeType().getDimension() == 0 && rightOperand.getNodeType().getDimension() == 0) {
                return;
            }
        }
        canNotBeApplied(op, leftOperand, rightOperand);
    }

    //logic
    public static void initLogic(String op, Node leftOperand, Node rightOperand) {
        if (leftOperand == null || rightOperand == null) {
            throw new DinaException("Некорректное начало выражения", DinaException.COMPILATION_ERROR);
        }
        if (leftOperand.getNodeType().getId() == NodeType.T_BOOLEAN
                && rightOperand.getNodeType().getId() == NodeType.T_BOOLEAN) {
            if (leftOperand.getNodeType().getDimension() == 0
                    && rightOperand.getNodeType().getDimension() == 0) {
                return;
            }
        }
        canNotBeApplied(op, leftOperand, rightOperand);
    }

    public static boolean isComparableTypes(Node a1, Node a2) {
        return a1.getNodeType().getId() == a2.getNodeType().getId() && a1.getNodeType().getDimension() == a2.getNodeType().getDimension();
    }

    public static boolean isNumeric(NodeType nodeType) {
        return nodeType.getId() == NodeType.T_CHAR || nodeType.getId() == NodeType.T_INT || nodeType.getId() == NodeType.T_FLOAT;
    }

    public static boolean isInteger(Node a1, Node a2) {
        return (a1.getNodeType().getId() == NodeType.T_CHAR
                && a2.getNodeType().getId() == NodeType.T_INT)
                || (a1.getNodeType().getId() == NodeType.T_INT
                && a2.getNodeType().getId() == NodeType.T_CHAR);
    }

    public static void canNotBeApplied(String op, Node leftOperand, Node rightOperand) {
        throw new DinaException("Оператор " + op + " не может быть применен к '"
                + leftOperand.getNodeType() + "' и '" + rightOperand.getNodeType() + "'", DinaException.COMPILATION_ERROR);
    }
}
