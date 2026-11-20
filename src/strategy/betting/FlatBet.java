package strategy.betting;

public final class FlatBet implements BettingStrategy {

    @Override
    public double nextBet(double unit) {
        return unit;
    }

    @Override
    public String name() {
        return "Flat";
    }
}
