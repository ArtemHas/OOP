package org.example;

/**
 * Represents multiplication of two expressions.
 */
public class Mul extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Creates a multiplication expression.
     *
     * @param left left expression
     * @param right right expression
     */
    public Mul(Expression left, Expression right) {
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
     * Calculates the derivative using the product rule.
     *
     * @param variable variable for differentiation
     * @return derivative
     */
    @Override
    public Expression derivative(String variable) {
        return new Add(
                new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))
        );
    }

    /**
     * Evaluates the product.
     */
    @Override
    public int eval(String variables) {
        return left.eval(variables) * right.eval(variables);
    }

    /**
     * Returns the expression as a string.
     */
    @Override
    public String toString() {
        return "(" + left + "*" + right + ")";
    }
}