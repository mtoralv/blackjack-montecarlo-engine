package strategy;

import game.Hand;
import game.Card;

public class BasicStrategy implements Strategy {
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
}