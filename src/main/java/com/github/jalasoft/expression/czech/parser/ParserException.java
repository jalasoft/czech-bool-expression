package com.github.jalasoft.expression.czech.parser;

import com.github.jalasoft.expression.czech.exception.ExpressionException;
import com.github.jalasoft.expression.czech.lexan.LexicalSymbol;

public final class ParserException extends ExpressionException {

    private final LexicalSymbol symbol;

    public ParserException(LexicalSymbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getMessage() {
        return "Unexpected lexical symbol found: '" + symbol + "'.";
    }
}
