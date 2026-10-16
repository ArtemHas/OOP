package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * The main class.
 */
public class Main {

    /**
     * The entry point of the program.
     *
     * @param args optional path to a file containing an expression
     * @throws IOException if the expression file cannot be read
     */
    public static void main(String[] args) throws IOException {

        String source;
        if (args.length == 0) {
            System.out.println("Enter an expression:");
            Scanner scanner = new Scanner(System.in);
            source = scanner.nextLine();
        } else if (args.length == 1) {
            source = Files.readString(Path.of(args[0]));
        } else {
            throw new IllegalArgumentException("Usage: Main [expression-file]");
        }
        Expression e = ExpressionParser.parse(source);

        System.out.println("Expression:");
        e.print();

        Expression de = e.derivative("x");

        System.out.println("Derivative:");
        de.print();

        int result = e.eval("x = 10; y = 13");

        System.out.println("Result:");
        System.out.println(result);
    }
}