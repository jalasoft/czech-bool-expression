package com.github.jalasoft.expression.czech.parser;

public interface RelationalOperation {

    static RelationalOperation greater() {
        return new Greater();
    }

    static RelationalOperation less() {
        return new Less();
    }

    static RelationalOperation equal() {
        return new Equal();
    }

    default RelationalOperation or(RelationalOperation operation2) {
        return new Or(this, operation2);
    }

    default RelationalOperation not() {
        return new Not(this);
    }

    boolean evaluate(int op1, int op2);

    String asString();
}

//---------------------------------------------------------------------------
//PRIVATE OPERATORS
//---------------------------------------------------------------------------

final class Not implements RelationalOperation {

    private final RelationalOperation operation;

    Not(RelationalOperation operation) {
        this.operation = operation;
    }

    @Override
    public boolean evaluate(int op1, int op2) {
        return !this.operation.evaluate(op1, op2);
    }

    @Override
    public String asString() {
        return "!" + this.operation.asString();
    }
}

final class Or implements RelationalOperation {

    private final RelationalOperation operation1;
    private final RelationalOperation operation2;

    Or(RelationalOperation operation1, RelationalOperation operation2) {
        this.operation1 = operation1;
        this.operation2 = operation2;
    }

    @Override
    public boolean evaluate(int op1, int op2) {
        return operation1.evaluate(op1, op2) || operation2.evaluate(op1, op2);
    }

    @Override
    public String asString() {
        return operation1.asString() + " | " + operation2.asString();
    }
}

final class Greater implements RelationalOperation {

    @Override
    public boolean evaluate(int op1, int op2) {
        return op1 > op2;
    }

    @Override
    public String asString() {
        return ">";
    }
}

final class Less implements RelationalOperation {

    @Override
    public boolean evaluate(int op1, int op2) {
        return op1 < op2;
    }

    @Override
    public String asString() {
        return "<";
    }
}

final class Equal implements RelationalOperation {
    @Override
    public boolean evaluate(int op1, int op2) {
        return op1 == op2;
    }

    @Override
    public String asString() {
        return "=";
    }
}


