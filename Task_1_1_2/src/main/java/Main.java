import java.util.Scanner;

/**
 * Main class of the program.
 */
public class Main {
    /**
     * The main entry point of the application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Shoe shoe = new Shoe(6);

        BlackjackGame game = new BlackjackGame(scanner, shoe);
        game.start();
    }
}