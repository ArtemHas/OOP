package org.example;

/**
 * Represents a number.
 */
public class Number extends Expression {

    private final int value;

    /**
     * Creates a number.
     *
     * @param value number value
     */
    public Number(int value) {
        this.value = value;
    }

    /**
     * Prints the number.
     */
    @Override
    public void print() {
        System.out.println(value);
    }

    /**
     * Returns zero as the derivative of a constant.
     */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Returns the number value.
     */
    @Override
    public int eval(String variables) {
        return value;
    }

    /**
     * Returns the number as a string.
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}