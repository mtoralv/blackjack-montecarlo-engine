package strategy.playing;

import game.Action;
import game.Card;
import game.Hand;
import game.Options;

public final class OnlyStand implements PlayingStrategy {

    @Override
    public Action decide(Hand hand, Card dealerUpcard, Options options) {
        return Action.STAND;
    }

    @Override
    public String name() {
        return "OnlyStand";
    }
}
