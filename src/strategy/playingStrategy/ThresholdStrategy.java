package strategy.playingStrategy;

import game.Hand;
import game.Card;

public class ThresholdStrategy implements PlayingStrategy {
    public String decide(Hand playerHand, Card dealerUpcard) {
    
        if(playerHand.getTotal() < 17) 
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
        return "ThresholdStrategy";
    }
}