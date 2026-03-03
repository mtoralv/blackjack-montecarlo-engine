package strategy;

import game.Hand;
import game.Card;

public class ThresholdStrategy implements Strategy {
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