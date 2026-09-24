/**
 * class for a card.
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
        String valueStr = (rank == Rank.ACE) ? "1/11" : String.valueOf(rank.getValue());
        return rank.getName() + " " + suit.getName() + " (" + valueStr + ")";
    }
}