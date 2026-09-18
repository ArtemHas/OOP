/**
 * a class for the Dealer.
 */
public class Dealer extends Participant {
    /**
     * gets the first cart of the dealer.
     * @return the first open card that the dealer has.
     */
    public Card getVisibleCard() {
        return cards.get(0);
    }

    /**
     * checks if the dealer must take another card.
     * @return true of false in case the dealer should take another card.
     */
    public boolean shouldHit() {
        return getValue() < 17;
    }

}