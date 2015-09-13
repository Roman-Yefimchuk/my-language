package dina.compiler.builder.program_construction.statements;

import java.util.*;
import dina.*;
import dina.compiler.*;
import dina.compiler.utils.*;
import dina.compiler.builder.*;
import dina.compiler.builder.objects.constants.*;

public class Switch extends StatementContainer {

    private Node expression;
    private SetTable<Integer, StatementContainer> casesContainers;
    private StatementContainer defaultContainer;
    private int size = -1;
    private ArrayList<Fill<StatementContainer>> callBack;

    public Switch(Node expression) {
        setStatementId(SWITCH);
        this.expression = expression;
        casesContainers = new SetTable<Integer, StatementContainer>();
        callBack = new ArrayList<Fill<StatementContainer>>();
    }

    public void addCaseLabel(int value, StatementContainer caseContainer) {
        if (casesContainers.size() != 0) {
            if (casesContainers.getValue(new Integer(value)) != null) {
                throw new DinaException("Дублирующая метка выбора: " + value, DinaException.COMPILATION_ERROR);
            }
        }
        casesContainers.put(value, caseContainer);
    }

    public void setDefaultContainer(StatementContainer defaultContainer) {
        if (this.defaultContainer != null) {
            throw new DinaException("Дублирующая метка 'default'", DinaException.COMPILATION_ERROR);
        }
        this.defaultContainer = defaultContainer;
    }

    @Override
    public void compile() {
        setAddress();
        compile0(this);
    }

    @Override
    public int getSize() {
        if (size == -1) {
            size = getSize0(this);
        }
        return size;
    }

    private static void compile0(Switch switchStmt) {
        StatementContainer defaultContainer = switchStmt.defaultContainer;
        SetTable<Integer, StatementContainer> casesContainers = switchStmt.casesContainers;
        int casesAmount = casesContainers.size();
        switchStmt.expression.compile();
        Output.writeByte(Constants.TABLESWITCH);
        Output.writeShort(casesAmount);
        if (defaultContainer == null) {
            Output.writeInteger(switchStmt.getEndAddress());
        } else {
            switchStmt.putCallBack(defaultContainer);
            Output.writeInteger(-1);
        }
        for (int caseIndex = 0; caseIndex < casesAmount; caseIndex++) {
            int caseLabel = casesContainers.getKey(caseIndex);
            Output.writeInteger(caseLabel);
            switchStmt.putCallBack(casesContainers.getValue(new Integer(caseLabel)));
            Output.writeInteger(-1);
        }
        StatementContainer lastContainer = null;
        StatementContainer currentContainer = null;
        for (int caseIndex = 0; caseIndex < casesAmount; caseIndex++) {
            int caseLabel = casesContainers.getKey(caseIndex);
            currentContainer = casesContainers.getValue(new Integer(caseLabel));
            if (lastContainer != currentContainer) {
                currentContainer.compile();
                lastContainer = currentContainer;
                Output.writeByte(Constants.GOTO);
                Output.writeInteger(switchStmt.getEndAddress());
            }
        }
        if (defaultContainer != null) {
            defaultContainer.compile();
            Output.writeByte(Constants.GOTO);
            Output.writeInteger(switchStmt.getEndAddress());
        }
        switchStmt.fill();
    }

    private void putCallBack(StatementContainer container) {
        int positionInOutput = Output.getSize() - 1;
        callBack.add(new Fill<StatementContainer>(container, positionInOutput));
    }

    private void fill() {
        int size = callBack.size();
        for (int index = 0; index < size; index++) {
            Fill<StatementContainer> fill = callBack.get(index);
            Output.setAddress(fill.getPosition(), fill.getObject().getBeginAddress());
        }
    }

    private static int getSize0(Switch switchStmt) {
        StatementContainer defaultContainer = switchStmt.defaultContainer;
        SetTable<Integer, StatementContainer> casesContainers = switchStmt.casesContainers;
        int casesAmount = casesContainers.size();
        int value = switchStmt.expression.getSize();
        value += SizeOf.SIZEOF_TABLESWITCH;
        value += SizeOf.SIZEOF_SHORT;//cases amount
        value += SizeOf.SIZEOF_INTEGER;//default address
        value += casesAmount * (SizeOf.SIZEOF_INTEGER + SizeOf.SIZEOF_INTEGER);//<case label(4 bytes) : address(4 bytes)>
        StatementContainer lastContainer = null;
        StatementContainer currentContainer = null;
        for (int caseIndex = 0; caseIndex < casesAmount; caseIndex++) {
            int caseLabel = casesContainers.getKey(caseIndex);
            currentContainer = casesContainers.getValue(new Integer(caseLabel));
            if (lastContainer != currentContainer) {
                value += currentContainer.getSize();
                value += SizeOf.SIZEOF_GOTO;
                value += SizeOf.SIZEOF_INTEGER;
                lastContainer = currentContainer;
            }
        }
        if (defaultContainer != null) {
            value += defaultContainer.getSize();
            value += SizeOf.SIZEOF_GOTO;
            value += SizeOf.SIZEOF_INTEGER;
        }
        return value;
    }
}
