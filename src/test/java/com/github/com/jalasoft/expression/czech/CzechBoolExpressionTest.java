package com.github.com.jalasoft.expression.czech;

import com.github.jalasoft.expression.czech.CzechBoolExpression;
import com.github.jalasoft.expression.czech.StandardMapContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import com.github.jalasoft.expression.czech.exception.ExpressionException;

import static com.github.jalasoft.expression.czech.StandardMapContext.context;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CzechBoolExpressionTest {

    private CzechBoolExpression expression;

    @BeforeAll
    public void init() {
        expression = new CzechBoolExpression();
    }

    public Object[][] expressions() {
        return new Object[][] {
                { "je včas" },
                { "prom je rovno 4" },
                { "proměnná je větší než 67 "},
                { "čísílko je větší nebo rovno -854" },
                { "chyb je více než 78 " },
                { "chyb je více nebo rovno 2" },
                { "c je menší než 7" },
                { "číslo je menší nebo rovno 8" },
                { "b je méně než 6" },
                { "prom1 je méně nebo rovno 785544" },
                { "prom1 je rovno 8 nebo prom1 je větší než 89" },
                { "číslo je menší nebo rovno 5 nebo číslo2 je větší než číslo1" },
                { "chyb je více než 5 nebo je včas" },
                { "chyb je méně než 2 nebo není včas" },
                { "číslo1 je menší jak 3 a je validni " },
                { "chyb je míň jak 3 nebo chyb je víc jak 5 a je včas" },
                { "je včas nebo chyb je stejně jako 5" }
        };
    }

    @ParameterizedTest()
    @MethodSource("expressions")
    public void inputExpressionsAreSuccessfullyParsed(String expression) throws ExpressionException {
        var exp = this.expression.parse(expression);

        assertNotNull(exp);
    }

    public Object[][] evaluations() {
        return new Object[][] {
                { "je včas", context().identifier("včas", true), true},
                { "je včas", context().identifier("včas", false), false},
                { "chyb je více než 78 ", context().identifier("chyb", 78), false},
                { "chyb je více než 78 ", context().identifier("chyb", 79), true},
                { "číslo je menší nebo rovno 8", context().identifier("číslo", 8), true },
                { "číslo je menší nebo rovno 8", context().identifier("číslo", 7), true },
                { "číslo je menší nebo rovno 8", context().identifier("číslo", 9), false },
                { "prom1 je větší nebo rovno 8 a prom1 je menší než 89", context().identifier("prom1", 9), true},
                { "je včas nebo chyb je stejně jako 5", context().identifier("včas", false).identifier("chyb", 6), false},
                { "je včas nebo chyb je stejně jako 5", context().identifier("včas", true).identifier("chyb", 1), true},
                { "chyb je míň jak 3 nebo chyb je víc jak 5 a je včas", context().identifier("chyb", 10).identifier("včas", false), false },
                { "chyb je víc jak 5 nebo není včas", context().identifier("chyb", 4).identifier("včas", false), true},
                { "chyb je víc jak 5 nebo není včas", context().identifier("chyb", 6).identifier("včas", true), true},
                { "prom1 je rovno 8 nebo prom1 je větší než 89", context().identifier("prom1", 8), true },
                { "prom1 je rovno 8 nebo prom1 je větší než 89", context().identifier("prom1", 90), true },
                { "prom1 je rovno 8 nebo prom1 je větší než 89", context().identifier("prom1", 60), false },
        };
    }

    @ParameterizedTest
    @MethodSource("evaluations")
    public void expressionWithIdentifiersIsCorrectlyEvaluated(String expression, StandardMapContext.Builder ctxBuilder, boolean expectedResult) throws ExpressionException {

        var ctx = ctxBuilder.build();
        var exp = this.expression.parse(expression);
        var actualResult = exp.evaluate(ctx);

        assertEquals(expectedResult, actualResult);
    }
}
