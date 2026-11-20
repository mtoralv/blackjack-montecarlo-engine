package strategy.betting;

import game.Rules;

public final class Martingale implements BettingStrategy {

    private double lastBet;
    private double lastNet;

    @Override
    public void reset(Rules rules) {
        lastBet = 0;
        lastNet = 0;
    }

    @Override
    public double nextBet(double unit) {
        if (lastBet == 0 || lastNet > 0) return unit;
        if (lastNet < 0) return lastBet * 2;
        return lastBet;
    }

    @Override
    public void onRoundEnd(double bet, double net) {
        lastBet = bet;
        lastNet = net;
    }

    @Override
    public String name() {
        return "Martingale";
    }
}
