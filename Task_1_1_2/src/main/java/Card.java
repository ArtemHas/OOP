/**
 * class for a card
 */
public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank.getName() + " " + suit.getName() + " (" + rank.getValue() + ")";
    }
}