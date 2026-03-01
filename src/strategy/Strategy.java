package strategy;

import game.Hand;
import game.Card;

public interface Strategy {
    String decide(Hand playerHand, Card dealerUpcard);
}