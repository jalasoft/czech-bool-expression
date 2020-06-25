package com.github.jalasoft.expression.czech;

import com.github.jalasoft.expression.czech.exception.ExpressionException;
import com.github.jalasoft.expression.czech.parser.Parser;

/**
 *
 * An entry class for parsing czech boolean expressions and retrieving an expression
 * object that can be evaluated any number of times with distinct contexts.
 *
 * <pre>
 *    BoolExpression expression = new CzechBoolExpression().expression("chyb je více než 5 a není včas");
 *
 *    Context ctx = StandardMapContext.context()
 *                  .identifier("chyb", 6)
 *                  .identifier("včas", false);
 *
 *    boolean result = expression.evaluate(ctx);
 * </pre>
 *
 *
 *
 * @author Jan Laštovička
 */
public final class CzechBoolExpression {

    private final Parser parser;

    public CzechBoolExpression() {
        this.parser = new Parser();
    }

    /**
     * Reads an input text and provides its parsed representation.
     * @param input a text that consists of boolean expressions in Czech language like "číslo1 je větší než 5 nebo číslo2 je menší než 0"
     * @return never null
     * @throws ExpressionException if input is not well formed
     * @throws IllegalArgumentException if input is null or blank
     */
    public BoolExpression parse(String input) throws ExpressionException {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Expression to evaluate must not be null or empty.");
        }

        var builder = new BoolExpressionBuilder();
        parser.parse(input, builder);

        return builder.get();
    }
}
