package strategy.playing;

import game.Action;
import game.Card;
import game.Hand;
import game.Options;

public final class ThresholdStrategy implements PlayingStrategy {

    @Override
    public Action decide(Hand hand, Card dealerUpcard, Options options) {
        return hand.total() < 17 ? Action.HIT : Action.STAND;
    }

    @Override
    public String name() {
        return "Threshold17";
    }
}
