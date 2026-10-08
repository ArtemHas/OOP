package org.example;

/**
 * Represents a variable.
 */
public class Variable extends Expression {

    private final String name;

    /**
     * Creates a variable.
     *
     * @param name variable name
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Prints the variable.
     */
    @Override
    public void print() {
        System.out.println(name);
    }

    /**
     * Calculates the derivative of the variable.
     *
     * @param variable variable for differentiation
     * @return 1 if names match, otherwise 0
     */
    @Override
    public Expression derivative(String variable) {
        if (name.equals(variable)) {
            return new Number(1);
        }

        return new Number(0);
    }

    /**
     * Evaluates the variable.
     *
     * @param variables variable assignments
     * @return variable value
     */
    @Override
    public int eval(String variables) {

        String[] assignments = variables.split(";");

        for (String assignment : assignments) {

            String[] parts = assignment.split("=");

            if (parts.length != 2) {
                throw new IllegalArgumentException(
                        "Неверное присваивание: " + assignment
                );
            }

            String variableName = parts[0].trim();
            int value = Integer.parseInt(parts[1].trim());

            if (name.equals(variableName)) {
                return value;
            }
        }

        throw new IllegalArgumentException(
                "Переменная не задана: " + name
        );
    }

    /**
     * Returns the variable name.
     */
    @Override
    public String toString() {
        return name;
    }
}
