package strategy;

import game.Hand;
import game.Card;

public interface Strategy {
    String decide(Hand playerHand, Card dealerUpcard);
    String getName();
    default void seeCard(Card card) 
    {
    }
    default void resetCount() 
    {
    }
    default int getBet(int baseBet) 
    {
        return baseBet;
    }   
}