package com.github.jalasoft.expression.czech;

import com.github.jalasoft.expression.czech.exception.IdentifierException;

import java.util.HashMap;
import java.util.Map;

public final class StandardMapContext implements Context {

    public static StandardMapContext.Builder context() {
        return new Builder();
    }

    //-----------------------------------------------------------------------
    //INSTANCE SCOPE
    //-----------------------------------------------------------------------

    private final Map<String, Object> identifiers;

    private StandardMapContext(Builder bldr) {
        this.identifiers = bldr.identifiers;
    }

    @Override
    public int number(String identName) {
        if (identName == null || identName.isBlank()) {
            throw new IllegalArgumentException("Identifier name must not be null or empty.");
        }

        Object value = getIdentifierOrThrowException(identName);
        if (value instanceof Integer) {
            return (int) value;
        }

        throw new IdentifierException(identName, "Number identifier '" + identName + "' is not a number.");
    }

    @Override
    public boolean bool(String identName) {
        if (identName == null || identName.isBlank()) {
            throw new IllegalArgumentException("Identifier name must not be null or empty.");
        }

        Object value = getIdentifierOrThrowException(identName);

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        throw new IdentifierException(identName, "Bool identifier '" + identName + "' is not a bool.");
    }

    private Object getIdentifierOrThrowException(String identName) {
        Object value = identifiers.get(identName);

        if (value == null) {
            throw new IdentifierException(identName, "Number identifier '" + identName + "' not found.");
        }

        return value;
    }

    //------------------------------------------------------------------------------
    //BUILDER
    //------------------------------------------------------------------------------

    public static final class Builder {

        private final Map<String, Object> identifiers = new HashMap<>();

        public Builder identifier(String name, int value) {
            addObjectIdentifier(name, value);
            return this;
        }

        public Builder identifier(String name, boolean value) {
            addObjectIdentifier(name, value);
            return this;
        }

        private void addObjectIdentifier(String name, Object value) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Identifier name must not be null or empty.");
            }

            if (identifiers.containsKey(name)) {
                throw new IllegalArgumentException("Context already contains an identifier of name '" + name + "'.");
            }

            identifiers.put(name, value);
        }

        public Context build() {
            return new StandardMapContext(this);
        }
    }
}
