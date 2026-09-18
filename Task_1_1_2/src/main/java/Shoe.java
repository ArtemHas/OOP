import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All cards left.
 */
public class Shoe {
    private final int numberOfDecks;
    private final List<Card> cards = new ArrayList<>();

    // Trimmed plastic card: if there are <= 60 cards left, it’s time to shuffle.
    private final int reshuffleThreshold = 60;

    public Shoe(int numberOfDecks) {
        this.numberOfDecks = numberOfDecks;
        resetAndShuffle();
    }

    /**
     * clearing the current shoe, reshuffling.
     */
    public void resetAndShuffle() {
        cards.clear();
        for (int d = 0; d < numberOfDecks; d++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(rank, suit));
                }
            }
        }
        Collections.shuffle(cards);
    }

    /**
     * a method to deal a card.
     *
     * @return a card that dealer took from the top of the shoe.
     */
    public Card dealCard() {
        if (cards.isEmpty()) {
            resetAndShuffle();
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * checks if the shoe needs a reshuffle.
     *
     * @return true or false whether the shoe need to be reshuffled.
     */
    public boolean needsReshuffle() {
        return cards.size() <= reshuffleThreshold;
    }

    /**
     * gets the amount of cards left
     *
     * @return the amount of cards left.
     */
    public int getRemainingCards() {
        return cards.size();
    }
}
