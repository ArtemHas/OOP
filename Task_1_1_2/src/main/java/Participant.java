import java.util.ArrayList;
import java.util.List;

/**
 * an abstract class that defines the functionality of the player and the dealer.
 */
public abstract class Participant {
    protected final List<Card> cards = new ArrayList<>();

    /**
     * a method to add a Card objects to the cards List.
     *
     * @param card that we want to add.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * clears the cards before a new round.
     */
    public void reset() {
        cards.clear();
    }

    /**
     * Calculates the total value of the cards, adjusting Aces if needed.
     *
     * @return the total point value of the cards.
     */
    public int getValue() {
        int sum = 0;
        int aceCount = 0;

        for (Card card : cards) {
            sum += card.getRank().getValue();
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }

        return sum;
    }

    /**
     * @return true of false whether the getValue is greater than 21.
     */
    public boolean isBust() {
        return getValue() > 21;
    }

    /**
     * @return the list of cards.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * @return true of false whether it is Black Jack.
     */
    public boolean isBlackjack() {
        return cards.size() == 2 && getValue() == 21;
    }

}