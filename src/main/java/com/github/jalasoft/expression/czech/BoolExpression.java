package com.github.jalasoft.expression.czech;

import com.github.jalasoft.expression.czech.exception.IdentifierException;

/**
 * An abstraction of an expression that can be evaluated based
 * on provided context and hose result is affected by the
 * identifiers used in the expression and their values provided
 * in the context
 */
public interface BoolExpression {

    /**
     * Performs the evaluation based on values of identifiers provided inside the context
     * @param context must not be null
     * @return true or false
     * @throws IdentifierException if context does not contain an identifier that is required by the expression for evaluation.
     */
    boolean evaluate(Context context);
}
