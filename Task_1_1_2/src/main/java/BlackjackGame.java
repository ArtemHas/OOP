import java.util.Scanner;

/**
 * the main class for the Black Jack game.
 */
public class BlackjackGame {
    private final Scanner scanner;
    private final Shoe shoe;
    private final Dealer dealer = new Dealer();
    private final Player player = new Player(10000);

    /**
     * a constructor for the class BlackJackGame
     *
     * @param scanner
     * @param shoe
     */
    public BlackjackGame(Scanner scanner, Shoe shoe) {
        this.scanner = scanner;
        this.shoe = shoe;
    }
    /**
     * a method to start the game.
     */
    public void start() {
        System.out.println("Добро пожаловать в Блэкджек!");

        int roundNumber = 1;

        while (player.getBalance() > 0) {
            System.out.println("\n================ Раунд " + roundNumber + " ================");
            System.out.println("Игрок: Баланс: " + player.getBalance() + " руб.");

            playRound();

            if (player.getBalance() <= 0) {
                System.out.println("\nУ вас закончились фишки! Игра окончена.");
                break;
            }

            if (shoe.needsReshuffle()) {
                System.out.println("\n==================================================");
                System.out.println("[!] Вышла подрезная пластиковая карта!");
                System.out.println("[!] Дилер перемешивает башмак из 6 колод...");
                System.out.println("==================================================");
                shoe.resetAndShuffle();
            }

            System.out.print("\nХотите сыграть следующий раунд?"
                    + "(0 + enter - выйти, enter - продолжить): ");
            String choice = scanner.nextLine();
            if (choice.equals("0")) {
                System.out.println("\nСпасибо за игру. Ваш финальный выигрыш: "
                        + player.getBalance() + " руб.");
                break;
            }
            roundNumber++;
        }
    }

    private void playRound() {
        int bet = askForBet();
        player.deductBalance(bet);

        player.reset();
        dealer.reset();

        player.addCard(shoe.dealCard());
        dealer.addCard(shoe.dealCard());
        player.addCard(shoe.dealCard());
        dealer.addCard(shoe.dealCard());

        System.out.println("\nВаши карты: " + player.getCards()
                + " => " + player.getValue());
        System.out.println("Открытая карта дилера: "
                + dealer.getVisibleCard());

        if (player.isBlackjack()) {
            if (dealer.isBlackjack()) {
                System.out.println("\nУ обоих Блэкджек! Ничья (Пуш).");
                player.addBalance(bet);
            } else {
                int winAmount = (int) (bet * 2.5);
                System.out.println("\nБЛЭКДЖЕК! Вы выиграли "
                        + winAmount + " руб. (выплата 3 к 2)!");
                player.addBalance(winAmount);
            }
            return;
        }

        while (true) {
            System.out.print("\nВведите 1 (взять карту) или 0 (остановиться): ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                Card card = shoe.dealCard();
                player.addCard(card);

                System.out.println("Вы открыли карту: " + card);
                System.out.println("Ваши карты: " + player.getCards()
                        + " => " + player.getValue());

                if (player.isBust()) {
                    System.out.println("\nПеребор! Вы набрали "
                            + player.getValue() + " очков и проиграли ставку.");
                    return;
                }

                if (player.getValue() == 21) {
                    System.out.println("У вас 21 очко! Автоматический переход к дилеру.");
                    break;
                }
            } else if (choice.equals("0")) {
                System.out.println("Вы остановились на "
                        + player.getValue() + " очках.");
                break;
            } else {
                System.out.println("Неверный ввод! Введите только 1 или 0.");
            }
        }

        System.out.println("\n--- Ход дилера ---");
        System.out.println("Все карты дилера: " + dealer.getCards()
                + " => " + dealer.getValue());

        if(dealer.isBlackjack()){
            System.out.println("У дилера блэкджек! Дилер выиграл.");
            return;
        }

        while (dealer.shouldHit()) {
            Card card = shoe.dealCard();
            dealer.addCard(card);
            System.out.println("Дилер берет карту: " + card);
            System.out.println("Карты дилера: " + dealer.getCards()
                    + " => " + dealer.getValue());
        }

        if (dealer.isBust()) {
            System.out.println("\nУ дилера перебор ("
                    + dealer.getValue() + " очков)! ВЫ ВЫИГРАЛИ!");
            player.addBalance(bet * 2);
        } else if (player.getValue() > dealer.getValue()) {
            System.out.println("\nВЫ ВЫИГРАЛИ! (" + player.getValue()
                    + " против " + dealer.getValue() + " у дилера)");
            player.addBalance(bet * 2);
        } else if (player.getValue() < dealer.getValue()) {
            System.out.println("\nДилер выиграл (" + dealer.getValue()
                    + " против " + player.getValue() + " у вас).");
        } else {
            System.out.println("\nНичья (" + player.getValue() + " = "
                    + dealer.getValue() + ")! Ставка возвращается.");
            player.addBalance(bet);
        }
    }

    private int askForBet() {
        while (true) {
            System.out.print("\nСделайте ставку (минимум 100, максимум "
                    + player.getBalance() + "): ");
            try {
                int bet = Integer.parseInt(scanner.nextLine());
                if (bet >= 100 && bet <= player.getBalance()) {
                    return bet;
                }
                System.out.println("Недопустимая сумма ставки!");
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите число!");
            }
        }
    }
}