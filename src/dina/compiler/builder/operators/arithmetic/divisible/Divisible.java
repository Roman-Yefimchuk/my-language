package dina.compiler.builder.operators.arithmetic.divisible;

import dina.compiler.builder.*;
import dina.compiler.builder.operators.*;

public class Divisible extends PrimitiveExpression {

    @Override
    public NodeClass getNodeClass() {
        if (rightOperand.getNodeClass() == NodeClass.CONSTANT) {
            if (rightOperand.getNodeType().isInteger()) {
                if (rightOperand.getIntegerValue() == 0) {
                    return NodeClass.VALUE;
                }
            }
        }
        return super.getNodeClass();
    }
}
