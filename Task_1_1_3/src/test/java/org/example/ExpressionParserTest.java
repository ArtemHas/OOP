package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests expression parsing.
 */
class ExpressionParserTest {
    @Test
    void parsesParenthesizedOperations() {
        assertEquals(23, ExpressionParser.parse("(3+(2*x))").eval("x = 10"));
        assertEquals(50, ExpressionParser.parse("((3+2)*x)").eval("x = 10"));
        assertEquals(5, ExpressionParser.parse("((10-3)-2)").eval(""));
        assertEquals(2, ExpressionParser.parse("((20/5)/2)").eval(""));
    }

    @Test
    void parsesNestedDerivativeExpression() {
        Expression expression = ExpressionParser.parse("(0+((0*x)+(2*1)))");
        assertEquals("(0+((0*x)+(2*1)))", expression.toString());
        assertEquals(2, expression.eval("x = 10"));
    }

    @Test
    void parsesConstantsAndVariablesWithoutParentheses() {
        assertEquals(-6, ExpressionParser.parse("-6").eval(""));
        assertEquals(Integer.MIN_VALUE, ExpressionParser.parse("-2147483648").eval(""));
        assertEquals(4, ExpressionParser.parse("value").eval("value = 4"));
        assertEquals(-6, ExpressionParser.parse("(2*-3)").eval(""));
    }

    @Test
    void parsedExpressionCanBeDifferentiatedAndParsedAgain() {
        Expression expression = ExpressionParser.parse("(3+(2*x))");
        assertEquals(2, expression.derivative("x").eval("x = 10"));
        assertEquals(expression.toString(),
                ExpressionParser.parse(expression.toString()).toString());
    }

    @Test
    void rejectsInvalidInputAndOperationsWithoutParentheses() {
        String[] invalidExpressions = {
            "", " ", "1 + 2", "(1 + 2", "1 2", "x)",
            "(2 ** 3)", "()", "@", "2147483648", "-2147483649", "(x)",
            "(1 + 2 * 3)", "-(x + 1)", "_x", "x_1", "x1", "abc2"
        };
        for (String source : invalidExpressions) {
            assertThrows(IllegalArgumentException.class,
                    () -> ExpressionParser.parse(source), source);
        }
        assertThrows(IllegalArgumentException.class, () -> ExpressionParser.parse(null));
    }

    @Test
    void parsesIntegerBoundariesAndZero() {
        assertEquals(0, ExpressionParser.parse("0").eval(""));
        assertEquals(0, ExpressionParser.parse("-0").eval(""));
        assertEquals(Integer.MAX_VALUE, ExpressionParser.parse("2147483647").eval(""));
        assertEquals("12345", ExpressionParser.parse("12345").toString());
    }

    @Test
    void parsesSingleLetterVariable() {
        Expression expression = ExpressionParser.parse("x");
        assertEquals("x", expression.toString());
        assertEquals(7, expression.eval("x=7"));
    }

    @Test
    void parsesNegativeRightOperandOfSubtraction() {
        Expression expression = ExpressionParser.parse("(5--2)");
        assertEquals("(5--2)", expression.toString());
        assertEquals(7, expression.eval(""));
    }

    @Test
    void rejectsMissingPartsWithErrorPosition() {
        assertParseError("", "Expected an expression at position 0");
        assertParseError("(", "Expected an expression at position 1");
        assertParseError("(1", "Expected an operator at position 2");
        assertParseError("(1+", "Expected an expression at position 3");
        assertParseError("(1+2", "Expected ')' at position 4");
        assertParseError("(1+2x)", "Expected ')' at position 4");
    }

    @Test
    void rejectsUnknownOperatorAndInvalidOperands() {
        assertParseError("(1^2)", "Expected +, -, * or / at position 3");
        assertParseError("-", "Expected an integer at position 1");
        assertParseError("-x", "Expected an integer at position 1");
        assertParseError("(1+*)", "Expected a constant, variable or '(' at position 3");
    }

    @Test
    void rejectsTrailingCharacters() {
        assertParseError("(1+2))", "Unexpected character at position 5");
        assertParseError("x1", "Unexpected character at position 1");
    }

    @Test
    void reportsIntegerOverflow() {
        assertParseError("2147483648", "Integer out of range at position 10");
        assertParseError("-2147483649", "Integer out of range at position 11");
    }

    private void assertParseError(String source, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> ExpressionParser.parse(source));
        assertEquals(expectedMessage, exception.getMessage());
    }

}
