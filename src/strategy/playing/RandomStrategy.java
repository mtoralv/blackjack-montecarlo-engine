package strategy.playing;

import java.util.SplittableRandom;

import game.Action;
import game.Card;
import game.Hand;
import game.Options;

public final class RandomStrategy implements PlayingStrategy {

    private final SplittableRandom rng;

    public RandomStrategy(SplittableRandom rng) {
        this.rng = rng;
    }

    @Override
    public Action decide(Hand hand, Card dealerUpcard, Options options) {
        return rng.nextBoolean() ? Action.HIT : Action.STAND;
    }

    @Override
    public String name() {
        return "Random";
    }
}
