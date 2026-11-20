package strategy.playing;

import game.Action;
import game.Card;
import game.Hand;
import game.Options;

public final class OnlyHit implements PlayingStrategy {

    @Override
    public Action decide(Hand hand, Card dealerUpcard, Options options) {
        return Action.HIT;
    }

    @Override
    public String name() {
        return "OnlyHit";
    }
}
