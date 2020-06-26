package com.github.jalasoft.expression.czech.parser;

/**
 * @author Jan "Honzales" Lastovicka
 */
public interface ConditionalOperation {

    static ConditionalOperation identity() {
        return new IdentityConditionalOperation();
    }

    default ConditionalOperation not() {
        return new NotConditionalOperation(this);
    }

    boolean evaluate(boolean op);

    String asString();
}

final class IdentityConditionalOperation implements ConditionalOperation {
    @Override
    public boolean evaluate(boolean op) {
        return op;
    }

    @Override
    public String asString() {
        return "is";
    }
}

final class NotConditionalOperation implements ConditionalOperation {

    private final ConditionalOperation operation;

    NotConditionalOperation(ConditionalOperation operation) {
        this.operation = operation;
    }

    @Override
    public boolean evaluate(boolean op) {
        return ! operation.evaluate(op);
    }

    @Override
    public String asString() {
        return "!" + operation.asString();
    }
}
