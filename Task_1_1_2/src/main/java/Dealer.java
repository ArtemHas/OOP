/**
 * a class for the Dealer.
 */
public class Dealer extends Participant {
    /**
     * @return the first open card that the dealer has.
     */
    public Card getVisibleCard() {
        return cards.get(0);
    }

    /**
     * @return true of false in case the dealer should take another card.
     */
    public boolean shouldHit() {
        return getValue() < 17;
    }

}