package strategy.betting;

import game.Card;
import game.Rules;

public final class HiLoStrategy implements BettingStrategy {

    private final int maxSpread;
    private int decks;
    private int runningCount;
    private int cardsSeen;

    public HiLoStrategy(int maxSpread) {
        if (maxSpread < 1) throw new IllegalArgumentException("maxSpread must be >= 1");
        this.maxSpread = maxSpread;
    }

    @Override
    public void reset(Rules rules) {
        this.decks = rules.decks();
        onShuffle();
    }

    @Override
    public void onCard(Card card) {
        runningCount += card.hiLo();
        cardsSeen++;
    }

    @Override
    public void onShuffle() {
        runningCount = 0;
        cardsSeen = 0;
    }

    public int runningCount() {
        return runningCount;
    }

    public double trueCount() {
        double decksRemaining = Math.max(0.25, (decks * 52 - cardsSeen) / 52.0);
        return runningCount / decksRemaining;
    }

    @Override
    public double nextBet(double unit) {
        // bet ramp: true count - 1 units, between 1 and maxSpread
        int units = (int) Math.floor(trueCount()) - 1;
        units = Math.max(1, Math.min(maxSpread, units));
        return unit * units;
    }

    @Override
    public String name() {
        return "HiLo1-" + maxSpread;
    }
}
