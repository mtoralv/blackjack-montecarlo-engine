package strategy.betting;

import game.CardListener;
import game.Rules;

public interface BettingStrategy extends CardListener {

    String name();

    default void reset(Rules rules) {
    }

    double nextBet(double unit);

    default void onRoundEnd(double bet, double net) {
    }
}
