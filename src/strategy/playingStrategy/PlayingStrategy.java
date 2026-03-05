package strategy.playingStrategy;

import game.Hand;
import game.Card;

public interface PlayingStrategy {
    String decide(Hand playerHand, Card dealerUpcard);
    String getName();
   
}