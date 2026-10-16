package org.example;

/**
 * Parses expressions enclosed in parentheses.
 */
public final class ExpressionParser {
    private final String source;
    private int position;

    private ExpressionParser(String source) {
        this.source = source;
    }

    /**
     * Parses a constant, a variable or an expression of the form (left operator right).
     *
     * @param source expression text
     * @return parsed expression
     * @throws IllegalArgumentException if the text is invalid
     */
    public static Expression parse(String source) {
        if (source == null) {
            throw new IllegalArgumentException("Expression must not be null");
        }
        ExpressionParser parser = new ExpressionParser(source);
        Expression result = parser.parseExpression();
        if (parser.position != source.length()) {
            throw parser.error("Unexpected character");
        }
        return result;
    }

    private Expression parseExpression() {
        if (position == source.length()) {
            throw error("Expected an expression");
        }
        char current = source.charAt(position);
        if (current == '(') {
            position++;
            final Expression left = parseExpression();
            if (position == source.length()) {
                throw error("Expected an operator");
            }
            char operator = source.charAt(position++);
            if (operator != '+' && operator != '-' && operator != '*' && operator != '/') {
                throw error("Expected +, -, * or /");
            }
            Expression right = parseExpression();
            if (position == source.length() || source.charAt(position) != ')') {
                throw error("Expected ')'");
            }
            position++;
            switch (operator) {
                case '+': return new Add(left, right);
                case '-': return new Sub(left, right);
                case '*': return new Mul(left, right);
                case '/': return new Div(left, right);
                default: throw error("Unknown operator");
            }
        }
        int start = position;
        if (current == '-' || Character.isDigit(current)) {
            if (current == '-') {
                position++;
            }
            int digitsStart = position;
            while (position < source.length() && Character.isDigit(source.charAt(position))) {
                position++;
            }
            if (position == digitsStart) {
                throw error("Expected an integer");
            }
            try {
                return new Number(Integer.parseInt(source.substring(start, position)));
            } catch (NumberFormatException exception) {
                throw error("Integer out of range");
            }
        }
        if (Character.isLetter(current)) {
            position++;
            while (position < source.length()) {
                current = source.charAt(position);
                if (!Character.isLetter(current)) {
                    break;
                }
                position++;
            }
            return new Variable(source.substring(start, position));
        }
        throw error("Expected a constant, variable or '('");
    }

    private IllegalArgumentException error(String message) {
        return new IllegalArgumentException(message + " at position " + position);
    }
}
