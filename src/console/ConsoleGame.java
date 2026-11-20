package console;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.SplittableRandom;

import game.Action;
import game.Card;
import game.CardListener;
import game.Hand;
import game.Options;
import game.RoundResult;
import game.Rules;
import game.Shoe;
import game.Table;
import strategy.playing.BasicStrategy;
import strategy.playing.PlayingStrategy;
import util.Colors;

public final class ConsoleGame implements PlayingStrategy {

    private final Rules rules;
    private final Table table;
    private final Scanner in = new Scanner(System.in);
    private final BasicStrategy coach;
    private double balance;

    public ConsoleGame(Rules rules, double bankroll, long seed) {
        this.rules = rules;
        this.table = new Table(rules, new Shoe(rules.decks(), rules.penetration(), new SplittableRandom(seed)));
        this.coach = new BasicStrategy(rules);
        this.balance = bankroll;
    }

    public void run() {
        while (balance >= 1) {
            clearScreen();
            header();
            double bet = askBet();
            if (bet <= 0) break;
            RoundResult result = table.playRound(bet, balance, this, CardListener.NONE);
            balance += result.net();
            showResult(result);
            if (balance < 1) {
                System.out.println(Colors.RED + "You're out of chips. Game over." + Colors.RESET);
                return;
            }
            System.out.print("Press Enter to deal again (q to quit): ");
            if (in.nextLine().trim().equalsIgnoreCase("q")) break;
        }
        System.out.printf(Locale.ROOT, "Thanks for playing! You leave with %s chips.%n", money(balance));
    }

    @Override
    public Action decide(Hand hand, Card dealerUpcard, Options options) {
        while (true) {
            clearScreen();
            header();
            System.out.println(Colors.YELLOW + "DEALER " + Colors.RESET + "    " + card(dealerUpcard) + Colors.MAGENTA + "[??]" + Colors.RESET);
            System.out.println(Colors.DIM + " ------------------------------------" + Colors.RESET);
            String label = options.handCount() > 1
                    ? "YOUR HAND " + (options.handIndex() + 1) + "/" + options.handCount()
                    : "YOUR HAND";
            System.out.println(Colors.CYAN + label + Colors.RESET + " " + hand(hand));
            System.out.println();

            StringBuilder menu = new StringBuilder("(h) Hit  (s) Stand");
            if (options.canDouble()) menu.append("  (d) Double");
            if (options.canSplit()) menu.append("  (p) Split");
            if (options.canSurrender()) menu.append("  (r) Surrender");
            menu.append("  (?) Hint: ");
            System.out.print(menu);

            String input = in.nextLine().trim().toLowerCase(Locale.ROOT);
            switch (input) {
                case "h": return Action.HIT;
                case "s": return Action.STAND;
                case "d": if (options.canDouble()) return Action.DOUBLE_OR_HIT; break;
                case "p": if (options.canSplit()) return Action.SPLIT; break;
                case "r": if (options.canSurrender()) return Action.SURRENDER_OR_HIT; break;
                case "?":
                    System.out.println("Basic strategy says: " + Colors.GREEN
                            + describe(coach.decide(hand, dealerUpcard, options), options) + Colors.RESET);
                    System.out.print("Press Enter...");
                    in.nextLine();
                    continue;
                default:
            }
        }
    }

    private static String describe(Action action, Options o) {
        return switch (action) {
            case HIT -> "hit";
            case STAND -> "stand";
            case SPLIT -> "split";
            case DOUBLE_OR_HIT -> o.canDouble() ? "double" : "hit";
            case DOUBLE_OR_STAND -> o.canDouble() ? "double" : "stand";
            case SURRENDER_OR_HIT -> o.canSurrender() ? "surrender" : "hit";
            case SURRENDER_OR_STAND -> o.canSurrender() ? "surrender" : "stand";
        };
    }

    @Override
    public String name() {
        return "Human";
    }

    private double askBet() {
        while (true) {
            System.out.printf(Locale.ROOT, "Place your bet (1-%s, 0 to leave): ", money(balance));
            String line = in.nextLine().trim();
            try {
                double bet = Double.parseDouble(line);
                if (bet == 0) return 0;
                if (bet >= 1 && bet <= balance) return bet;
            } catch (NumberFormatException ignored) {
            }
            System.out.println(Colors.RED + "Invalid bet." + Colors.RESET);
        }
    }

    private void showResult(RoundResult r) {
        clearScreen();
        header();
        System.out.println(Colors.YELLOW + "DEALER " + Colors.RESET + "   " + hand(r.dealerHand()));
        System.out.println(Colors.DIM + " ------------------------------------" + Colors.RESET);
        List<Hand> hands = r.playerHands();
        for (int i = 0; i < hands.size(); i++) {
            String label = hands.size() > 1 ? "HAND " + (i + 1) + "  " : "YOU    ";
            System.out.println(Colors.CYAN + label + Colors.RESET + "   " + hand(hands.get(i)) + "   " + outcome(r, i));
        }
        System.out.println();
        String color = r.net() > 0 ? Colors.GREEN : r.net() < 0 ? Colors.RED : Colors.YELLOW;
        System.out.printf(Locale.ROOT, "Round: %s%s%s%n%n", color, signed(r.net()), Colors.RESET);
    }

    private String outcome(RoundResult r, int i) {
        double net = r.handNets().get(i);
        String text;
        if (r.playerBlackjack() && net > 0) text = "Blackjack!";
        else if (r.surrendered()) text = "Surrendered";
        else if (r.playerHands().get(i).isBust()) text = "Bust";
        else if (r.dealerBlackjack() && net < 0) text = "Dealer blackjack";
        else if (net > 0) text = "Win";
        else if (net < 0) text = "Lose";
        else text = "Push";
        return text + " (" + signed(net) + ")";
    }

    private void header() {
        System.out.println(Colors.YELLOW + " |------------------------------------|");
        System.out.println(" |              BLACKJACK             |");
        System.out.println(" |------------------------------------|" + Colors.RESET);
        System.out.println(Colors.DIM + "  " + rules.describe() + Colors.RESET);
        System.out.println("  Chips: " + Colors.GREEN + money(balance) + Colors.RESET);
        System.out.println();
    }

    private static String hand(Hand hand) {
        StringBuilder sb = new StringBuilder();
        for (Card c : hand.cards()) sb.append(card(c));
        return sb.append("  ").append(Colors.BOLD).append(hand.isSoft() ? "soft " : "").append(hand.total())
                .append(Colors.RESET).toString();
    }

    private static String card(Card c) {
        String color = c.suit().isRed() ? Colors.RED : Colors.BLUE;
        return Colors.MAGENTA + "[" + color + c + Colors.MAGENTA + "]" + Colors.RESET;
    }

    private static String money(double v) {
        return v == Math.rint(v) ? String.format(Locale.ROOT, "%.0f", v) : String.format(Locale.ROOT, "%.1f", v);
    }

    private static String signed(double v) {
        return (v > 0 ? "+" : "") + money(v);
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
