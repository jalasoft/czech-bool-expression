package com.github.jalasoft.expression.czech.parser;

import com.github.jalasoft.expression.czech.exception.ExpressionException;
import com.github.jalasoft.expression.czech.input.StringInputSystem;
import com.github.jalasoft.expression.czech.lexan.Lexan;
import com.github.jalasoft.expression.czech.lexan.LexanException;
import com.github.jalasoft.expression.czech.lexan.LexicalSymbol;

import static com.github.jalasoft.expression.czech.lexan.LexicalSymbol.Type.*;

/**
 * 1    START        -> COND NEXT
 * 2    COND         -> UNARY_COND
 * 3    COND         -> BINARY_COND
 * 4    UNARY_COND   -> je ident
 * 5    UNARY_COND   -> není ident
 * 6    BINARY_COND  -> ident OP ROPERAND
 * 7    OP           -> je OP2
 * 8    OP           -> není OP2
 * 9    OP2          -> e
 * 10   OP2          -> víc OP2_REST
 * 11   OP2          -> více OP2_REST
 * 12   OP2          -> větší OP2_REST
 * 13   OP2          -> menší OP2_REST
 * 14   OP2          -> méně OP2_REST
 * 15   OP2          -> míň OP2_REST
 * 16   OP2          -> rovno
 * 17   OP2          -> roven
 * 18   OP2          -> rovna
 * 19   OP2          -> stejný jako
 * 20   OP2          -> stejné jako
 * 21   OP2_REST     -> než
 * 22   OP2_REST     -> jak
 * 23   OP2_REST     -> nebo OR_EQUAL
 * 24   OR_EQUAL     -> rovno OP2_REST2
 * 25   OR_EQUAL     -> stejné OP2_REST2
 * 26   OR_EQUAL     -> stejný OP2_REST2
 * 27   OP2_REST2    -> e
 * 28   OP2_REST2    -> jak
 * 29   OP2_REST2    -> jako
 * 30   ROPERAND     -> number
 * 31   ROPERAND     -> ident
 * 32   NEXT         -> e
 * 33   NEXT         -> AND COND NEXT
 * 34  AND           -> a AND_REST
 * 35  AND_REST      -> e
 * 36  AND_REST      -> zároveň
 * 37  NEXT         -> nebo COND NEXT
 *
 *  FIRST(START) = FIRST(COND) = { je, neni, ident }
 *  FIRST(COND) = FIRST(UNARY) ∪ FIRST(BINARY) = { je, neni, ident }
 *  FIRST(UNARY) = { je, neni }
 *  FIRST(BINARY) = { ident }
 *  FIRST(OP) = { je, neni }
 *  FIRST(OP2) = { e, vic, vice, vetsi, vetsi, mensi, mene, min, rovno, roven, rovna, stejny, stejne }
 *  FIRST(OP2_REST) = { nez, jak, nebo }
 *  FIRST(OP2_OR) = { rovno, stejne, stejny }
 *  FIRST(OP2_REST2) = { e, jak, jako }
 *  FIRST(ROPERAND) = { number, ident }
 *  FIRST(NEXT) = { e, a, nebo }
 *  FIRST(AND) = { a }
 *  FIRST(AND_REST) = { e, zaroven }
 *
 *  FOLLOW(OP2) = FOLLOW(OP) = { number, ident }
 *  FOLLOW(OP) = FIRST(ROPERAND) = { number, ident }
 *  FOLLOW(OP2_REST2) = FOLLOW(OP2_OR) = {number, ident }
 *  FOLLOW(OP2_OR) = FOLLOW(OP2_REST) = { number, ident }
 *  FOLLOW(OP2_REST) = FOLLOW(OP2) = { number, ident }
 *  FOLLOW(NEXT) = FOLLOW(S) = { e }
 *  FOLLOW(AND_REST) = FOLLOW(AND) = FIRST(COND) = { je, neni, ident }
 *
 *              ident   number  je  není    neníq   víc     více    větší   menší   méně    míň     rovno   roven   rovna   stejný  stejné  než     jak     nebo    jako    a   e       zaroven
 *  START         1             1    1
 *  COND          3             2    2
 *  UNARY_COND                  4    5
 *  BINARY_COND   6
 *  OP                          7    8
 *  OP2           9       9                         10       11       12     13      14     15       16       17      18      19      20
 *  OP2_REST                                                                                                                                21       22       23
 *  OR_EQUAL                                                                                          24                      26      25
 *  OP2_REST2     27      27                                                                          24                                             28              29
 *  ROPERAND      31      30
 *  NEXT                                                                                                                                                      37            33    32
 *  AND                                                                                                                                                                     34
 *  AND_REST      35            35  35                                                                                                                                                      36
 */
public final class Parser {

    private Lexan lexan;
    private ExpressionListener listener;

    private LexicalSymbol nextSymbol;

    public void parse(String input, ExpressionListener listener) throws ExpressionException {
        this.lexan = new Lexan(new StringInputSystem(input));
        this.listener = listener;
        this.nextSymbol = this.lexan.next();

        start();

        readNext();
        if (!nextSymbol.is(EPSILON)) {
            throw new ParserException(nextSymbol);
        }
    }

    private void readNext() throws LexanException {
        this.nextSymbol = lexan.next();
    }

    private int numberValue() throws ParserException {
        check(NUMBER);
        return (int) nextSymbol.value();
    }

    private String identValue() throws ParserException {
        check(IDENT);
        return (String) nextSymbol.value();
    }

    private void check(LexicalSymbol.Type type) throws ParserException {
       if (this.nextSymbol.type() != type) {
            throw new ParserException(nextSymbol);
       }
    }

    private void start() throws ExpressionException {
        switch (nextSymbol.type()) {
            case IDENT:
            case JE:
            case NENI:
                cond();
                next();
                break;

            default: throw new ParserException(nextSymbol);
        }
    }

    private void cond() throws ExpressionException {
        switch (nextSymbol.type()) {
            case IDENT:
                binary_cond();
                break;

            case JE:
            case NENI:
                unary_cond();
                break;

            default: throw new ParserException(nextSymbol);
        }
    }

    private void unary_cond() throws ExpressionException {
        switch (nextSymbol.type()) {
            case JE:
                readNext();
                var ident1 = identValue();
                listener.exp(ident1, ConditionalOperation.identity());
                readNext();
                break;

            case NENI:
                readNext();
                var ident2 = identValue();
                listener.exp(ident2, ConditionalOperation.identity().not());
                readNext();
                break;

            default: throw new ParserException(nextSymbol);
        }
    }

    private void binary_cond() throws ExpressionException {
           switch (nextSymbol.type()) {
               case IDENT:
                   var ident = identValue();
                   readNext();
                   var operation = op();
                   var symbol = roperand();

                   if (symbol.is(NUMBER)) {
                       listener.exp(ident, operation, (int) symbol.value());
                   } else if (symbol.is(IDENT)) {
                       listener.exp(ident, operation, (String) symbol.value());
                   } else {
                       throw new ParserException(symbol);
                   }
           }
    }

    private RelationalOperation op() throws ExpressionException {
        switch (nextSymbol.type()) {
            case JE:
                readNext();
                return op2();

            case NENI:
                readNext();
                return op2().not();

            default: throw new ParserException(nextSymbol);
        }
    }

    private LexicalSymbol roperand() throws ExpressionException {
        switch (nextSymbol.type()) {
            case NUMBER:
            case IDENT:
                var symbol = nextSymbol;
                readNext();
                return symbol;

            default: throw new ParserException(nextSymbol);
        }
    }

    private RelationalOperation op2() throws ExpressionException {
        switch (nextSymbol.type()) {
            case IDENT:
            case NUMBER:
                return RelationalOperation.equal();

            case VIC:
            case VICE:
            case VETSI:
                readNext();
                var greater = RelationalOperation.greater();
                if (op2_rest()) {
                    greater = greater.or(RelationalOperation.equal());
                }
                return greater;

            case MENSI:
            case MENE:
            case MIN:
                readNext();
                var less = RelationalOperation.less();
                if(op2_rest()) {
                    less = less.or(RelationalOperation.equal());
                }
                return less;

            case ROVNO:
            case ROVEN:
            case ROVNA:
                readNext();
                return RelationalOperation.equal();

            case STEJNY:
            case STEJNE:
                readNext();
                check(JAKO);
                readNext();
                return RelationalOperation.equal();

            default: throw new ParserException(nextSymbol);
        }
    }

    private boolean op2_rest() throws ExpressionException {
        switch (nextSymbol.type()) {
            case NEZ:
            case JAK:
                readNext();
                return false;

            case NEBO:
                readNext();
                or_equal();
                return true;

            default: throw new ParserException(nextSymbol);
        }
    }

    private void or_equal() throws ExpressionException {
        switch (nextSymbol.type()) {
            case ROVNO:
            case STEJNE:
            case STEJNY:
                readNext();
                op2_rest2();
                break;

            default: throw new ParserException(nextSymbol);
        }
    }

    private void op2_rest2() throws ExpressionException {
        switch (nextSymbol.type()) {
            case NUMBER:
            case IDENT:
                break;

            case JAK:
            case JAKO:
                readNext();
                break;

            default: throw new ParserException(nextSymbol);
        }
    }

    private void next() throws ExpressionException {
        switch (nextSymbol.type()) {
            case NEBO:
                listener.or();
                readNext();
                cond();
                next();
                break;

            case A:
                listener.and();
                and();
                cond();
                next();
                break;

            case EPSILON:
                break;

            default: throw new ParserException(nextSymbol);
        }
    }

    private void and() throws ExpressionException {
        switch (nextSymbol.type()) {
            case A:
                readNext();
                andRest();
                break;

            default: throw new ParserException(nextSymbol);
        }
    }

    private void andRest() throws ExpressionException {
        switch (nextSymbol.type()) {
            case IDENT:
            case JE:
            case NENI:
                break;

            case ZAROVEN:
                readNext();
                break;

            default: throw new ParserException(nextSymbol);
        }
    }
}
