package org.example;
/**
 * Base class for mathematical expressions.
 */
public abstract class Expression {

    /**
     * Prints the expression.
     */
    public abstract void print();

    /**
     * Calculates the derivative.
     *
     * @param variable variable for differentiation
     * @return derivative
     */
    public abstract Expression derivative(String variable);

    /**
     * Evaluates the expression.
     *
     * @param variables variable assignments
     * @return result
     */
    public abstract int eval(String variables);
}