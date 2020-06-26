package com.github.jalasoft.expression.czech;

import com.github.jalasoft.expression.czech.parser.ConditionalOperation;
import com.github.jalasoft.expression.czech.parser.ExpressionListener;
import com.github.jalasoft.expression.czech.parser.RelationalOperation;

import java.util.LinkedList;

/**
 * Listener of the parser, that constructs {@link BoolExpression}
 *
 * @author Jan Laštovička
 */
final class BoolExpressionBuilder implements ExpressionListener {

    private final LinkedList<BoolExpression> stack = new LinkedList<BoolExpression>();
    private boolean and;

    @Override
    public void exp(String lOperandIdent, RelationalOperation operation, int rOperand) {
        push(ctx -> operation.evaluate(ctx.number(lOperandIdent), rOperand));
    }

    @Override
    public void exp(String lOperandIdent, RelationalOperation operation, String rOperandIdent) {
        push(ctx -> operation.evaluate(ctx.number(lOperandIdent), ctx.number(rOperandIdent)));
    }

    @Override
    public void exp(String operandIdent, ConditionalOperation operation) {
        push(ctx -> operation.evaluate(ctx.bool(operandIdent)));
    }

    private void push(BoolExpression exp) {
        if (this.and) {
            var other = stack.pop();
            //apply AND operator
            BoolExpression newExp = ctx -> exp.evaluate(ctx) && other.evaluate(ctx);
            stack.push(newExp);
            this.and = false;
        } else {
            stack.push(exp);
        }
    }

    @Override
    public void and() {
        this.and = true;
    }

    @Override
    public void or() {

    }

    public BoolExpression get() {
        //apply OR operator
        BoolExpression root = ctx -> stack.stream().anyMatch(exp -> exp.evaluate(ctx));

        return ctx -> {
            if (ctx == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            return root.evaluate(ctx);
        };
    }
}
