package strategy.bettingStrategy;

import game.Card;

public interface BettingStrategy {
    
    String getName();

    default int getBet(int baseBet) 
    {
        return baseBet;
    }

    default void seeCard(Card card) 
    {
    }

    default void resetCount() 
    {
    }

    default void roundResult(int result) 
    {
    }


    
}
