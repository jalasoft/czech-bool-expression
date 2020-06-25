package com.github.jalasoft.expression.czech.exception;

public final class IdentifierException extends RuntimeException {

    private final String identifier;

    public IdentifierException(String identifier, String message) {
        super(message);
        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }
}
