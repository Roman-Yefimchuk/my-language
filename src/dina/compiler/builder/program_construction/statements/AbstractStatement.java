package dina.compiler.builder.program_construction.statements;

import dina.compiler.parser.*;
import dina.compiler.builder.*;
import dina.compiler.builder.program_construction.*;

public class AbstractStatement extends Node {

    public static final int DO = ParserConstants.DO;
    public static final int IF = ParserConstants.IF;
    public static final int WHILE = ParserConstants.WHILE;
    public static final int RETURN = ParserConstants.RETURN;
    public static final int FOR = ParserConstants.FOR;
    public static final int GOTO = ParserConstants.GOTO;
    public static final int LABEL = +100500;
    public static final int TRY = ParserConstants.TRY;
    public static final int BLOCK = -1;
    public static final int EXIT = -100500;
    public static final int SWITCH = ParserConstants.SWITCH;
    private StatementContainer parentContainer;
    private int statementId;
    private Function function;
    private static long internalLabelsCount = -1;

    public void setParentContainer(StatementContainer parentContainer) {
        this.parentContainer = parentContainer;
    }

    public StatementContainer getParentContainer() {
        return parentContainer;
    }

    public int getStatementId() {
        return statementId;
    }

    public void setStatementId(int statementId) {
        this.statementId = statementId;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public static long nextValue() {
        return internalLabelsCount++;
    }

    public static void reset() {
        internalLabelsCount = -1;
    }

    public static StatementContainer forStatement(int statementId, AbstractStatement statement) {
        StatementContainer parentStatement = statement.parentContainer;
        while (parentStatement != null) {
            if (parentStatement.getStatementId() == statementId) {
                break;
            }
            parentStatement = parentStatement.getParentContainer();
        }
        return parentStatement;
    }

    //глубина, на которой находится контейнер statementId относительно statement
    public static int deepOf(int statementId, AbstractStatement statement) {
        int value = 0;
        StatementContainer parentStatement = statement.parentContainer;
        while (parentStatement != null) {
            if (parentStatement.getStatementId() == statementId) {
                value++;
                break;
            } else {
                parentStatement = parentStatement.getParentContainer();
                value++;
            }
        }
        return value;
    }

    //количество контейнеров, в котором находится statement
    public static int amountOf(int statementId, AbstractStatement statement) {
        int value = 0;
        if (statement.statementId == statementId) {
            value++;
        }
        AbstractStatement parentStatement = statement.parentContainer;
        while (parentStatement != null) {
            if (parentStatement.statementId == statementId) {
                value++;
            }
            parentStatement = parentStatement.parentContainer;
        }
        return value;
    }
}
