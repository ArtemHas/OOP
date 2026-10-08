package org.example;

/**
 * Represents subtraction of two expressions.
 */
public class Sub extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Creates a subtraction expression.
     *
     * @param left left expression
     * @param right right expression
     */
    public Sub(Expression left, Expression right) {
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
     * Calculates the derivative of the difference.
     *
     * @param variable variable for differentiation
     * @return derivative
     */
    @Override
    public Expression derivative(String variable) {
        return new Sub(
                left.derivative(variable),
                right.derivative(variable)
        );
    }

    /**
     * Evaluates the difference.
     */
    @Override
    public int eval(String variables) {
        return left.eval(variables) - right.eval(variables);
    }

    /**
     * Returns the expression as a string.
     */
    @Override
    public String toString() {
        return "(" + left + "-" + right + ")";
    }
}