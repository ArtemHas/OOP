package org.example;
/**
 * Represents addition of two expressions.
 */
public class Add extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Creates an addition expression.
     *
     * @param left left expression
     * @param right right expression
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Prints the expression.
     */
    @Override
    public void print() {
        System.out.println(toString());
    }

    /**
     * Calculates the derivative of the sum.
     *
     * @param variable variable for differentiation
     * @return derivative
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(
                left.derivative(variable),
                right.derivative(variable)
        );
    }

    /**
     * Evaluates the sum.
     */
    @Override
    public int eval(String variables) {
        return left.eval(variables) + right.eval(variables);
    }

    /**
     * Returns the expression as a string.
     */
    @Override
    public String toString() {
        return "(" + left + "+" + right + ")";
    }
}
