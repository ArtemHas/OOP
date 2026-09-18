import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("getVisibleCard возвращает первую карту")
    void testVisibleCard() {
        Card firstCard = new Card(Rank.KING, Suit.DIAMONDS);
        dealer.addCard(firstCard);
        dealer.addCard(new Card(Rank.TWO, Suit.CLUBS));

        assertEquals(firstCard, dealer.getVisibleCard());
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
        Shoe shoe = new Shoe(1);
        assertTrue(shoe.needsReshuffle());
    }

    @Test
    @DisplayName("Проверка работы с балансом игрока")
    void testPlayerBalanceOperations() {
        Player p = new Player(1000);

        assertEquals(1000, p.getBalance());

        // Проверяем пополнение баланса
        p.addBalance(500);
        assertEquals(1500, p.getBalance());

        // Проверяем списание баланса
        p.deductBalance(300);
        assertEquals(1200, p.getBalance());
    }

    @Test
    @DisplayName("Проверка методов класса Card и красивого вывода toString")
    void testCardMethodsAndToString() {
        Card card = new Card(Rank.ACE, Suit.SPADES);

        assertEquals(Rank.ACE, card.getRank());

        // Проверяем вызов toString
        String text = card.toString();
        assertNotNull(text);
        assertTrue(text.contains("Туз"));
        assertTrue(text.contains("Пики"));
        assertTrue(text.contains("11"));
    }
    @Test
    @DisplayName("Проверка методов у Enum Rank и Suit")
    void testEnumsCoverage() {
        // Проверяем геттеры ранга и масти
        assertEquals("Червы", Suit.HEARTS.getName());
        assertEquals("Король", Rank.KING.getName());
        assertEquals(10, Rank.KING.getValue());
    }
    @Test
    @DisplayName("Симуляция полного цикла игры BlackjackGame")
    void testBlackjackGameFlow() {
        // Простой сценарий: поставил 100, нажал 0 (хватит), нажал 0 (выйти)
        String simulatedInput = "100\n" // Ставка
                + "0\n"                 // Остановиться (Stand)
                + "0\n";                // Выйти из игры (0)

        InputStream originalSystemIn = System.in;

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

            BlackjackGame game = new BlackjackGame();
            assertDoesNotThrow(() -> game.start());

        } finally {
            System.setIn(originalSystemIn);
        }
    }


}



