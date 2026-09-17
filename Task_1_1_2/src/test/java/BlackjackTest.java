import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlackjackTest {
    private Player player;
    private Dealer dealer;

    @BeforeEach
    void setUp() {
        player = new Player(10000);
        dealer = new Dealer();
    }

    @Test
    @DisplayName("Простой подсчет очков без Тузов")
    void testSimpleScoresWithoutAces() {
        player.addCard(new Card(Rank.TEN, Suit.SPADES));
        player.addCard(new Card(Rank.SEVEN, Suit.HEARTS));

        assertEquals(17, player.getValue());
        assertFalse(player.isBust());
    }

    @Test
    @DisplayName("Туз считается за 11, если нет перебора")
    void testAceAsEleven() {
        player.addCard(new Card(Rank.ACE, Suit.SPADES));
        player.addCard(new Card(Rank.EIGHT, Suit.CLUBS));

        assertEquals(19, player.getValue());
    }

    @Test
    @DisplayName("Туз превращается из 11 в 1 при переборе")
    void testAceReducesToOneWhenBusting() {
        player.addCard(new Card(Rank.ACE, Suit.SPADES));
        player.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        player.addCard(new Card(Rank.FIVE, Suit.DIAMONDS));

        assertEquals(14, player.getValue());
        assertFalse(player.isBust());
    }

    @Test
    @DisplayName("Два Туза с раздачи дают 12 очков")
    void testTwoAcesHand() {
        player.addCard(new Card(Rank.ACE, Suit.SPADES));
        player.addCard(new Card(Rank.ACE, Suit.HEARTS));

        assertEquals(12, player.getValue());
        assertFalse(player.isBust());
    }

    @Test
    @DisplayName("Три Туза дают 13 очков")
    void testThreeAcesHand() {
        player.addCard(new Card(Rank.ACE, Suit.SPADES));
        player.addCard(new Card(Rank.ACE, Suit.HEARTS));
        player.addCard(new Card(Rank.ACE, Suit.CLUBS));

        assertEquals(13, player.getValue());
    }

    @Test
    @DisplayName("Перебор фиксируется при сумме больше 21")
    void testBustCondition() {
        player.addCard(new Card(Rank.TEN, Suit.SPADES));
        player.addCard(new Card(Rank.SIX, Suit.HEARTS));
        player.addCard(new Card(Rank.SIX, Suit.CLUBS));

        assertEquals(22, player.getValue());
        assertTrue(player.isBust());
    }

    @Test
    @DisplayName("Блэкджек")
    void testNaturalBlackjack() {
        player.addCard(new Card(Rank.ACE, Suit.SPADES));
        player.addCard(new Card(Rank.KING, Suit.HEARTS));

        assertTrue(player.isBlackjack());
        assertEquals(21, player.getValue());
    }

    @Test
    @DisplayName("21 очко из трех карт не Блэкджек")
    void testTwentyOneIsNotBlackjack() {
        player.addCard(new Card(Rank.SEVEN, Suit.SPADES));
        player.addCard(new Card(Rank.SEVEN, Suit.HEARTS));
        player.addCard(new Card(Rank.SEVEN, Suit.CLUBS));

        assertEquals(21, player.getValue());
        assertFalse(player.isBlackjack());
    }

    @Test
    @DisplayName("Сброс карт обнуляет очки")
    void testReset() {
        player.addCard(new Card(Rank.TEN, Suit.SPADES));
        player.reset();

        assertEquals(0, player.getValue());
        assertTrue(player.getCards().isEmpty());
    }

    @Test
    @DisplayName("Дилер обязан брать карту, если очков меньше 17")
    void testDealerMustHitUnder17() {
        dealer.addCard(new Card(Rank.TEN, Suit.SPADES));
        dealer.addCard(new Card(Rank.SIX, Suit.HEARTS));

        assertTrue(dealer.shouldHit());
    }

    @Test
    @DisplayName("Дилер обязан остановиться на 17 очках и выше")
    void testDealerMustStandOn17OrMore() {
        dealer.addCard(new Card(Rank.TEN, Suit.SPADES));
        dealer.addCard(new Card(Rank.SEVEN, Suit.HEARTS));

        assertFalse(dealer.shouldHit());

        dealer.addCard(new Card(Rank.TWO, Suit.CLUBS));
        assertFalse(dealer.shouldHit());
    }

    @Test
    @DisplayName("В 6 колодах ровно 312 карт")
    void testInitialShoeSize() {
        Shoe shoe = new Shoe(6);
        assertEquals(312, shoe.getRemainingCards());
    }

    @Test
    @DisplayName("Раздача карты уменьшает остаток в башмаке")
    void testDealCardDecreasesCount() {
        Shoe shoe = new Shoe(6);
        shoe.dealCard();

        assertEquals(311, shoe.getRemainingCards());
    }

    @Test
    @DisplayName("Порог перетасовки срабатывает при малом количестве карт")
    void testReshuffleThreshold() {
        Shoe shoe = new Shoe(1); // 52 карты (порог 60)
        assertTrue(shoe.needsReshuffle());
    }
}



