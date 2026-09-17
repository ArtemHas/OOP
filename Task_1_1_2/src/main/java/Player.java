/**
 * Class for the Player
 */
public class Player extends Participant {
    private int balance;

    public Player(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void deductBalance(int amount) {
        this.balance -= amount;
    }
}