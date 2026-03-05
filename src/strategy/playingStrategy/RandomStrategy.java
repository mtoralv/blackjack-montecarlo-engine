package strategy.playingStrategy;

import game.Hand;
import game.Card;

public class RandomStrategy implements PlayingStrategy {
    public String decide(Hand playerHand, Card dealerUpcard) {
    
        if(Math.random() < 0.5) 
        {
            return "h";
        }
        else 
        {
            return "s";
        }
    
    }

    public String getName()
    {
        return "RandomStrategy";
    }
}

