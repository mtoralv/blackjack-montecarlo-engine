package strategy.betting;

import game.Rules;

public final class OscarsGrind implements BettingStrategy {

    private double units = 1;
    private double cycleProfit; // each cycle tries to win 1 unit
    private double unit = 1;

    @Override
    public void reset(Rules rules) {
        units = 1;
        cycleProfit = 0;
    }

    @Override
    public double nextBet(double unit) {
        this.unit = unit;
        return unit * units;
    }

    @Override
    public void onRoundEnd(double bet, double net) {
        cycleProfit += net / unit;
        if (cycleProfit >= 1) {
            units = 1;
            cycleProfit = 0;
        } else if (net > 0) {
            units = Math.max(1, Math.min(units + 1, 1 - cycleProfit));
        }
    }

    @Override
    public String name() {
        return "OscarsGrind";
    }
}
