package org.example;

/**
 * Represents division of two expressions.
 */
public class Div extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Creates a division expression.
     *
     * @param left numerator
     * @param right denominator
     */
    public Div(Expression left, Expression right) {
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
     * Calculates the derivative using the quotient rule.
     *
     * @param variable variable for differentiation
     * @return derivative
     */
    @Override
    public Expression derivative(String variable) {
        return new Div(
                new Sub(
                        new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))
                ),
                new Mul(right, right)
        );
    }

    /**
     * Evaluates the quotient.
     *
     * @throws ArithmeticException if the denominator is zero
     */
    @Override
    public int eval(String variables) {
        int denominator = right.eval(variables);

        if (denominator == 0) {
            throw new ArithmeticException("Деление на ноль");
        }

        return left.eval(variables) / denominator;
    }

    /**
     * Returns the expression as a string.
     */
    @Override
    public String toString() {
        return "(" + left + "/" + right + ")";
    }
}