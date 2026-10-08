package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpressionTest {

    @Test
    void numberTest() {
        Expression number = new Number(5);

        assertEquals("5", number.toString());
        assertEquals(5, number.eval("x = 10"));
        assertEquals("0", number.derivative("x").toString());
    }
    @Test
    void numberPrintTest() {
        Expression number = new Number(5);

        number.print();
    }
    @Test
    void variablePrintTest() {
        Expression e = new Variable("x");
        e.print();
    }
    @Test
    void variableDerivativeTest() {
        Expression x = new Variable("x");
        Expression y = new Variable("y");

        assertEquals("1", x.derivative("x").toString());
        assertEquals("0", y.derivative("x").toString());
    }

    @Test
    void variableEvalTest() {
        Expression x = new Variable("x");

        assertEquals(10, x.eval("x = 10; y = 20"));
    }

    @Test
    void addTest() {
        Expression e = new Add(
                new Number(3),
                new Number(2)
        );

        assertEquals("(3+2)", e.toString());
        assertEquals(5, e.eval(""));
        assertEquals("(0+0)", e.derivative("x").toString());
    }

    @Test
    void subTest() {
        Expression e = new Sub(
                new Number(5),
                new Number(2)
        );

        assertEquals("(5-2)", e.toString());
        assertEquals(3, e.eval(""));
        assertEquals("(0-0)", e.derivative("x").toString());
    }

    @Test
    void mulTest() {
        Expression e = new Mul(
                new Number(2),
                new Variable("x")
        );

        assertEquals("(2*x)", e.toString());
        assertEquals(20, e.eval("x = 10"));
        assertEquals("((0*x)+(2*1))", e.derivative("x").toString());
    }

    @Test
    void divTest() {
        Expression e = new Div(
                new Number(10),
                new Variable("x")
        );

        assertEquals("(10/x)", e.toString());
        assertEquals(2, e.eval("x = 5"));
        assertEquals(
                "(((0*x)-(10*1))/(x*x))",
                e.derivative("x").toString()
        );
    }

    @Test
    void divisionByZeroTest() {
        Expression e = new Div(
                new Number(10),
                new Number(0)
        );

        assertThrows(
                ArithmeticException.class,
                () -> e.eval("")
        );
    }

    @Test
    void variableNotFoundTest() {
        Expression x = new Variable("x");

        assertThrows(
                IllegalArgumentException.class,
                () -> x.eval("y = 10")
        );
    }
}