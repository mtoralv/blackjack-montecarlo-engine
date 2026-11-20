package strategy.betting;

import game.Rules;

public final class OneThreeTwoSix implements BettingStrategy {

    private static final int[] SEQUENCE = {1, 3, 2, 6};
    private int step;

    @Override
    public void reset(Rules rules) {
        step = 0;
    }

    @Override
    public double nextBet(double unit) {
        return unit * SEQUENCE[step];
    }

    @Override
    public void onRoundEnd(double bet, double net) {
        if (net > 0) {
            step = (step + 1) % SEQUENCE.length;
        } else if (net < 0) {
            step = 0;
        }
    }

    @Override
    public String name() {
        return "1-3-2-6";
    }
}
