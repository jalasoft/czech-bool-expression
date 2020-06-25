package com.github.jalasoft.expression.czech.lexan;

import com.github.jalasoft.expression.czech.exception.ExpressionException;

public final class LexanException extends ExpressionException {

    private final char symbol;

    LexanException(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getMessage() {
        return "Unexpected symbol '" + symbol + "' found in input sequence.";
    }
}
