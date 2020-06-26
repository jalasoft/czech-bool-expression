package com.github.jalasoft.expression.czech.parser;

/**
 * A listener of parsing of an expression. Methods in this interface simply
 * receive expressions in an abstract way, ready for further processing, independent
 * of czech language.
 *
 * @author Jan "Honzales" Lastovicka
 */
public interface ExpressionListener {

    void exp(String lOperandIdent, RelationalOperation operation, int rOperand);

    void exp(String lOperandIdent, RelationalOperation operation, String rOperandIdent);

    void exp(String operandIdent, ConditionalOperation operator);

    void and();

    void or();
}
