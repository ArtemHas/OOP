package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests expression parsing.
 */
class ExpressionParserTest {

    @Test
    void numberTest() {
        assertEquals(123, ExpressionParser.parse("123").eval(""));
        assertEquals(-5, ExpressionParser.parse("-5").eval(""));
    }

    @Test
    void variableTest() {
        assertEquals("x", ExpressionParser.parse("x").toString());
        assertEquals(10, ExpressionParser.parse("value").eval("value=10"));
    }

    @Test
    void operationsTest() {
        assertEquals(5, ExpressionParser.parse("(3+2)").eval(""));
        assertEquals(1, ExpressionParser.parse("(3-2)").eval(""));
        assertEquals(6, ExpressionParser.parse("(3*2)").eval(""));
        assertEquals(3, ExpressionParser.parse("(6/2)").eval(""));
    }

    @Test
    void nestedExpressionTest() {
        Expression expression = ExpressionParser.parse("(0+((0*x)+(2*1)))");

        assertEquals("(0+((0*x)+(2*1)))", expression.toString());
        assertEquals(2, expression.eval("x=10"));
    }

    @Test
    void invalidExpressionTest() {
        String[] invalidExpressions = {
            "", "(", "(1", "(1+", "(1+2", "(1+2x)", "(1^2)",
            "-", "@", "x)", "x1", "_x", "1+2", "2147483648"
        };

        for (String expression : invalidExpressions) {
            assertThrows(IllegalArgumentException.class,
                    () -> ExpressionParser.parse(expression));
        }
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(null));
    }
}
