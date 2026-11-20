import java.util.ArrayList;
import java.util.List;

import game.Action;
import game.Card;
import game.CardListener;
import game.Hand;
import game.Options;
import game.Rank;
import game.RoundResult;
import game.Rules;
import game.Shoe;
import game.Table;
import simulation.MonteCarlo;
import simulation.SimulationConfig;
import simulation.SimulationResult;
import strategy.betting.HiLoStrategy;
import strategy.betting.Martingale;
import strategy.betting.OneThreeTwoSix;
import strategy.betting.OscarsGrind;
import strategy.playing.BasicStrategy;
import strategy.playing.PlayingStrategy;

import static game.Rank.ACE;
import static game.Rank.EIGHT;
import static game.Rank.FIVE;
import static game.Rank.FOUR;
import static game.Rank.KING;
import static game.Rank.NINE;
import static game.Rank.QUEEN;
import static game.Rank.SEVEN;
import static game.Rank.SIX;
import static game.Rank.TEN;
import static game.Rank.THREE;
import static game.Rank.TWO;

// No JUnit, just a small runner. Stacked shoes deal in order: player, dealer up, player, hole, then draws.
public final class EngineTests {

    private static final Rules RULES = Rules.standard();
    private static final double EPS = 1e-9;
    private static int passed;
    private static final List<String> failures = new ArrayList<>();

    public static void main(String[] args) {
        test("hand totals and soft hands", EngineTests::handTotals);
        test("blackjack and pair detection", EngineTests::blackjackAndPairs);
        test("player blackjack pays 3:2", EngineTests::playerBlackjack);
        test("dealer peek ends the round before the player acts", EngineTests::dealerPeek);
        test("blackjack against blackjack is a push", EngineTests::blackjackPush);
        test("double down wins twice the bet", EngineTests::doubleDown);
        test("late surrender loses half the bet", EngineTests::surrender);
        test("split eights and double after split", EngineTests::splitAndDouble);
        test("split aces receive one card and 21 is not a blackjack", EngineTests::splitAces);
        test("dealer stands on soft 17 (S17) and hits it (H17)", EngineTests::soft17);
        test("dealer does not draw when the player busts", EngineTests::dealerSkipsAfterBust);
        test("hole card is only seen when revealed", EngineTests::holeCardVisibility);
        test("basic strategy chart spot checks", EngineTests::basicStrategyChart);
        test("Hi-Lo running count, true count and bet ramp", EngineTests::hiLo);
        test("betting progressions", EngineTests::progressions);
        test("simulations are reproducible", EngineTests::reproducible);
        test("basic strategy house edge matches the published value", EngineTests::houseEdge);

        System.out.println();
        System.out.println(passed + " passed, " + failures.size() + " failed");
        if (!failures.isEmpty()) {
            failures.forEach(f -> System.out.println("  FAILED: " + f));
            System.exit(1);
        }
    }


    private static void handTotals() {
        Hand h = hand(ACE, SIX);
        check(h.total() == 17 && h.isSoft(), "A6 is soft 17");
        h.add(Card.of(TEN));
        check(h.total() == 17 && !h.isSoft(), "A6+T is hard 17");
        Hand aces = hand(ACE, ACE, NINE);
        check(aces.total() == 21 && aces.isSoft(), "AA9 is soft 21");
        Hand bust = hand(KING, QUEEN, FIVE);
        check(bust.isBust() && bust.total() == 25, "KQ5 busts");
    }

    private static void blackjackAndPairs() {
        check(hand(ACE, KING).isBlackjack(), "AK is a blackjack");
        check(!hand(SEVEN, SEVEN, SEVEN).isBlackjack(), "777 is not a blackjack");
        check(hand(KING, QUEEN).isPair(), "two ten-valued cards are a pair");
        check(!hand(KING, NINE).isPair(), "K9 is not a pair");
    }

    private static void playerBlackjack() {
        RoundResult r = play(RULES, new BasicStrategy(RULES), ACE, NINE, KING, SEVEN);
        near(r.net(), 1.5, "net");
        check(r.playerBlackjack(), "flagged as blackjack");
    }

    private static void dealerPeek() {
        RoundResult r = play(RULES, neverAsked(), KING, ACE, QUEEN, KING);
        near(r.net(), -1, "only the initial bet is lost");
        check(r.dealerBlackjack(), "dealer blackjack flagged");
    }

    private static void blackjackPush() {
        RoundResult r = play(RULES, neverAsked(), ACE, ACE, KING, KING);
        near(r.net(), 0, "push");
    }

    private static void doubleDown() {
        // 6+5 vs 6 -> double, gets a 10. dealer 16 draws 9 and busts
        RoundResult r = play(RULES, new BasicStrategy(RULES), SIX, SIX, FIVE, TEN, TEN, NINE);
        near(r.net(), 2, "net");
        near(r.totalWagered(), 2, "wagered");
        check(r.doubles() == 1, "one double");
    }

    private static void surrender() {
        // 16 vs 10 -> surrender
        RoundResult r = play(RULES, new BasicStrategy(RULES), TEN, TEN, SIX, SEVEN);
        near(r.net(), -0.5, "net");
        check(r.surrendered(), "surrender flagged");
    }

    private static void splitAndDouble() {
        // 8,8 vs 6: split, 8+3 doubles (21), 8+2 doubles (19), dealer busts
        RoundResult r = play(RULES, new BasicStrategy(RULES), EIGHT, SIX, EIGHT, TEN, THREE, TEN, TWO, NINE, TEN);
        check(r.splits() == 1 && r.doubles() == 2, "one split, two doubles");
        near(r.totalWagered(), 4, "wagered");
        near(r.net(), 4, "net");
        check(r.playerHands().size() == 2, "two hands");
    }

    private static void splitAces() {
        // A,A vs 6: A+9 and A+K, dealer makes 21
        RoundResult r = play(RULES, new BasicStrategy(RULES), ACE, SIX, ACE, TEN, NINE, KING, FIVE);
        check(r.playerHands().get(0).size() == 2 && r.playerHands().get(1).size() == 2, "one card per split ace");
        check(!r.playerBlackjack(), "21 after split is not a blackjack");
        near(r.net(), -1, "net");
    }

    private static void soft17() {
        // player 19, dealer A+6 and the next card is a 4
        RoundResult s17 = play(RULES, new BasicStrategy(RULES), TEN, ACE, NINE, SIX, FOUR);
        near(s17.net(), 1, "S17: dealer stands on 17, player wins");
        Rules h17Rules = RULES.withDealerHitsSoft17(true);
        RoundResult h17 = play(h17Rules, new BasicStrategy(h17Rules), TEN, ACE, NINE, SIX, FOUR);
        near(h17.net(), -1, "H17: dealer draws to 21, player loses");
    }

    private static void dealerSkipsAfterBust() {
        RoundResult r = play(RULES, new BasicStrategy(RULES), TEN, SEVEN, SIX, TEN, TEN, FIVE);
        near(r.net(), -1, "net");
        check(r.dealerHand().size() == 2, "dealer did not draw");
    }

    private static void holeCardVisibility() {
        List<Card> seen = new ArrayList<>();
        int[] seenWhenAsked = {-1};
        PlayingStrategy recorder = new PlayingStrategy() {
            public Action decide(Hand hand, Card up, Options o) {
                seenWhenAsked[0] = seen.size();
                return Action.STAND;
            }

            public String name() {
                return "recorder";
            }
        };
        Table table = new Table(RULES, Shoe.stacked(cards(TEN, SEVEN, NINE, TEN)));
        table.playRound(1, Double.POSITIVE_INFINITY, recorder, new CardListener() {
            public void onCard(Card c) {
                seen.add(c);
            }
        });
        check(seenWhenAsked[0] == 3, "player decides having seen 3 cards, saw " + seenWhenAsked[0]);
        check(seen.size() == 4, "hole card revealed at the end");
    }


    private static void basicStrategyChart() {
        BasicStrategy bs = new BasicStrategy(RULES);
        Options all = new Options(true, true, true, 0, 1);
        Options noSplit = new Options(true, false, true, 0, 1);
        Options none = new Options(false, false, false, 0, 1);
        expect(bs, hand(TEN, SIX), TEN, all, Action.SURRENDER_OR_HIT);
        expect(bs, hand(TEN, TWO), FOUR, all, Action.STAND);
        expect(bs, hand(TEN, TWO), THREE, all, Action.HIT);
        expect(bs, hand(SIX, FIVE), ACE, all, Action.HIT);
        expect(bs, hand(SIX, FIVE), TEN, all, Action.DOUBLE_OR_HIT);
        expect(bs, hand(ACE, SEVEN), THREE, all, Action.DOUBLE_OR_STAND);
        expect(bs, hand(ACE, SEVEN), NINE, all, Action.HIT);
        expect(bs, hand(EIGHT, EIGHT), ACE, all, Action.SPLIT);
        expect(bs, hand(ACE, ACE), TEN, all, Action.SPLIT);
        expect(bs, hand(ACE, ACE), TEN, noSplit, Action.HIT);
        expect(bs, hand(NINE, NINE), SEVEN, all, Action.STAND);
        expect(bs, hand(TEN, TEN), SIX, all, Action.STAND);
        expect(bs, hand(FIVE, FIVE), NINE, all, Action.DOUBLE_OR_HIT);
        expect(bs, hand(TEN, TWO, THREE, ACE), SIX, none, Action.STAND);
        Rules h17 = RULES.withDealerHitsSoft17(true);
        expect(new BasicStrategy(h17), hand(SIX, FIVE), ACE, all, Action.DOUBLE_OR_HIT);
    }

    private static void hiLo() {
        HiLoStrategy hilo = new HiLoStrategy(12);
        hilo.reset(Rules.standard().withDecks(1));
        for (Rank r : new Rank[] {TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE}) hilo.onCard(Card.of(r));
        check(hilo.runningCount() == 5, "running count 5, got " + hilo.runningCount());
        near(hilo.nextBet(10), 40, "bet ramp TC-1");
        hilo.onShuffle();
        near(hilo.nextBet(10), 10, "back to one unit after the shuffle");
        hilo.onCard(Card.of(KING));
        hilo.onCard(Card.of(ACE));
        check(hilo.runningCount() == -2, "tens and aces count -1");
    }

    private static void progressions() {
        Martingale m = new Martingale();
        m.reset(RULES);
        double bet = m.nextBet(1);
        double[] nets = {-1, -1, -1, 1};
        double[] expected = {1, 2, 4, 8, 1};
        for (int i = 0; i < nets.length; i++) {
            near(bet, expected[i], "martingale step " + i);
            m.onRoundEnd(bet, nets[i] * bet);
            bet = m.nextBet(1);
        }
        near(bet, expected[4], "martingale resets after a win");

        OneThreeTwoSix p = new OneThreeTwoSix();
        p.reset(RULES);
        double[] seq = {1, 3, 2, 6, 1};
        for (double s : seq) {
            double b = p.nextBet(1);
            near(b, s, "1-3-2-6 step");
            p.onRoundEnd(b, b);
        }

        OscarsGrind o = new OscarsGrind();
        o.reset(RULES);
        double[] results = {-1, -1, 1, 1, 1};
        double[] bets = {1, 1, 1, 2, 1};
        for (int i = 0; i < results.length; i++) {
            double b = o.nextBet(1);
            near(b, bets[i], "oscar step " + i);
            o.onRoundEnd(b, results[i] * b);
        }
    }


    private static void reproducible() {
        SimulationConfig cfg = config("basic", "hilo", 8, 2_000, 7);
        SimulationResult a = MonteCarlo.run(cfg);
        SimulationResult b = MonteCarlo.run(cfg);
        near(a.stats().returnOnAction(), b.stats().returnOnAction(), "same seed, same result");
        check(a.stats().returnOnAction() != MonteCarlo.run(config("basic", "hilo", 8, 2_000, 8)).stats().returnOnAction(),
                "different seed, different result");
    }

    private static void houseEdge() {
        // published ~0.44% without surrender, surrender is worth ~0.07 -> ~0.37%
        SimulationResult r = MonteCarlo.run(config("basic", "flat", 200, 10_000, 2026));
        double edge = r.stats().houseEdge();
        double se = r.stats().houseEdgeStdError();
        check(Math.abs(edge - 0.0037) < 4 * se + 0.0005,
                String.format("house edge %.3f%% +- %.3f%%", 100 * edge, 100 * se));
    }


    private static SimulationConfig config(String play, String bet, int sims, int hands, long seed) {
        return new SimulationConfig(RULES, play, bet, 12, sims, hands, 1, Double.POSITIVE_INFINITY, 500, seed, 100);
    }

    private static RoundResult play(Rules rules, PlayingStrategy strategy, Rank... order) {
        return new Table(rules, Shoe.stacked(cards(order))).playRound(1, Double.POSITIVE_INFINITY, strategy, CardListener.NONE);
    }

    private static Card[] cards(Rank... ranks) {
        Card[] cards = new Card[ranks.length];
        for (int i = 0; i < ranks.length; i++) cards[i] = Card.of(ranks[i]);
        return cards;
    }

    private static Hand hand(Rank... ranks) {
        Hand h = new Hand();
        for (Rank r : ranks) h.add(Card.of(r));
        return h;
    }

    private static PlayingStrategy neverAsked() {
        return new PlayingStrategy() {
            public Action decide(Hand hand, Card up, Options o) {
                throw new AssertionError("player should not be asked to act");
            }

            public String name() {
                return "never";
            }
        };
    }

    private static void expect(BasicStrategy bs, Hand hand, Rank up, Options o, Action expected) {
        Action got = bs.decide(hand, Card.of(up), o);
        check(got == expected, hand + " vs " + up + ": expected " + expected + ", got " + got);
    }

    private static void near(double actual, double expected, String what) {
        check(Math.abs(actual - expected) < EPS, what + ": expected " + expected + ", got " + actual);
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void test(String name, Runnable body) {
        try {
            body.run();
            passed++;
            System.out.println("  ok    " + name);
        } catch (AssertionError | RuntimeException e) {
            failures.add(name + " -> " + e.getMessage());
            System.out.println("  FAIL  " + name + " -> " + e.getMessage());
        }
    }
}
