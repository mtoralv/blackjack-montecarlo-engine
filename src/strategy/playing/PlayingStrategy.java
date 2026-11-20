package strategy.playing;

import game.Action;
import game.Card;
import game.Hand;
import game.Options;

public interface PlayingStrategy {

    Action decide(Hand hand, Card dealerUpcard, Options options);

    String name();
}
